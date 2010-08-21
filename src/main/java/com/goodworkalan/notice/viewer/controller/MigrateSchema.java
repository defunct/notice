package com.goodworkalan.notice.viewer.controller;

import java.sql.SQLException;

import com.goodworkalan.addendum.Addenda;
import com.goodworkalan.addendum.connector.NamingConnector;
import com.goodworkalan.addendum.jpa.CreateEntity;
import com.goodworkalan.notice.viewer.model.Column;
import com.goodworkalan.notice.viewer.model.Filter;
import com.goodworkalan.notice.viewer.model.Grid;
import com.goodworkalan.notice.viewer.model.Log;
import com.goodworkalan.paste.controller.Abnormality;

public class MigrateSchema implements Runnable {
    /** The naming entry for the data source name. */
    public final static String DATA_SOURCE_NAME = "java:comp/env/notice/viewer/datasource";

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
        addenda
            .addendum()
                .create(new CreateEntity(Log.class)).end()
                .create(new CreateEntity(Grid.class)).end()
                .create(new CreateEntity(Column.class)).end()
                .commit();
        addenda
            .addendum()
                .create(new CreateEntity(Filter.class)).end()
                .commit();
        addenda.amend();
    }
}
