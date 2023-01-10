package com.wyu.scheduling.service;

import com.wyu.scheduling.config.CornTaskRegistrar;
import com.wyu.scheduling.config.SchedulingRunnable;
import com.wyu.scheduling.config.SpringContextUtils;
import com.wyu.scheduling.exception.CommonException;
import com.wyu.scheduling.model.SysJob;
import com.wyu.scheduling.repository.SysJobRepository;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zwx
 * @date 2023-01-09 23:02
 */
@Service
public class SysJobService {
    @Autowired
    private SysJobRepository sysJobRepository;


    @Autowired
    private CornTaskRegistrar cornTaskRegistrar;

    public List<SysJob> getSysJobByState(Integer state) {
        return this.sysJobRepository.findAllByState(state);
    }

    public List<SysJob> getAllJobs() {
        return this.sysJobRepository.findAll();
    }

    public void addJob(SysJob sysJob) {
        try {
            SpringContextUtils.getBean(sysJob.getBeanName());
        } catch (NoSuchBeanDefinitionException e) {
            throw new CommonException("找不到该bean");
        }
        List<SysJob> all = sysJobRepository.findAll();
        for (SysJob job : all) {
            if (job.equals(sysJob)) {
                //作业重复，添加失败
                throw new CommonException("作业重复");
            }
        }
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        if (schedulingRunnable.getMethod() == null) {
            throw new CommonException("找不到该方法");
        }
        //添加 如果新加的job是开启状态，就顺便开启
        sysJobRepository.save(sysJob);
        if (sysJob.getState() == 1) {
            this.cornTaskRegistrar.addCronTask(schedulingRunnable, sysJob.getCronExpression());
        }
    }

    public void updateJob(SysJob sysJob) {
        sysJob.setUpdateTime(new Date());
        this.sysJobRepository.save(sysJob);
        SchedulingRunnable runnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        if (sysJob.getState() == 1) {
            // 定时任务为开启状态
            cornTaskRegistrar.addCronTask(runnable, sysJob.getCronExpression());
        } else {
            cornTaskRegistrar.removeCronTask(runnable);
        }
    }

    public void deleteJobsById(Long id) {
        SysJob sysJob = sysJobRepository.findById(id).orElseThrow(() -> new CommonException("不存在该作业"));
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        this.cornTaskRegistrar.removeCronTask(schedulingRunnable);
        sysJobRepository.delete(sysJob);
    }
}
