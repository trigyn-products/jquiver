package com.trigyn.jws.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.dao.QueryStore;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.vo.DashletPropertyVO;

@Repository
public interface IDashletPropertiesRepository extends JpaRepository<DashletProperties, String>{
	
	@Query(QueryStore.JPQ_QUERY_TO_GET_DAHSLET_PROPERTIES_BY_ID)
	List<DashletPropertyVO> findDashletPropertyByDashletId(String dashletId, Integer isDeleted);

}
