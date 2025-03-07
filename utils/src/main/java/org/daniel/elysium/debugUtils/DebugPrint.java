package org.daniel.elysium.debugUtils;

/**
 * A utility class for controlling debug printing based on a specified {@link DebugLevel}.
 * Implements a singleton pattern to ensure a single instance manages debug output.
 */
public class DebugPrint {

    /** Singleton instance of {@code DebugPrint}. */
    private static volatile DebugPrint instance;

    /** The current debug level controlling print output. */
    private static DebugLevel level = DebugLevel.DISABLED;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param level The debug level to set.
     */
    private DebugPrint(DebugLevel level) {
        DebugPrint.level = level;
    }

    /**
     * Returns the singleton instance of {@code DebugPrint}, creating it if necessary.
     *
     * @param level The debug level to set (applies only if the instance is created).
     * @return The singleton instance of {@code DebugPrint}.
     */
    public static DebugPrint getInstance(DebugLevel level) {
        DebugPrint debugPrint = instance;
        if (debugPrint == null) {
            synchronized (DebugPrint.class) {
                debugPrint = instance;
                if (debugPrint == null) {
                    instance = debugPrint = new DebugPrint(level);
                }
            }
        }
        return debugPrint;
    }

    /**
     * Flushes the output stream buffer if the debug level is set to {@code ALL} or higher.
     */
    public static void flush() {
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.flush();
        }
    }

    /**
     * Prints the given object to the console if the debug level is set to {@code ALL} or higher.
     *
     * @param x The object to print.
     */
    public static void print(Object x) {
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.print(x);
        }
    }

    /**
     * Prints the given object if the debug flag is enabled and the debug level allows it.
     *
     * @param x     The object to print.
     * @param debug If {@code true}, printing is allowed based on the debug level.
     */
    public static void print(Object x, boolean debug) {
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.print(x);
        }
    }

    /**
     * Prints the given object to the console followed by a new line if the debug level allows it.
     *
     * @param x The object to print.
     */
    public static void println(Object x) {
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.println(x);
        }
    }

    /**
     * Prints a blank line to the console if the debug level allows it.
     */
    public static void println() {
        if (level.getValue() >= DebugLevel.ALL.getValue()) {
            System.out.println();
        }
    }

    /**
     * Prints the given object with a new line if the debug flag is enabled and the debug level allows it.
     *
     * @param x     The object to print.
     * @param debug If {@code true}, printing is allowed based on the debug level.
     */
    public static void println(Object x, boolean debug) {
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.println(x);
        }
    }

    /**
     * Prints a blank line if the debug flag is enabled and the debug level allows it.
     *
     * @param debug If {@code true}, printing is allowed based on the debug level.
     */
    public static void println(boolean debug) {
        if (level.getValue() <= DebugLevel.ALL.getValue()
                && level.getValue() > DebugLevel.DISABLED.getValue()
                && debug) {
            System.out.println();
        }
    }
}
