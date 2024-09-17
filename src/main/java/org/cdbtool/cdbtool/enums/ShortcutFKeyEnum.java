package org.cdbtool.cdbtool.enums;

import com.googlecode.lanterna.input.KeyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ShortcutFKeyEnum {

    F1(KeyType.F1, 1),
    F2(KeyType.F2, 2),
    F3(KeyType.F3, 3),
    F4(KeyType.F4, 4),
    F5(KeyType.F5, 5),
    F6(KeyType.F6, 6),
    F7(KeyType.F7, 7),
    F8(KeyType.F8, 8),
    F9(KeyType.F9, 9),
    F10(KeyType.F10, 10);

    private final KeyType key;
    private final int index;

    public static Optional<Integer> getIndexByKey(KeyType keyType) {
        return Arrays.stream(values())
                .filter(fKeyEnum -> fKeyEnum.key.equals(keyType))
                .map(fKeyEnum -> fKeyEnum.index)
                .findFirst();
    }
}
