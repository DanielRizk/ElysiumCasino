package org.daniel.elysium.underdev;

import java.awt.*;

public interface Resizable {
    float LOW_SCALE = 0.5f;
    float MEDIUM_SCALE = 0.8f;
    float HIGH_SCALE = 1.0f;

    Dimension getScaledDimension();
}
