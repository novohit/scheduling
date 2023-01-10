package com.wyu.scheduling.taskdemo;

import org.springframework.stereotype.Component;

/**
 * @author zwx
 * @date 2023-01-09 23:13
 */
@Component
public class ScheduledTaskDemo {

    public void taskWithParams(String params) {
        System.out.println("执行带参数的定时任务..." + params);
    }

    public void taskWithoutParams() {
        System.out.println("执行带参数的定时任务...");
    }
}
