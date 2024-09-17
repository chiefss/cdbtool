package org.cdbtool.cdbtool.dtos;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConnectionDto {

    private UUID id;
    private String name;
    private int adapterId;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
}
