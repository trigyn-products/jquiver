package com.trigyn.jws.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.dao.QueryStore;
import com.trigyn.jws.dashboard.entities.DashletRoleAssociation;
import com.trigyn.jws.dashboard.entities.DashletRoleAssociationPK;
import com.trigyn.jws.dashboard.vo.DashletRoleAssociationVO;

@Repository
public interface IDashletRoleAssociationRepository
		extends JpaRepository<DashletRoleAssociation, DashletRoleAssociationPK> {

	@Query(QueryStore.JPQ_QUERY_TO_GET_DASHLET_ROLES_ASSOCIATION_BY_ID)
	List<DashletRoleAssociationVO> getDashletRolesByDashletId(String dashletId);
}
