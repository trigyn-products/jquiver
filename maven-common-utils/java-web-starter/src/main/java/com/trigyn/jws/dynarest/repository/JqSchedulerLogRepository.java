package com.trigyn.jws.dynarest.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JqSchedulerLog;

@Repository
public interface JqSchedulerLogRepository extends JpaRepositoryImplementation<JqSchedulerLog, String> {

}
