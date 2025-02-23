package org.daniel.elysium.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenManager {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public ScreenManager(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.cardLayout = (CardLayout) mainPanel.getLayout();
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void addScreen(JPanel screen, String screenName) {
        mainPanel.add(screen, screenName);
    }
}
