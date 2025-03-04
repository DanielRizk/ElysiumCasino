package org.daniel.elysium.games.baccarat.center.models.player;

import javax.swing.*;
import java.awt.*;

/**
 * Custom label for displaying the "PLAYER" text in a Baccarat game interface.
 * This label is styled specifically to suit the game's aesthetic and functional requirements,
 * including custom font settings and adjusted component height to remove unnecessary padding.
 */
public class PlayerTextUI extends JLabel {

    /**
     * Constructs a new PlayerTextUI component with predefined text and style.
     * The label is set to display "PLAYER", with a specific font, color, and alignment.
     * It also adjusts its size to fit the text exactly without any extra padding.
     */
    public PlayerTextUI() {
        setText("PLAYER");
        setFont(new Font("Roboto", Font.BOLD, 100));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        adjustHeight();
    }

    /**
     * Adjusts the height of the label to exactly fit the height of the text,
     * removing the default padding around the text typically provided by the component.
     * This is achieved by setting the preferred, maximum, and minimum sizes of the label
     * based on the exact text height calculated using font metrics.
     */
    private void adjustHeight() {
        FontMetrics metrics = getFontMetrics(getFont());
        int textHeight = metrics.getAscent() - metrics.getDescent(); // Calculate exact text height

        setPreferredSize(new Dimension(getPreferredSize().width, textHeight));
        setMaximumSize(new Dimension(getMaximumSize().width, textHeight));
        setMinimumSize(new Dimension(getMinimumSize().width, textHeight));
    }
}

