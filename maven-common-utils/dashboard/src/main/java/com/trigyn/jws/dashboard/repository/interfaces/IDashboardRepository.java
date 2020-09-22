package com.trigyn.jws.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.Dashboard;

@Repository
public interface IDashboardRepository extends JpaRepository<Dashboard, String>{

}
