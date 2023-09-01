package com.trigyn.jws.dashboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@Service
@Transactional
public class DashboardService {

	private final static Logger logger = LogManager.getLogger(DashboardService.class);

	@Autowired
	private DashboardDaoImpl					dashboardDao						= null;

	@Autowired
	private IDashletRepository					iDashletRepository					= null;

	@Autowired
	private IDashboardLookupCategoryRepository	iDashboardLookupCategoryRepository	= null;

	@Autowired
	private DBTemplatingService					templatingService					= null;

	@Autowired
	private DashboardDaoImpl dashboardDAO = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private DashletService dashletService = null;
	
	

	public void saveDashboardRoleAssociation(DashboardRoleAssociation dashboardRoleAssociation) throws Exception {
		dashboardDao.saveDashboardRoleAssociation(dashboardRoleAssociation);
	}

	public List<Dashboard> findDashboardsByContextId(String contextName, List<String> userRoles) throws Exception {
		String			userId				= Constants.ANON_USER_STR;
		List<Object[]>	dashboardObjArray	= dashboardDao.findDashboardsByContextId(userRoles, userId);
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

	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.DASHBOARD)
	public String getDashletUI(String a_userId, boolean a_isContentOnly, String dashboardId, List<String> roleIdList,
			Boolean isLayoutRequired, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception, CustomStopException {
		try {
			List<String> dashletUIs = new ArrayList<String>();
			Map<String, Object> mapOfVariables = new HashMap<String, Object>();

			Dashboard dashboard = dashboardDAO.findDashboardByDashboardId(dashboardId);
			List<Dashlet> dashlets = dashletService.getDashlets(roleIdList, dashboardId,httpServletRequest, httpServletResponse);
			for (Dashlet l_dashlet : dashlets) {
				String dashletUI = dashletService.getDashletUIString(l_dashlet, a_userId, a_isContentOnly, dashboardId, null,
						httpServletRequest, httpServletResponse);
				if(null !=dashletUI) {
					dashletUIs.add(dashletUI);
				}
			}
			if(dashletUIs.isEmpty()==false) {
				mapOfVariables.put("dashletUIs", dashletUIs);
			}
			mapOfVariables.put("dashboardId", dashboardId);
			mapOfVariables.put("dashboardName", dashboard.getDashboardName());
			mapOfVariables.put("dashboard", dashboard);
			if (dashletUIs.isEmpty() == false) {
				if (isLayoutRequired != null && isLayoutRequired == false) {
					return menuService.getTemplateWithoutLayout("dashlets", mapOfVariables);
				}
				return menuService.getTemplateWithSiteLayout("dashlets", mapOfVariables);
			} 
		
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getDashletUI.", custStopException);
			throw custStopException;
		} 
		return null;
	}

	public Integer getContextBasedPermissions(String contextName) throws Exception {
		return dashboardDao.getContextBasedPermissions(contextName);
	}

	public void removeDashletFromDashboard(String dashletId, String dashboardId) throws Exception {
		dashboardDao.removeDashletFromDashboard(dashletId, dashboardId);
	}

	public List<DashboardDashletVO> getDashletsByContextId(String dashboardId) throws Exception {
		return iDashletRepository.findDashletByContextId(dashboardId);
	}

	public List<DashboardLookupCategoryVO> getDashboardLookupDetails(String categoryName) throws Exception {
		return iDashboardLookupCategoryRepository.findDashboardLookupCategoryByName(categoryName);
	}

	public List<DashboardLookupCategoryVO> getDashboardLookupDetailsById(List<DashletProperties> properties) throws Exception {
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
