package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.JqScheduler;

@Repository
public interface JqschedulerRepository extends JpaRepositoryImplementation<JqScheduler, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ACTIVE_SCHEDULERS)
	List<JqScheduler> retrieveSchedulers(Integer isActive);

}
