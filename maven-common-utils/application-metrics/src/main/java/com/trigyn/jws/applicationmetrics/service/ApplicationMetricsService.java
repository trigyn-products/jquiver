package com.trigyn.jws.applicationmetrics.service;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.sun.management.OperatingSystemMXBean;
import com.trigyn.jws.applicationmetrics.interceptor.ApplicationServiceInterceptor;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

public class ApplicationMetricsService {	

	private ApplicationContext applicationContext = null;
	
	public ApplicationMetricsService() {
		
	}
	
	public Map<String, Object> getJvmMetrics(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets, UserDetailsVO userDetails) {
		Map<String, Object> actuatorMap =  new HashMap<String, Object>();
		
		List<MemoryPoolMXBean> memoryPools = new ArrayList<MemoryPoolMXBean>(ManagementFactory.getMemoryPoolMXBeans());
		Map<String, Object> jvmMemoryPoolMap =  new HashMap<String, Object>();
		for (MemoryPoolMXBean memoryPool : memoryPools) {
			jvmMemoryPoolMap.put(memoryPool.getName(), memoryPool.getUsage());
		}
		
		actuatorMap.put("memory-pool-metric", jvmMemoryPoolMap);
		
		ThreadMXBean threadPools = ManagementFactory.getThreadMXBean();
		int totalThreadCount = threadPools.getThreadCount();
		int nbRunning = 0;
		int nbWaiting = 0;
		int nbTimedWaiting = 0;
		int nbBlocked = 0;
		int nbNew = 0;
		int nbTerminated = 0;
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			
		    if (t.getState()==Thread.State.RUNNABLE) nbRunning++;
		    if (t.getState()==Thread.State.WAITING) nbWaiting++;
		    if (t.getState()==Thread.State.TIMED_WAITING) nbTimedWaiting++;
		    if (t.getState()==Thread.State.BLOCKED) nbBlocked++;
		    if (t.getState()==Thread.State.NEW) nbNew++;
		    if (t.getState()==Thread.State.TERMINATED) nbTerminated++;
		}

		Map<String, Object> threadMetricMap =  new HashMap<String, Object>();
		
	    threadMetricMap.put("TOTAL_THREAD_COUNT",totalThreadCount);
	    threadMetricMap.put(Thread.State.RUNNABLE.name(), nbRunning);
	    threadMetricMap.put(Thread.State.WAITING.name(), nbWaiting);
	    threadMetricMap.put(Thread.State.TIMED_WAITING.name(), nbTimedWaiting);
	    threadMetricMap.put(Thread.State.BLOCKED.name(), nbBlocked);
	    threadMetricMap.put(Thread.State.NEW.name(), nbNew);
	    threadMetricMap.put(Thread.State.TERMINATED.name(), nbTerminated);
	    
	    actuatorMap.put("thread-metrics", threadMetricMap);
		
	    Map<String, Object> systemMetricMap =  new HashMap<String, Object>();
    	DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.UP);
	    
	    OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	    long upTime = runtimeMXBean.getUptime();
	    long startTime = runtimeMXBean.getStartTime();
	    double processCpuUsage = operatingSystemMXBean.getProcessCpuLoad();
	    if (processCpuUsage >= 0) {
	    	processCpuUsage = (processCpuUsage * 100);
        }
	    
	    double systemCpuUsage = operatingSystemMXBean.getSystemCpuLoad();
	    if (systemCpuUsage >= 0) {
	    	systemCpuUsage = (systemCpuUsage * 100);
        }
	    
	    double systemCpuCount = operatingSystemMXBean.getAvailableProcessors();
	    double systemLoadAverage = operatingSystemMXBean.getSystemLoadAverage();
	    if (systemLoadAverage >= 0) {
	    	systemLoadAverage = (systemLoadAverage * 100);
        }
	    systemMetricMap.put("uptime", upTime);
	    systemMetricMap.put("start-time", startTime);
	    systemMetricMap.put("process-cpu-usage", df.format(processCpuUsage)+"%");
	    systemMetricMap.put("system-cpu-usage", df.format(systemCpuUsage)+"%");
	    systemMetricMap.put("system-cpu-count", systemCpuCount);
	    systemMetricMap.put("system-load-average", df.format(systemLoadAverage)+"%");
	    
	    actuatorMap.put("system-metrics", systemMetricMap);
	    
	    
	    Map<String, Object> classLoadingGCMetricMap =  new HashMap<String, Object>();
	    ClassLoadingMXBean clMxBean = ManagementFactory.getClassLoadingMXBean();
        classLoadingGCMetricMap.put("classes-loaded ", clMxBean.getLoadedClassCount());
        classLoadingGCMetricMap.put("classes-unloaded ", clMxBean.getUnloadedClassCount());
        classLoadingGCMetricMap.put("total-classes-loaded ", clMxBean.getTotalLoadedClassCount());
        
        actuatorMap.put("classloading-metrics", classLoadingGCMetricMap);
	    
//	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//		Set names = mbs.queryNames(null, null);
//		System.out.println(names.toString().replace(", ",
//		System.getProperty("line.separator")));
	    System.gc();
	    
	    Map<String,Object> gcMetricMap = GCInformation.printGCInfo();
	    actuatorMap.put("gc-metrics", gcMetricMap);
	    actuatorMap.put("http-trace-metrics", ApplicationServiceInterceptor.getTimeInfo());
	    return actuatorMap;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
