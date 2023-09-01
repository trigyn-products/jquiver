package com.trigyn.jws.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.dao.QueryStore;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dashboard.vo.DashletVO;

@Repository
public interface IDashletRepository extends JpaRepository<Dashlet, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_DASHLET_BY_CONTEXT_ID)
	List<DashboardDashletVO> findDashletByContextId(String dashboardId);

	@Query(QueryStore.JPA_QUERY_TO_GET_DAHSLET_DETAILS_BY_ID)
	DashletVO findDashletByDashletId(String dashletId);
}
