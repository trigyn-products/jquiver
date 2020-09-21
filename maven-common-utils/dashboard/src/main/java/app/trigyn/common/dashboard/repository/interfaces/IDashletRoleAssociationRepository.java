package app.trigyn.common.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.dao.QueryStore;
import app.trigyn.common.dashboard.entities.DashletRoleAssociation;
import app.trigyn.common.dashboard.entities.DashletRoleAssociationPK;
import app.trigyn.common.dashboard.vo.DashletRoleAssociationVO;

@Repository
public interface IDashletRoleAssociationRepository extends JpaRepository<DashletRoleAssociation, DashletRoleAssociationPK>{

	@Query(QueryStore.JPQ_QUERY_TO_GET_DASHLET_ROLES_ASSOCIATION_BY_ID)
	List<DashletRoleAssociationVO> getDashletRolesByDashletId(String dashletId);
}
