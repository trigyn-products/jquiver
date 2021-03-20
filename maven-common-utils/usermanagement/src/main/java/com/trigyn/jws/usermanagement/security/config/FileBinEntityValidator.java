package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class FileBinEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager
			.getLogger(FileBinEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	private String								primaryKeyName					= "fileUploadId";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside FileBinEntityValidator.hasAccessToEntity(fileUploadId - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNames, a_joinPoint);

		String	fileUploadId	= reqObject.getParameter(primaryKeyName);
		String	fileBinId		= reqObject.getParameter("fileBinId");

		boolean	hasAccess		= false;
		if (StringUtils.isBlank(fileBinId) == true) {
			if (StringUtils.isBlank(fileUploadId) == true) {
				fileUploadId	= reqObject.getRequestURI();
				fileUploadId	= fileUploadId.replaceFirst("/cf/files/", "");
			}
			fileBinId = authorizedValidatorDAO.getFileBinIdByFileUploadId(fileUploadId);
		}
		Long count = authorizedValidatorDAO.hasAccessToEntity(Constants.Modules.FILEBIN.getModuleName(), fileBinId,
				roleNames);
		if (count != null && count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside FileBinEntityValidator.getEntityName(fileUploadId - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject, reqObject.getParameter(primaryKeyName), roleNameList, a_joinPoint);

		String	fileUploadId	= reqObject.getParameter(primaryKeyName);
		String	fileBinId		= reqObject.getParameter("fileBinId");

		if (StringUtils.isBlank(fileBinId) == true) {
			if (StringUtils.isBlank(fileUploadId) == true) {
				fileUploadId	= reqObject.getRequestURI();
				fileUploadId	= fileUploadId.replaceFirst("/cf/files/", "");
			}
			fileBinId = authorizedValidatorDAO.getFileBinIdByFileUploadId(fileUploadId);
		}
		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId(Constants.Modules.FILEBIN.getModuleName(),
				fileBinId);

	}

}
