package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.JwsClusterState;

/**
 * @author Shrinath.Halki
 *
 */
public interface JQClusterStateRepository extends JpaRepositoryImplementation<JwsClusterState, String> {
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ACTIVE_CLUSTER_STATE)
	List<JwsClusterState> retrieveAllClusterByActiveState(Integer isActive);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ACTIVE_CLUSTER_STATE_BY_CLUSTER)
	JwsClusterState retrieveClusterByClusterState(Integer isActive, String instanceId);
}
