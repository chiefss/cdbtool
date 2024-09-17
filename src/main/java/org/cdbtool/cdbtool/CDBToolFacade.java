package org.cdbtool.cdbtool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CDBToolFacade {

    public static void init() throws Exception {
        CDBTool cdbTool = new CDBTool();
        cdbTool.loadConfig();
        cdbTool.initUi();
        cdbTool.initDefaultContent();
        cdbTool.startUi();
    }
}
