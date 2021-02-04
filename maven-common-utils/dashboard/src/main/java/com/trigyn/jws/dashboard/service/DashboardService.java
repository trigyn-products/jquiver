package com.trigyn.jws.dashboard.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dashboard.dao.DashboardDaoImpl;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardLookupCategoryRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class DashboardService {

	@Autowired
	private DashboardDaoImpl					dashboardDao						= null;

	@Autowired
	private IDashletRepository					iDashletRepository					= null;

	@Autowired
	private IDashboardLookupCategoryRepository	iDashboardLookupCategoryRepository	= null;

	@Autowired
	private DBTemplatingService					templatingService					= null;

	public void saveDashboardRoleAssociation(DashboardRoleAssociation dashboardRoleAssociation) throws Exception {
		dashboardDao.saveDashboardRoleAssociation(dashboardRoleAssociation);
	}

	public List<Dashboard> findDashboardsByContextId(String contextName, List<String> userRoles) throws Exception {
		String			userId				= Constants.ANON_USER_STR;
		String			contextId			= dashboardDao.getContextNameById(contextName);
		List<Object[]>	dashboardObjArray	= dashboardDao.findDashboardsByContextId(contextId, userRoles, userId);
		List<Dashboard>	dashboards			= new LinkedList<>();
		for (Object[] dashboardObjectArray : dashboardObjArray) {
			Dashboard dashboard = (Dashboard) dashboardObjectArray[0];
			dashboards.add(dashboard);
		}
		return dashboards;
	}

	public List<Dashlet> loadDashboardDahlets(String dashboardId) throws Exception {
		List<Object[]>	dashletsDetails	= dashboardDao.loadDashboardDashlets(dashboardId);
		List<Dashlet>	dashletList		= new ArrayList<>();
		for (Object[] dashletObject : dashletsDetails) {
			Dashlet dashlet = (Dashlet) dashletObject[1];
			dashletList.add(dashlet);
		}
		return dashletList;
	}

	public String getContextNameById(String contextName) throws Exception {
		return dashboardDao.getContextNameById(contextName);
	}

	public Integer getContextBasedPermissions(String contextName) throws Exception {
		return dashboardDao.getContextBasedPermissions(contextName);
	}

	public void removeDashletFromDashboard(String dashletId, String dashboardId) throws Exception {
		dashboardDao.removeDashletFromDashboard(dashletId, dashboardId);
	}

	public List<DashboardDashletVO> getDashletsByContextId(String contextId, String dashboardId) throws Exception {
		return iDashletRepository.findDashletByContextId(contextId, dashboardId);
	}

	public List<DashboardLookupCategoryVO> getDashboardLookupDetails(String categoryName) throws Exception {
		return iDashboardLookupCategoryRepository.findDashboardLookupCategoryByName(categoryName);
	}

	public List<DashboardLookupCategoryVO> getDashboardLookupDetailsById(List<DashletProperties> properties)
			throws Exception {
		List<String> propertyTypeIdList = new ArrayList<>();
		if (CollectionUtils.isEmpty(properties) == false) {
			for (DashletProperties dashletProperties : properties) {
				propertyTypeIdList.add(dashletProperties.getType());
			}
		}
		return iDashboardLookupCategoryRepository.findDashboardLookupCategoryById(propertyTypeIdList);
	}

	public List<TemplateVO> getComponentTemplates(List<DashboardLookupCategoryVO> lookupDetails) throws Exception {
		List<TemplateVO> componentTemplateVO = new ArrayList<>();
		if (CollectionUtils.isEmpty(lookupDetails) == false) {
			for (DashboardLookupCategoryVO lookupCategoryVO : lookupDetails) {
				componentTemplateVO.add(templatingService.getTemplateByName(lookupCategoryVO.getLookupDescription()));
			}
		}
		return componentTemplateVO;
	}
}
