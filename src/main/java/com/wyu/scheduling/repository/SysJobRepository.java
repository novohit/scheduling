package com.wyu.scheduling.repository;

import com.wyu.scheduling.model.SysJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zwx
 * @date 2023-01-09 22:39
 */
public interface SysJobRepository extends JpaRepository<SysJob, Long> {
    List<SysJob> findAllByState(Integer state);
}
