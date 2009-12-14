package com.goodworkalan.minimal.persistence;

import java.sql.SQLException;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.NamingConnector;

/**
 * Contains a single static method that performs any database migrations.
 * 
 * @author Alan Gutierrez
 */
public class Migration {
    /** The naming entry for the data source name. */
    public final static String DATA_SOURCE_NAME = "java:comp/env/minimal/datasource";

    /**
     * Perform any database migrations.
     * 
     * @throws SQLException
     *             For any SQL exception.
     */
    public static void migrate() throws SQLException {
        Addenda addenda = new Addenda(new NamingConnector(DATA_SOURCE_NAME));
        addenda.addendum();
    }
}