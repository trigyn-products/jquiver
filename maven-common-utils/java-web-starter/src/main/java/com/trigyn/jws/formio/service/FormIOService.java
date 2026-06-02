package com.trigyn.jws.formio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.formio.dao.FormIODAO;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.utils.Constant;

import jakarta.transaction.Transactional;

@Service
public class FormIOService {

	private final static Logger		logger					= LoggerFactory.getLogger(FormIOService.class);

	@Autowired
	private FormIODAO				formIODAO				= null;

	@Autowired
	private UserManagementService	userManagementService	= null;

	@Autowired
	private ModuleVersionService	moduleVersionService	= null;

	@Autowired
	private ActivityLog				log						= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	@Autowired
	private DynamicFormCrudDAO		dynamicFormCrudDAO		= null;

	/**
	 * @param  formIoIds
	 * @return
	 */
	public List<FormIOVO> findFormIOByFormId(String formIoIds) {
		List<FormIOVO>	formIOVOs	= new ArrayList<>();
		List<Object[]>	objects		= formIODAO.getAllFormsByDynaFormId(formIoIds);
		if (!CollectionUtils.isEmpty(objects)) {
			for (Object[] fmios : objects) {
				FormIOVO formIOVO = new FormIOVO();
				formIOVO.setFormIoId(fmios[0].toString());
				if (fmios[1] != null) {
					formIOVO.setFormName(fmios[1].toString());
				}
				formIOVO.setFormIoJson(fmios[2].toString());
				formIOVOs.add(formIOVO);
			}
		}
		return formIOVOs;
	}

	/**
	 * @param formIo
	 * @param formIoVo
	 */
	@Transactional
	public void saveFormIOByVersion(FormIO formIo, FormIOVO formIoVo) {
		try {
			/*
			 * List<DynamicForm> dynamicForms =
			 * dynamicFormCrudDAO.getAllDynamicFormsByFormIoId(formIo.getFormIoId());
			 * dynamicForms.replaceAll(df -> { df.setFormIoId(null); return df; }); for
			 * (DynamicForm dynamicForm : dynamicForms) {
			 * dynamicFormCrudDAO.saveDynamicForm(dynamicForm); }
			 */
			formIODAO.saveOrUpdateFormIo(formIo);
			// saveFormIoRoles(formIo);
		//	formIoVo = FormIOUtils.convertToFormIoVO(formIo);
			moduleVersionService.saveModuleVersion(formIoVo, null, formIo.getFormIoId(), "FormIO",
					Constant.REVISION_SOURCE_VERSION_TYPE);
			logFormIoActivity(formIo);
		} catch (Exception exec) {
			logger.error("Error occured while saving Form IO details : ", exec);
			throw new CustomStopException("Error occured while saving Form IO details : " + exec);
		}

	}

	public void saveFormIoRoles(FormIO formIo) {
		// TODO :: Logic to Get the RoleIds.
		List<String>	roleIds		= new ArrayList<>();
		JwsEntityRoleVO	entityRoles	= new JwsEntityRoleVO();
		entityRoles.setEntityId(formIo.getFormIoId());
		entityRoles.setEntityName(formIo.getFormName());
		entityRoles.setRoleIds(roleIds);
		entityRoles.setModuleId(formIo.getFormIoId());
		userManagementService.deleteAndSaveEntityRole(entityRoles);
	}

	private void logFormIoActivity(FormIO formIo) throws Exception {
		String				action				= formIo.getFormIoId() != null ? Constants.Action.EDIT.getAction()
				: Constants.Action.ADD.getAction();
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();

		Date				activityTimestamp	= new Date();
		if (formIo.getFormIoType() == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("action", action);
		requestParams.put("entityName", formIo.getFormName());
		requestParams.put("masterModuleType", "FormIO");
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "Form IO Versioning Update.");
		requestParams.put("date", activityTimestamp.toString());
		log.activitylog(requestParams);
	}

}
