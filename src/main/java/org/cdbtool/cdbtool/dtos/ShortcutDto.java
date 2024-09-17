package org.cdbtool.cdbtool.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortcutDto {

    private int index;
    private String label;
    private Runnable action;
}
