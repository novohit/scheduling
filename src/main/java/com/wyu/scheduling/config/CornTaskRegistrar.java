package com.wyu.scheduling.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zwx
 * @date 2023-01-09 21:54
 */
@Component
public class CornTaskRegistrar implements DisposableBean {

    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();

    // 线程池
    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler taskScheduler() {
        return this.taskScheduler;
    }

    /**
     * 添加一个定时任务
     * @param runnable
     * @param cronExpression
     */
    public void addCronTask(Runnable runnable, String cronExpression) {
        this.addCronTask(new CronTask(runnable, cronExpression));
    }

    private void addCronTask(CronTask cronTask) {
        if (cronTask != null) {
            Runnable runnable = cronTask.getRunnable();
            if (this.scheduledTasks.containsKey(runnable)) {
                // 说明要添加的定时任务已经存在
                // 先已存在的定时任务移除再添加
                this.removeCronTask(runnable);
            }
            // 最重要的一步 添加一个定时任务
            ScheduledTask task = new ScheduledTask();
            // 将定时任务线程放到线程池里执行
            task.future = this.taskScheduler.schedule(runnable, cronTask.getTrigger());
            this.scheduledTasks.put(runnable, task);
        }
    }

    /**
     * 根据key:runnable移除定时任务
     * @param runnable
     */
    public void removeCronTask(Runnable runnable) {
        // 1. 从map中移除
        ScheduledTask task = this.scheduledTasks.remove(runnable);
        // 2. 取消正在执行的定时任务
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void destroy() throws Exception {
        // 1. 让所有定时任务停止执行
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }
        // 2. 清空集合
        this.scheduledTasks.clear();
    }
}
