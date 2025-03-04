package org.daniel.elysium.games.baccarat.center.models.banker;

import javax.swing.*;
import java.awt.*;

/**
 * Custom label for displaying the "BANKER" text in a Baccarat game interface.
 * This label is styled specifically to suit the game's aesthetic and functional requirements,
 * including custom font settings and adjusted component height to remove unnecessary padding.
 */
public class BankerTextUI extends JLabel {

    /**
     * Constructs a new BankerTextUI component with predefined text and style.
     * The label is set to display "BANKER", with a specific font, color, and alignment.
     * It also adjusts its size to fit the text exactly without any extra padding.
     */
    public BankerTextUI() {
        setText("BANKER");
        setFont(new Font("Roboto", Font.BOLD, 100));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);

        // Remove default padding by limiting the component's height
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
        int textHeight = metrics.getAscent() - metrics.getDescent(); // Exact text height without extra padding

        setPreferredSize(new Dimension(getPreferredSize().width, textHeight));
        setMaximumSize(new Dimension(getMaximumSize().width, textHeight));
        setMinimumSize(new Dimension(getMinimumSize().width, textHeight));
    }
}
