package app.trigyn.common.dashboard.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.trigyn.common.dashboard.dao.DashboardDaoImpl;
import app.trigyn.common.dashboard.entities.Dashboard;
import app.trigyn.common.dashboard.entities.DashboardRoleAssociation;
import app.trigyn.common.dashboard.entities.Dashlet;
import app.trigyn.common.dashboard.repository.interfaces.IDashboardLookupCategoryRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletRepository;
import app.trigyn.common.dashboard.utility.Constants;
import app.trigyn.common.dashboard.vo.DashboardDashletVO;
import app.trigyn.common.dashboard.vo.DashboardLookupCategoryVO;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class DashboardService {

	@Autowired
	private DashboardDaoImpl dashboardDao 											= null;

	@Autowired
	private IDashletRepository iDashletRepository 									= null;
	
	@Autowired
	private IDashboardLookupCategoryRepository iDashboardLookupCategoryRepository	= null;
	
	
	/**
	 * @param dashboardRoleAssociation
	 * @throws Exception
	 */
	public void saveDashboardRoleAssociation(DashboardRoleAssociation dashboardRoleAssociation) throws Exception {
		dashboardDao.saveDashboardRoleAssociation(dashboardRoleAssociation);
	}

	/**
	 * @param contextName
	 * @param userRoles
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<Dashboard> findDashboardsByContextId(String contextName, List<String> userRoles) throws Exception {
		String userId = Constants.ANON_USER_STR;
		String contextId = dashboardDao.getContextNameById(contextName);
		List<Object[]> dashboardObjArray = dashboardDao.findDashboardsByContextId(contextId, userRoles, userId);
		List<Dashboard> dashboards = new LinkedList<>();
		for (Object[] dashboardObjectArray : dashboardObjArray) {
			Dashboard dashboard = (Dashboard) dashboardObjectArray[0];
			dashboards.add(dashboard);
		}
		return dashboards;
	}

	/**
	 * @param dashboardId
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<Dashlet> loadDashboardDahlets(String dashboardId) throws Exception {
		List<Object[]> dashletsDetails = dashboardDao.loadDashboardDashlets(dashboardId);
		List<Dashlet> dashletList = new ArrayList<>();
		for (Object[] dashletObject : dashletsDetails) {
			Dashlet dashlet = (Dashlet) dashletObject[1];
			dashletList.add(dashlet);
		}
		return dashletList;
	}

	/**
	 * @param contextName
	 * @return {@link String}
	 * @throws Exception
	 */
	public String getContextNameById(String contextName) throws Exception {
		return dashboardDao.getContextNameById(contextName);
	}

	/**
	 * @param contextName
	 * @return {@link Integer}
	 * @throws Exception
	 */
	public Integer getContextBasedPermissions(String contextName) throws Exception {
		return dashboardDao.getContextBasedPermissions(contextName);
	}

	/**
	 * @param dashletId
	 * @param dashboardId
	 * @throws Exception
	 */
	public void removeDashletFromDashboard(String dashletId, String dashboardId) throws Exception {
		dashboardDao.removeDashletFromDashboard(dashletId, dashboardId);
	}

	/**
	 * @param contextId
	 * @param dashboardId
	 * @return
	 * @throws Exception
	 */
	public List<DashboardDashletVO> getDashletsByContextId(String contextId, String dashboardId) throws Exception {
		return iDashletRepository.findDashletByContextId(contextId, dashboardId);
	}

	
	/**
	 * @param categoryName
	 * @return
	 * @throws Exception
	 */
	public List<DashboardLookupCategoryVO> getDashboardLookupDetails(String categoryName) throws Exception {
		return iDashboardLookupCategoryRepository.findDashboardLookupCategoryByName(categoryName);
	}

	public List<TemplateVO> getComponentTemplates() {
		return null;
	}
}
