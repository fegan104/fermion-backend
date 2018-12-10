package com.fermion.data.database;

import com.fermion.util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    // These are to be configured and NEVER stored in the code.
    // once you retrieve this code, you can update
    public final static String rdsMySqlDatabaseUrl = "fermioncalendardb.cfxxtkssr9qe.us-east-2.rds.amazonaws.com";
    public final static String dbUsername = "fermion2018";
    public final static String dbPassword = "fermion2018";

    public final static String jdbcTag = "jdbc:mysql://";
    public final static String rdsMySqlDatabasePort = "3306";
    public final static String multiQueries = "?allowMultiQueries=true&useSSL=false";

    public final static String dbName = "innodb";    // default created from MySQL WorkBench

    // pooled across all usages.
    static Connection conn;

    /**
     * Singleton access to DB connection to share resources effectively across multiple accesses.
     */
    static Connection connect() throws Exception {
        if (conn != null) return conn;

        Logger.log("start connecting...");
        Class.forName("com.mysql.cj.jdbc.Driver");

        conn = DriverManager.getConnection(
                jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + dbName + multiQueries,
                dbUsername,
                dbPassword);
        Logger.log("Database has been connected successfully.");
        return conn;
    }
}
