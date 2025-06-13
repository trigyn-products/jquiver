package com.trigyn.jws.dbutils.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;

public class ScriptMigrator {

	private ScriptMigrator() {

	}

	public static void runScripts(Connection conn, String filePath) throws SQLException {
		ScriptRunner	scriptRunner	= new ScriptRunner(conn);
		InputStream		inputStream		= ScriptMigrator.class.getResourceAsStream(filePath);
		Reader			reader			= new BufferedReader(new InputStreamReader(inputStream));
		scriptRunner.runScript(reader);
	}

}