package com.wyu.scheduling.config;

import com.wyu.scheduling.model.SysJob;
import com.wyu.scheduling.service.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用启动时 初始化已经存在数据库的任务
 *
 * @author zwx
 * @date 2023-01-09 22:41
 */
@Component
public class InitTask implements CommandLineRunner {

    @Autowired
    private CornTaskRegistrar cornTaskRegistrar;

    @Autowired
    private SysJobService sysJobService;

    /**
     * 项目启动时 该方法会被自动执行
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        List<SysJob> list = sysJobService.getSysJobByState(1);
        // 遍历 执行每一个定时任务
        list.forEach(sysJob -> cornTaskRegistrar.addCronTask(
                new SchedulingRunnable(sysJob.getBeanName(),
                        sysJob.getMethodName(),
                        sysJob.getMethodParams()),
                sysJob.getCronExpression()));
    }
}
