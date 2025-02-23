package org.daniel.elysium.debugUtils;

public class DebugPrint {
    //TODO: create custom scanner
    private static volatile DebugPrint instance;
    private static DebugLevel level = DebugLevel.DISABLED;

    private DebugPrint(DebugLevel level){
        DebugPrint.level = level;
    }

    public static DebugPrint getInstance(DebugLevel level){
        DebugPrint debugPrint = instance;
        if (debugPrint == null){
            synchronized (DebugPrint.class){
                debugPrint = instance;
                if (debugPrint == null){
                    instance = debugPrint = new DebugPrint(level);
                }
            }
        }
        return debugPrint;
    }



    public static void print(Object x){
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.print(x);
        }
    }

    public static void print(Object x, boolean debug){
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.print(x);
        }
    }

    public static void println(Object x){
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.println(x);
        }
    }

    public static void println(){
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.println();
        }
    }

    public static void println(Object x, boolean debug){
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.println(x);
        }
    }

    public static void println(boolean debug){
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.println();
        }
    }
}
