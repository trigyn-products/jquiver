package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.ManualType;

@Repository
public interface IManualTypeRepository extends JpaRepository<ManualType, String> {

	boolean existsByName(String name);

}
