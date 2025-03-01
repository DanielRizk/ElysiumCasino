package org.daniel.elysium.debugUtils;

/**
 * Defines different levels of debug logging in the application.
 * Controls which debug messages should be printed based on their severity level.
 */
public enum DebugLevel {

    /** Disables all debug output. */
    DISABLED(-1),

    /** Enables debug-level messages. */
    DEBUG(0),

    /** Enables all debug messages, including general logs. */
    ALL(1),

    /** Enables only essential print statements. */
    PRINT(2);

    /** The numeric value representing the debug level. */
    private final int value;

    /**
     * Constructs a {@code DebugLevel} with the specified numeric value.
     *
     * @param value The integer representation of the debug level.
     */
    DebugLevel(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of the debug level.
     *
     * @return The integer value associated with the debug level.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the name of the debug level as a string.
     *
     * @return The name of the debug level.
     */
    @Override
    public String toString() {
        return name();
    }
}
