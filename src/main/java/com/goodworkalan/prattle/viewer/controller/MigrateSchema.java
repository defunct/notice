package com.goodworkalan.prattle.viewer.controller;

import java.sql.SQLException;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.NamingConnector;
import com.goodworkalan.paste.stop.Abnormality;

public class MigrateSchema implements Runnable {
    /** The naming entry for the data source name. */
    public final static String DATA_SOURCE_NAME = "java:comp/env/prattle/viewer/datasource";

    public void run() {
        try {
            migrate();
        } catch (SQLException e) {
            throw new Abnormality(500, e);
        }
    }

    /**
     * Perform any database migrations.
     * 
     * @throws SQLException
     *             For any SQL exception.
     */
    private void migrate() throws SQLException {
        Addenda addenda = new Addenda(new NamingConnector(DATA_SOURCE_NAME));
        addenda.amend();
    }
}
