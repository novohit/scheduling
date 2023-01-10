package com.wyu.scheduling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 配置定时任务的线程池
 *
 * @author zwx
 * @date 2023-01-09 17:01
 */
@Configuration
public class SchedulingConfig {

    /**
     * ThreadPoolTaskScheduler，可以很方便的对重复执行的任务进行调度管理
     * 相比于通过java自带的周期性任务线程池ScheduleThreadPoolExecutor，此bean对象支持根据cron表达式创建周期性任务。
     * 当然，ThreadPoolTaskScheduler其实底层使用也是java自带的线程池。
     *
     * 它的核心线程池大小就是我们配置的 poolSize属性，最大线程池大小是 Integer.MAX_VALUE，keepAliveTime 为 0
     * 这里用到的队列是 DelayedWorkQueue ，这个队列有一个属性 private final DelayQueue<RunnableScheduledFuture> dq = new DelayQueue<RunnableScheduledFuture>();
     * 对这个队列的操作实际是是对这个 DelayQueue 的操作，这个队列大小是 Integer.MAX_VALUE，所以线程数量肯定是够用了
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 配置核心线程池大小
        threadPoolTaskScheduler.setPoolSize(4);
        // 当任务执行cancel()后虽然任务被取消但是任务仍然在队列中
        // 这里设置将取消的任务从队列中清除
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduling-task-");
        return threadPoolTaskScheduler;
    }
}
