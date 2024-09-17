package org.cdbtool.cdbtool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cdbtool.cdbtool.exceptions.AdapterEnumException;

@Getter
@AllArgsConstructor
public enum AdapterEnum {

    PG(1, "Postgres", "jdbc:postgresql://%s:%d/%s"),
    MYSQL(2, "MySQL", "jdbc:mysql://%s:%d/%s");

    private final int id;
    private final String name;
    private final String url;

    public static AdapterEnum findById(int id) throws AdapterEnumException {
        for (AdapterEnum value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        throw new AdapterEnumException(String.format("Cannot find adapter by id \"%d\"", id));
    }

    public static AdapterEnum findByName(String name) throws AdapterEnumException {
        for (AdapterEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new AdapterEnumException(String.format("Cannot find adapter by name \"%s\"", name));
    }
}
