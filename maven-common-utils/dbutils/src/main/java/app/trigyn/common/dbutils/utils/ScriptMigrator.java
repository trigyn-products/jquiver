package app.trigyn.common.dbutils.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;

public class ScriptMigrator {

    private ScriptMigrator() {

    }

    public static void runScripts(DataSource dataSource, String filePath) throws SQLException {
        ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());
        InputStream inputStream = ScriptMigrator.class.getResourceAsStream(filePath);
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        scriptRunner.runScript(reader);
    }

}