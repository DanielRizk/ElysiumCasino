package org.daniel.elysium.debugUtils;

public enum DebugLevel {
    DISABLED(-1),
    DEBUG(0),
    ALL(1),
    PRINT(2);

    private final int value;

    DebugLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }
}
