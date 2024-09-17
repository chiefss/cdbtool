package org.cdbtool.cdbtool.connectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.enums.AdapterEnum;
import org.cdbtool.cdbtool.exceptions.AdapterEnumException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectorFactory {

    public static Connector create(ConnectionDto connectionDto, String schemaName) throws AdapterEnumException, SQLException {
        AdapterEnum adapter = AdapterEnum.findById(connectionDto.getAdapterId());
        String database = "";
        if (Strings.isNotEmpty(connectionDto.getDatabase())) {
            database = connectionDto.getDatabase();
        } else if (Strings.isNotEmpty(schemaName)) {
            database = schemaName;
        }
        String url = String.format(adapter.getUrl(), connectionDto.getHost(), connectionDto.getPort(), database);
        Connection connection = DriverManager.getConnection(url, connectionDto.getUsername(), connectionDto.getPassword());
        return switch (adapter) {
            case PG -> new ConnectorPg(connection);
            case MYSQL -> new ConnectorMySql(connection);
        };
    }
}
