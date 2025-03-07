package org.daniel.elysium.cliUtils;

import org.daniel.elysium.debugUtils.DebugPrint;

import java.io.IOException;

public class CmdHelper {
    // TODO: JAVADOC
    public static void clearCMD() {
        try {
            DebugPrint.print("\033[H\033[2J");
            DebugPrint.flush();
        } catch (Exception e) {
            DebugPrint.println(e, true);
        }
    }

    public static void haltCMD() {
        DebugPrint.println("Press any key to continue...");
        try {
            DebugPrint.println(System.in.read(), true);
        } catch (IOException e){
            DebugPrint.println(e, true);
        }

    }
}
