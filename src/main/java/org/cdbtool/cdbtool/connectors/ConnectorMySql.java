package org.cdbtool.cdbtool.connectors;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;

@Log4j2
public class ConnectorMySql extends AbstractConnector {

    public static final String SCHEMA_SQL_QUERY = "SELECT schema_name FROM information_schema.schemata ORDER BY schema_name";
    public static final String SCHEMA_FIELD_NAME = "schema_name";
    public static final String TABLES_SQL_QUERY = "SELECT TABLE_NAME " +
            "FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = '%s';";
    public static final String TABLE_FIELD_NAME = "table_name";

    public ConnectorMySql(Connection connection) {
        super(connection);
    }

    @Override
    public String getSchemaSqlQuery() {
        return SCHEMA_SQL_QUERY;
    }

    @Override
    public String getSchemaFieldName() {
        return SCHEMA_FIELD_NAME;
    }

    @Override
    public String getTableSqlQuery() {
        return TABLES_SQL_QUERY;
    }

    @Override
    public String getTableFieldName() {
        return TABLE_FIELD_NAME;
    }
}
