package com.trigyn.jws.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociationPK;

@Repository
public interface IDashboardDashletAssociationRepository
		extends JpaRepository<DashboardDashletAssociation, DashboardDashletAssociationPK> {

}
