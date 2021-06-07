
package com.trigyn.jws.applicationmetrics.service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.GcInfo;

public class GCInformation {

	private final static Logger						logger			= LogManager.getLogger(GCInformation.class);

	private static final String						GC_BEAN_NAME	= "java.lang:name=G1 Old Generation,type=GarbageCollector";

	private static volatile GarbageCollectorMXBean	gcMBean;

	public GCInformation() {
	}

	private static void initGCMBean() {
		if (gcMBean == null) {
			synchronized (GCInformation.class) {
				if (gcMBean == null) {
					gcMBean = getGCMBean();
				}
			}
		}
	}

	private static GarbageCollectorMXBean getGCMBean() {
		try {
			MBeanServer				server	= ManagementFactory.getPlatformMBeanServer();
			GarbageCollectorMXBean	bean	= ManagementFactory.newPlatformMXBeanProxy(server, GC_BEAN_NAME, GarbageCollectorMXBean.class);
			return bean;
		} catch (RuntimeException a_re) {
			logger.error(a_re);
			throw a_re;
		} catch (Exception a_exp) {
			logger.error(a_exp);
			throw new RuntimeException(a_exp);
		}
	}

	static Map<String, Object> printGCInfo() {
		// initialize GC MBean
		initGCMBean();
		Map<String, Object> gcMetricMap = new HashMap<String, Object>();
		try {
			GcInfo						gci				= gcMBean.getLastGcInfo();
			long						id				= gci.getId();
			long						startTime		= gci.getStartTime();
			long						endTime			= gci.getEndTime();
			long						duration		= gci.getDuration();
			// if (startTime == endTime) {
			// return false; // no gc
			// }
			// System.out.println("GC ID: "+id);
			// System.out.println("Start Time: "+startTime);
			// System.out.println("End Time: "+endTime);
			// System.out.println("Duration: "+duration);
			Map<String, MemoryUsage>	mapBefore		= gci.getMemoryUsageBeforeGc();
			Map<String, MemoryUsage>	mapAfter		= gci.getMemoryUsageAfterGc();

			long						gcLiveDataSize	= mapAfter.get("G1 Old Gen").getUsed();
			long						gcMaxDataSize	= mapAfter.get("G1 Old Gen").getMax();

			gcMetricMap.put("GC Live Data Size ", gcLiveDataSize);
			gcMetricMap.put("GC Max Data Size ", gcMaxDataSize);

			// System.out.println();
			// System.out.println("Before GC Memory Usage Details....");
			Set			memType	= mapBefore.keySet();
			Iterator	it		= memType.iterator();
			while (it.hasNext()) {
				String		type	= (String) it.next();
				// System.out.println(type);
				MemoryUsage	mu1		= (MemoryUsage) mapBefore.get(type);
				// System.out.print("Initial Size: "+mu1.getInit());
				// System.out.print(" Used: "+ mu1.getUsed());
				// System.out.print(" Max: "+mu1.getMax());
				// System.out.print(" Committed: "+mu1.getCommitted());
				// System.out.println(" ");
			}
			// System.out.println();
			// System.out.println("After GC Memory Usage Details....");
			memType	= mapAfter.keySet();
			it		= memType.iterator();
			while (it.hasNext()) {
				String		type	= (String) it.next();
				// System.out.println(type);
				MemoryUsage	mu2		= (MemoryUsage) mapAfter.get(type);
				// System.out.print("Initial Size: "+mu2.getInit());
				// System.out.print(" Used: "+ mu2.getUsed());
				// System.out.print(" Max: "+mu2.getMax());
				// System.out.print(" Committed: "+mu2.getCommitted());
				// System.out.println(" ");
			}
		} catch (RuntimeException a_re) {
			logger.error(a_re);
			throw a_re;
		} catch (Exception a_exp) {
			logger.error(a_exp);
			throw new RuntimeException(a_exp);
		}
		return gcMetricMap;
	}

}
