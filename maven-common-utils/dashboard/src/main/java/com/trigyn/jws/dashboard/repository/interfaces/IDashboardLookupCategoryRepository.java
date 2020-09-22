package com.trigyn.jws.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.dao.QueryStore;
import com.trigyn.jws.dashboard.entities.DashboardLookupCategory;
import com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO;

@Repository
public interface IDashboardLookupCategoryRepository extends JpaRepository<DashboardLookupCategory, String>{

	@Query(QueryStore.JPA_QUERY_TO_GET_LOOKUP_DETAILS_BY_CATEOGRY_NAME)
	List<DashboardLookupCategoryVO> findDashboardLookupCategoryByName(String lookupCategoryName);
}
