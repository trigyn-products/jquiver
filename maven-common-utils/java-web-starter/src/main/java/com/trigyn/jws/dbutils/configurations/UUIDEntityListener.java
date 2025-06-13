/**
 * 
 */
package com.trigyn.jws.dbutils.configurations;

import java.util.UUID;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.DatasourceLookUp;
import com.trigyn.jws.dbutils.entities.JwsLookup;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.FileUploadTemp;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JqSchedulerLog;
import com.trigyn.jws.dynarest.entities.JwsClusterState;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.quartz.models.entities.MailSchedule;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.security.config.SaltDetails;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * 
 */
public class UUIDEntityListener {

	@PrePersist
    @PreUpdate
    void setIdIfMissing(Object entity) {
		String id = UUID.randomUUID().toString();
        if (entity instanceof Dashboard) {
        	Dashboard e = (Dashboard) entity;
            if (e.getDashboardId() == null) {
                e.setDashboardId(id);
            }
        } else  if (entity instanceof Dashlet) {
        	Dashlet e = (Dashlet) entity;
            if (e.getDashletId() == null) {
                e.setDashletId(id);
            }
        } else  if (entity instanceof DashletProperties) {
        	DashletProperties e = (DashletProperties) entity;
            if (e.getPropertyId() == null) {
                e.setPropertyId(id);
            }
        } else  if (entity instanceof AdditionalDatasource) {
        	AdditionalDatasource e = (AdditionalDatasource) entity;
            if (e.getAdditionalDatasourceId() == null) {
                e.setAdditionalDatasourceId(id);
            }
        } else  if (entity instanceof ModuleListing) {
        	ModuleListing e = (ModuleListing) entity;
            if (e.getModuleId() == null) {
                e.setModuleId(id);
            }
        } else  if (entity instanceof PropertyMaster) {
        	PropertyMaster e = (PropertyMaster) entity;
            if (e.getPropertyMasterId() == null) {
                e.setPropertyMasterId(id);
            }
        } else  if (entity instanceof DynamicForm) {
        	DynamicForm e = (DynamicForm) entity;
            if (e.getFormId() == null) {
                e.setFormId(id);
            }
        } else  if (entity instanceof DynamicFormSaveQuery) {
        	DynamicFormSaveQuery e = (DynamicFormSaveQuery) entity;
            if (e.getDynamicFormQueryId() == null) {
                e.setDynamicFormQueryId(id);
            }
        } else  if (entity instanceof ManualEntryDetails) {
        	ManualEntryDetails e = (ManualEntryDetails) entity;
            if (e.getManualEntryId() == null) {
                e.setManualEntryId(id);
            }
        } else  if (entity instanceof ManualType) {
        	ManualType e = (ManualType) entity;
            if (e.getManualId() == null) {
                e.setManualId(id);
            }
        } else  if (entity instanceof FileUpload) {
        	FileUpload e = (FileUpload) entity;
            if (e.getFileUploadId() == null) {
                e.setFileUploadId(id);
            }
        } else  if (entity instanceof FileUploadConfig) {
        	FileUploadConfig e = (FileUploadConfig) entity;
            if (e.getFileBinId() == null) {
                e.setFileBinId(id);
            }
        } else  if (entity instanceof FileUploadConfig) {
        	FileUploadConfig e = (FileUploadConfig) entity;
            if (e.getFileBinId() == null) {
                e.setFileBinId(id);
            }
        } else  if (entity instanceof FileUploadTemp) {
        	FileUploadTemp e = (FileUploadTemp) entity;
            if (e.getFileUploadTempId() == null) {
                e.setFileUploadTempId(id);
            }
        } else  if (entity instanceof JqScheduler) {
        	JqScheduler e = (JqScheduler) entity;
            if (e.getSchedulerId() == null) {
                e.setSchedulerId(id);
            }
        } else  if (entity instanceof JqSchedulerLog) {
        	JqSchedulerLog e = (JqSchedulerLog) entity;
            if (e.getSchedulerLogId() == null) {
                e.setSchedulerLogId(id);
            }
        } else  if (entity instanceof JwsDynamicRestDetail) {
        	JwsDynamicRestDetail e = (JwsDynamicRestDetail) entity;
            if (e.getJwsDynamicRestId() == null) {
                e.setJwsDynamicRestId(id);
            }
        } else  if (entity instanceof MailSchedule) {
        	MailSchedule e = (MailSchedule) entity;
            if (e.getMailScheduleId() == null) {
                e.setMailScheduleId(id);
            }
        } else  if (entity instanceof ScriptLibraryDetails) {
        	ScriptLibraryDetails e = (ScriptLibraryDetails) entity;
            if (e.getScriptLibId() == null) {
                e.setScriptLibId(id);
            }
        } else  if (entity instanceof TemplateMaster) {
        	TemplateMaster e = (TemplateMaster) entity;
            if (e.getTemplateId() == null) {
                e.setTemplateId(id);
            }
        } else  if (entity instanceof JwsEntityRoleAssociation) {
        	JwsEntityRoleAssociation e = (JwsEntityRoleAssociation) entity;
            if (e.getEntityRoleId() == null) {
                e.setEntityRoleId(id);
            }
        } else  if (entity instanceof JwsMasterModules) {
        	JwsMasterModules e = (JwsMasterModules) entity;
            if (e.getModuleId() == null) {
                e.setModuleId(id);
            }
        } else  if (entity instanceof JwsRole) {
        	JwsRole e = (JwsRole) entity;
            if (e.getRoleId() == null) {
                e.setRoleId(id);
            }
        } else  if (entity instanceof JwsUser) {
        	JwsUser e = (JwsUser) entity;
            if (e.getUserId() == null) {
                e.setUserId(id);
            }
        } else  if (entity instanceof GenericUserNotification) {
        	GenericUserNotification e = (GenericUserNotification) entity;
            if (e.getNotificationId() == null) {
                e.setNotificationId(id);
            }
        } else  if (entity instanceof DatasourceLookUp) {
        	DatasourceLookUp e = (DatasourceLookUp) entity;
            if (e.getDatasourceLookupId() == null) {
                e.setDatasourceLookupId(id);
            }
        } else  if (entity instanceof JwsClusterState) {
        	JwsClusterState e = (JwsClusterState) entity;
            if (e.getInstanceId() == null) {
                e.setInstanceId(id);
            }
        } else  if (entity instanceof JwsRoleMasterModulesAssociation) {
        	JwsRoleMasterModulesAssociation e = (JwsRoleMasterModulesAssociation) entity;
            if (e.getRoleModuleId() == null) {
                e.setRoleModuleId(id);
            }
        } else  if (entity instanceof ScriptLibraryConnection) {
        	ScriptLibraryConnection e = (ScriptLibraryConnection) entity;
            if (e.getScriptlibconnId() == null) {
                e.setScriptlibconnId(id);
            }
        } else  if (entity instanceof JwsLookup) {
        	JwsLookup e = (JwsLookup) entity;
            if (e.getLookupId() == null) {
                e.setLookupId(id);
            }
        } else  if (entity instanceof JwsModuleVersion) {
        	JwsModuleVersion e = (JwsModuleVersion) entity;
            if (e.getModuleVersionId() == null) {
                e.setModuleVersionId(id);
            }
        } else  if (entity instanceof JwsConfirmationToken) {
        	JwsConfirmationToken e = (JwsConfirmationToken) entity;
            if (e.getTokenId() == null) {
                e.setTokenId(id);
            }
        } else  if (entity instanceof JwsUserRoleAssociation) {
        	JwsUserRoleAssociation e = (JwsUserRoleAssociation) entity;
            if (e.getUserRoleId() == null) {
                e.setUserRoleId(id);
            }
        } else  if (entity instanceof SaltDetails) {
        	SaltDetails e = (SaltDetails) entity;
            if (e.getRequestId() == null) {
                e.setRequestId(id);
            }
        } else  if (entity instanceof CaptchaDetails) {
        	CaptchaDetails e = (CaptchaDetails) entity;
            if (e.getRequestId() == null) {
                e.setRequestId(id);
            }
        }
    }
}
