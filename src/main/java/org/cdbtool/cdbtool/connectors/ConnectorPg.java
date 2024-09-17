package org.cdbtool.cdbtool.connectors;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;

@Log4j2
public class ConnectorPg extends AbstractConnector {

    public static final String SCHEMA_SQL_QUERY = "SELECT schema_name FROM information_schema.schemata ORDER BY schema_name";
    public static final String SCHEMA_FIELD_NAME = "schema_name";
    public static final String TABLES_SQL_QUERY = "SELECT pgc.relname " +
            "FROM pg_catalog.pg_class pgc " +
            "LEFT JOIN pg_catalog.pg_namespace pgn ON pgn.oid = pgc.relnamespace " +
            "WHERE pgc.relkind IN ('r','v','m','f','p') " +
            "AND pgn.nspname = '%s' " +
            "AND pgn.nspname <> 'pg_catalog' " +
            "AND pgn.nspname <> 'information_schema' " +
            "AND pgn.nspname !~ '^pg_toast' " +
            "ORDER BY pgc.relname";
    public static final String TABLE_FIELD_NAME = "relname";

    public ConnectorPg(Connection connection) {
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
