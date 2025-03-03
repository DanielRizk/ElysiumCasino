package org.daniel.elysium.games.baccarat.center;

import javax.swing.*;
import java.awt.*;

public class BankerTextUI extends JLabel {
    public BankerTextUI() {
        setText("BANKER");
        setFont(new Font("Roboto", Font.BOLD, 100));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);

        // Remove default padding by limiting the component's height
        adjustHeight();
    }

    private void adjustHeight() {
        FontMetrics metrics = getFontMetrics(getFont());
        int textHeight = metrics.getAscent() - metrics.getDescent(); // Exact text height without extra padding

        setPreferredSize(new Dimension(getPreferredSize().width, textHeight));
        setMaximumSize(new Dimension(getMaximumSize().width, textHeight));
        setMinimumSize(new Dimension(getMinimumSize().width, textHeight));
    }
}
