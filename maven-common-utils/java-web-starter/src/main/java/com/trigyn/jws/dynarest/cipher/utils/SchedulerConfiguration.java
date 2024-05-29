package com.trigyn.jws.dynarest.cipher.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.itextpdf.io.IOException;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.dao.JwsClusterStateDAO;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsClusterState;
import com.trigyn.jws.dynarest.repository.JQClusterStateRepository;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.quartz.service.impl.JwsQuartzJobService;
import com.trigyn.jws.quartz.util.InstanceVersion;
import com.trigyn.jws.usermanagement.utils.Constants;

@Configuration
public class SchedulerConfiguration {
	
	private final static Logger logger = LogManager.getLogger(SchedulerConfiguration.class);

	@Autowired
	private JqschedulerRepository		jqschedulerRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService	= null;

	@Autowired
	private ServletContext				servletContext			= null;

	@Autowired
	private JQClusterStateRepository 	clusterStateRepository  = null;
	
	@Autowired
	private JwsClusterStateDAO 			clusterStateDAO 		= null;
	
	@Autowired
	private JwsQuartzJobService			jobService				= null;
	
	@Autowired
	private Environment 				environment				= null;
	
	@PostConstruct
	public void initialiseScheduler() {

		try {

			List<JqScheduler> schedulers = jqschedulerRepository.retrieveSchedulers(1);

			for (JqScheduler jwsScheduler : schedulers) {
				try {

					String		cronExpression			= jwsScheduler.getCronScheduler();
					String		schedulerId				= jwsScheduler.getSchedulerId();

					String		schedulerUrlProperty	= propertyMasterService
							.findPropertyMasterValue("scheduler-url");
					String		baseURL					= jobService.getBaseUrl();

					JobDataMap	map						= new JobDataMap();
					map.put("baseURL", baseURL);
					map.put("schedulerId", schedulerId);
					map.put("schedulerUrlProperty", schedulerUrlProperty);
					map.put("contextPath", servletContext.getContextPath());
					String jobGroup = jwsScheduler.getJwsDynamicRestId();
					boolean status = jobService.scheduleCronJob(jwsScheduler.getScheduler_name(), jobGroup, JwsSchedulerJob.class,
							new Date(), cronExpression, map);
					if (status) {
						logger.info("Scheduler executed successfully.");
					} else {
						logger.info("Could not start job ");
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
			if (Boolean.valueOf(isClustered()) != null && Boolean.valueOf(isClustered()) == false && Boolean.valueOf(isClustered())) {
				updateClusterState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateClusterState() throws Exception {
		String strClustereActiveTime = propertyMasterService.findPropertyMasterValue("clusterActiveTime");
		Integer clustereActiveTime = (null == strClustereActiveTime || strClustereActiveTime.isEmpty()) ? 30 : StringUtils.isNumeric(strClustereActiveTime) ? Integer.parseInt(strClustereActiveTime) : 30;
		Runnable clusterExecutor = new Runnable() {
			public void run() {
				
				String instanceId = null;
				try {
					
					instanceId = new InstanceVersion().generateInstanceId();
					if(instanceId !=null) {
						JwsClusterState aCluster = clusterStateRepository.retrieveClusterByClusterState(Constants.ISACTIVE, instanceId);
						
						if(aCluster !=null) {
							Date currentTime = java.util.Calendar.getInstance().getTime();
							long diffInSeconds = TimeUnit.MILLISECONDS
									.toSeconds(currentTime.getTime() - aCluster.getUpdatedOn().getTime());
							if(diffInSeconds > clustereActiveTime) {
								aCluster.setIsSchedular(null);
							} else if (diffInSeconds <= clustereActiveTime) {
								aCluster.setIsSchedular(Constants.ISACTIVE);
							}
							aCluster.setUpdatedOn(new Date());
							clusterStateDAO.updateClusterState(aCluster);
						}
						
						JwsClusterState clusterState = clusterStateRepository.findById(instanceId).orElse(null);
						aCluster = clusterStateRepository.retrieveClusterByClusterState(Constants.ISACTIVE, instanceId);
						List<JwsClusterState> clusterStates = clusterStateRepository.retrieveAllClusterByActiveState(Constants.ISACTIVE);
						for (JwsClusterState cst : clusterStates) {
							if(cst.getIsSchedular()!=null && Constants.ISACTIVE.compareTo(cst.getIsSchedular()) == 0) {
								boolean isClusterUpdated = false;
								Date currentTime = java.util.Calendar.getInstance().getTime();
								long diffInSeconds = TimeUnit.MILLISECONDS
										.toSeconds(currentTime.getTime() - cst.getUpdatedOn().getTime());
								if(diffInSeconds > clustereActiveTime) {
									cst.setIsSchedular(null);
									clusterStateDAO.updateClusterState(cst);
									isClusterUpdated  = true;
								}
								if (clusterState != null && isClusterUpdated) {
									clusterState.setIsSchedular(Constants.ISACTIVE);
									clusterState.setUpdatedOn(new Date());
									clusterStateDAO.updateClusterState(clusterState);
								}
								break;
							}
						}
						clusterStates = clusterStateRepository.retrieveAllClusterByActiveState(Constants.ISACTIVE);
						String activeInstanceId = null;
						for (JwsClusterState cst : clusterStates) {
							if(cst.getIsSchedular()!=null && Constants.ISACTIVE.compareTo(cst.getIsSchedular()) == 0) {
								activeInstanceId = cst.getInstanceId();
								break;
							}
						}
						
						if (null == clusterState) {
							clusterState = new JwsClusterState();
							clusterState.setInstanceId(instanceId);
							clusterState.setIsActive(Constants.ISACTIVE);
							if (null == activeInstanceId) {
								clusterState.setIsSchedular(Constants.ISACTIVE);
							}else {
								clusterState.setIsSchedular(null);
							}
							clusterState.setUpdatedOn(new Date());
							clusterStateDAO.saveClusterState(clusterState);
						}
						clusterStates = clusterStateRepository.retrieveAllClusterByActiveState(Constants.ISACTIVE);
						if(clusterStates!=null && clusterStates.isEmpty()) {
							clusterState.setIsSchedular(Constants.ISACTIVE);
							clusterState.setUpdatedOn(new Date());
							clusterStateDAO.updateClusterState(clusterState);
						}
					}
					
				} catch (IOException exec) {
					logger.error("Error while updating cluster state : "+ ExceptionUtils.getStackTrace(exec.getCause()));
				} catch (Exception exec) {
					logger.error("Error "+ ExceptionUtils.getStackTrace(exec.getCause()));
				}
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(clusterExecutor, 0, clustereActiveTime, TimeUnit.SECONDS);
	}
	
	public String isClustered(){
	    return environment.getProperty("org.quartz.jobStore.isClustered");
	}

}
