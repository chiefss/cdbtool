package org.cdbtool.cdbtool.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

    public static boolean isSelect(String sql) {
        return sql.toLowerCase().startsWith("select ");
    }

    public static boolean isInsert(String sql) {
        return sql.toLowerCase().startsWith("insert ");
    }

    public static boolean isUpdate(String sql) {
        return sql.toLowerCase().startsWith("update ");
    }

    public static boolean isDelete(String sql) {
        return sql.toLowerCase().startsWith("delete ");
    }
}
