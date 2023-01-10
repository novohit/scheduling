package com.wyu.scheduling.config;

import java.util.concurrent.ScheduledFuture;

/**
 * 封装一下
 *
 * @author zwx
 * @date 2023-01-09 21:51
 */
public final class ScheduledTask {
    // 定时任务返回的结果
    volatile ScheduledFuture<?> future;

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
