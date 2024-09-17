package org.cdbtool;

import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.CDBToolFacade;

@Log4j2
public class Main {

    public static void main(String[] args) {
        try {
            CDBToolFacade.init();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}