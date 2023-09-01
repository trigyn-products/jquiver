package com.trigyn.jws.quartz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

/**
 * @author aNIRUDDHA
 * @since  10-Jun-2023
 */

public class InstanceVersion implements InstanceIdGenerator {

	private final static Logger LOGGER = LogManager.getLogger(InstanceVersion.class);

	@Override
	public String generateInstanceId() throws SchedulerException {

		String				tmpdir				= System.getProperty("user.dir");
		Map<String, Object>	instanceInformation	= new HashMap<String, Object>();
		String				instanceId			= null;

		File				instanceIdFile		= new File(tmpdir + File.separatorChar + "instanceId.txt");
		try {
			if (instanceIdFile.exists() == false || instanceIdFile.length() == 0) {

				if (instanceIdFile.createNewFile() == false) {
					LOGGER.info("can't create instance id file. Exit application");
					System.exit(-2);
				}
				// System.out.println("Instance file created");
				instanceInformation.put("instanceID", UUID.randomUUID());
				instanceId = ((UUID) instanceInformation.get("instanceID")).toString();

				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(instanceIdFile));
				out.writeObject(instanceInformation);
				out.close();

			} else {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(instanceIdFile));
				instanceInformation	= (HashMap<String, Object>) in.readObject();
				instanceId			= ((UUID) instanceInformation.get("instanceID")).toString();
			}
			// System.out.println("Instance ID : " + instanceId);
		} catch (IOException exec) {
			LOGGER.error("Error while updating cluster state : " + ExceptionUtils.getStackTrace(exec.getCause()));
		} catch (ClassNotFoundException clsExec) {
			LOGGER.error("Error while reading the file " + ExceptionUtils.getStackTrace(clsExec.getCause()));
		}

		return instanceId;

	}

	/*
	 * public static void main(String[] args) throws Throwable {
	 * System.out.println(getInstanceVersion()); }
	 */

}
