package org.cdbtool.cdbtool.connectors;

import org.cdbtool.cdbtool.exceptions.ConnectorException;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultCallback {

    void handle(ResultSet resultSet) throws SQLException, ConnectorException;
}
