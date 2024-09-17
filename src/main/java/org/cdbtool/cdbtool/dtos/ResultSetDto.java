package org.cdbtool.cdbtool.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultSetDto {

    private TableSchemaDto schema = new TableSchemaDto();
    private List<TableRowDto> rows = new ArrayList<>();
}
