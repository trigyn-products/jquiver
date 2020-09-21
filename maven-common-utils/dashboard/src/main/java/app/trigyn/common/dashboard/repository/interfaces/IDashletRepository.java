package app.trigyn.common.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.dao.QueryStore;
import app.trigyn.common.dashboard.entities.Dashlet;
import app.trigyn.common.dashboard.vo.DashboardDashletVO;
import app.trigyn.common.dashboard.vo.DashletVO;

@Repository
public interface IDashletRepository extends JpaRepository<Dashlet, String>{

	@Query(QueryStore.JPA_QUERY_TO_GET_DASHLET_BY_CONTEXT_ID)
	List<DashboardDashletVO> findDashletByContextId(String contextId, String dashboardId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_DAHSLET_DETAILS_BY_ID)
	DashletVO findDashletByDashletId(String dashletId);
}
