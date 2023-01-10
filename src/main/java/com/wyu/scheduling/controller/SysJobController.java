package com.wyu.scheduling.controller;

import com.wyu.scheduling.model.ResultVO;
import com.wyu.scheduling.model.SysJob;
import com.wyu.scheduling.service.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zwx
 * @date 2023-01-10 14:17
 */
@RestController
@RequestMapping("/jobs")
public class SysJobController {

    @Autowired
    private SysJobService sysJobService;

    @GetMapping("/")
    public List<SysJob> getAllJobs() {
        return this.sysJobService.getAllJobs();
    }

    @PostMapping("/")
    public ResultVO addJob(@RequestBody SysJob sysJob) {
        this.sysJobService.addJob(sysJob);
        return ResultVO.success("作业添加成功");
    }

    @PutMapping("/")
    public ResultVO update(@RequestBody SysJob sysJob) {
        this.sysJobService.updateJob(sysJob);
        return ResultVO.success("作业更新成功");
    }

    @DeleteMapping("/{id}")
    public ResultVO deleteJobs(@PathVariable Long id) {
        this.sysJobService.deleteJobsById(id);
        return ResultVO.success("作业删除成功");
    }
}
