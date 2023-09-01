package com.trigyn.jws.dynarest.cipher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author aNIRUDDHA
 * @since 10-Jun-2023
 */

public class InstanceVersion {
	
	public static String getInstanceVersion() throws IOException, ClassNotFoundException {

		String tmpdir = System.getProperty("user.dir");
        Map<String, Object> instanceInformation = new HashMap<String, Object>();
		String instanceId = null;
		
        File instanceIdFile = new File(tmpdir + File.separatorChar + "instanceId.txt");
        if(instanceIdFile.exists() == false || instanceIdFile.length() == 0) {
        	if(instanceIdFile.createNewFile() == false) {
        		System.err.println("can't create instance id file. Exit application");
        		System.exit(-2);
        	}
        	//System.out.println("Instance file created");
            instanceInformation.put("instanceID", UUID.randomUUID());
            instanceId = ((UUID)instanceInformation.get("instanceID")).toString();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(instanceIdFile));
            out.writeObject(instanceInformation);
            out.close();
        }else {
        	ObjectInputStream in = new ObjectInputStream(new FileInputStream(instanceIdFile));
        	instanceInformation = (HashMap<String, Object>)in.readObject();
        	instanceId = ((UUID)instanceInformation.get("instanceID")).toString();
        }
        
       // System.out.println("Instance ID : " + instanceId);
        
        return instanceId;
	}
	
	/*
	public static void main(String[] args) throws Throwable {
		System.out.println(getInstanceVersion());
	}
	*/
	
}
