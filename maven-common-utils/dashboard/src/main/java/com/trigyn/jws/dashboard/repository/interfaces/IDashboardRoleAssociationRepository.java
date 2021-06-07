package com.trigyn.jws.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociationPK;

@Repository
public interface IDashboardRoleAssociationRepository extends JpaRepository<DashboardRoleAssociation, DashboardRoleAssociationPK> {

}
