package com.trigyn.jws.quartz.jobs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.quartz.jobs.QueryStore;
import com.trigyn.jws.quartz.models.entities.MailSchedule;

@Repository
public interface MailScheduleRepository extends JpaRepository<MailSchedule, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_MAIL_SCHEDULE_BY_GROUP_ID)
	List<MailSchedule> findBySendorGroupId(String mailScheduleGroupId);

}
