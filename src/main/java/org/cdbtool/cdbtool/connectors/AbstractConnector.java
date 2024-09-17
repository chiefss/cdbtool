package org.cdbtool.cdbtool.connectors;

import org.cdbtool.cdbtool.dtos.*;
import org.cdbtool.cdbtool.exceptions.ConnectorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractConnector implements Connector {

    private final Connection connection;

    AbstractConnector(Connection connection) {
        this.connection = connection;
    }

    public abstract String getSchemaSqlQuery();

    public abstract String getSchemaFieldName();

    public abstract String getTableSqlQuery();

    public abstract String getTableFieldName();

    @Override
    public List<SchemaDto> getSchemas() throws SQLException, ConnectorException {
        List<SchemaDto> schemaDtos = new ArrayList<>();
        execute(getSchemaSqlQuery(), resultSet -> {
            try {
                schemaDtos.add(new SchemaDto(resultSet.getString(getSchemaFieldName())));
            } catch (SQLException e) {
                throw new ConnectorException(e.getMessage());
            }
        });
        return schemaDtos;
    }

    @Override
    public List<TableDto> getTables(String schemaName) throws SQLException, ConnectorException {
        List<TableDto> tableDtos = new ArrayList<>();
        String sql = String.format(getTableSqlQuery(), schemaName);
        execute(sql, resultSet -> {
            try {
                tableDtos.add(new TableDto(resultSet.getString(getTableFieldName())));
            } catch (SQLException e) {
                throw new ConnectorException(e.getMessage());
            }
        });
        return tableDtos;

    }

    @Override
    public void execute(String sql, ResultCallback resultCallback) throws SQLException, ConnectorException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            if (resultSet == null) {
                return;
            }
            while (resultSet.next()) {
                resultCallback.handle(resultSet);
            }
        }
    }

    public ResultSetDto execute(String sql) throws SQLException, ConnectorException {
        ResultSetDto resultSetDto = new ResultSetDto();
        AtomicBoolean firstLoop = new AtomicBoolean(true);
        execute(sql, resultSet -> {
            if (firstLoop.get()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnType = metaData.getColumnTypeName(i);
                    resultSetDto.getSchema().getItems().add(new TableSchemaItemDto(columnName, columnType));
                }
                firstLoop.set(false);
            }
            TableRowDto tableRowDto = new TableRowDto();
            for (TableSchemaItemDto item : resultSetDto.getSchema().getItems()) {
                tableRowDto.getValue().add(resultSet.getString(item.getName()));
            }
            resultSetDto.getRows().add(tableRowDto);
        });
        return resultSetDto;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
