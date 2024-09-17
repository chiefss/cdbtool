package org.cdbtool.cdbtool;

import lombok.Getter;
import lombok.Setter;
import org.cdbtool.cdbtool.dtos.ConnectionDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Environment {

    private List<ConnectionDto> connections = new ArrayList<>();
}
