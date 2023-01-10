package com.wyu.scheduling.config;

import com.wyu.scheduling.exception.CommonException;
import com.wyu.scheduling.service.SysJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 每一个定时任务都对应一个子线程
 * 定时任务线程的配置
 * 封装一下Runnable
 *
 * @author zwx
 * @date 2023-01-09 16:42
 */
public class SchedulingRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingRunnable.class);

    private String beanName;

    private String methodName;

    private String params;

    private Object targetBean;

    private Method method;


    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName, methodName, null);
    }

    public SchedulingRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        init();
    }

    private void init() {
        targetBean = SpringContextUtils.getBean(beanName);
        try {
            // 方法有参数
            if (StringUtils.hasText(params)) {
                /**
                 * getDeclaredMethod()和getMethod()的区别：
                 * getDeclaredMethod()获取的是类自身声明的所有方法，包含public、protected和private方法
                 * getMethod()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法
                 */
                method = targetBean.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = targetBean.getClass().getDeclaredMethod(methodName);
            }
            // 修改方法的修饰符使其可以访问 因为有可能是私有的
            ReflectionUtils.makeAccessible(method);
        } catch (NoSuchMethodException e) {
            LOGGER.error("找不到该方法 bean:[{}],method:[{}],params:[{}]", beanName, methodName, params);
            CornTaskRegistrar cornTaskRegistrar = (CornTaskRegistrar) SpringContextUtils.getBean("cornTaskRegistrar");
            cornTaskRegistrar.removeCronTask(this);
            // e.printStackTrace();
        }
    }

    /**
     * 定时任务
     */
    @Override
    public void run() {
        if (method == null) {
            LOGGER.error("找不到该方法 bean:[{}],method:[{}],params:[{}]", beanName, methodName, params);
            CornTaskRegistrar cornTaskRegistrar = (CornTaskRegistrar) SpringContextUtils.getBean("cornTaskRegistrar");
            cornTaskRegistrar.removeCronTask(this);
            return;
        }
        LOGGER.info("定时任务开始执行 - bean:[{}],method:[{}],params:[{}]", beanName, methodName, params);
        long startTime = System.currentTimeMillis();
        try {
            if (StringUtils.hasText(params)) {
                method.invoke(targetBean, params);
            } else {
                method.invoke(targetBean);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("定时任务执行异常 - bean:[{}],method:[{}],params:[{}]", beanName, methodName, params, e);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("定时任务执行结束 - bean:[{}],method:[{}],params:[{}],耗时:[{}]ms", beanName, methodName, params, endTime - startTime);
    }

    /**
     * 该程序设置不允许执行重复的定时任务 所以重写下equals和hashCode方法
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        return Objects.equals(beanName, that.beanName) && Objects.equals(methodName, that.methodName) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, params);
    }

    public Method getMethod() {
        return method;
    }
}
