package org.cdbtool.cdbtool.connectors;

import org.cdbtool.cdbtool.dtos.ResultSetDto;
import org.cdbtool.cdbtool.dtos.SchemaDto;
import org.cdbtool.cdbtool.dtos.TableDto;
import org.cdbtool.cdbtool.exceptions.ConnectorException;

import java.sql.SQLException;
import java.util.List;

public interface Connector extends AutoCloseable {

    ResultSetDto execute(String sql) throws SQLException, ConnectorException;
    void execute(String sql, ResultCallback resultCallback) throws SQLException, ConnectorException;
    List<SchemaDto> getSchemas() throws SQLException, ConnectorException;

    List<TableDto> getTables(String schemaName) throws SQLException, ConnectorException;
}
