package org.daniel.elysium.interfaces;

import org.daniel.elysium.models.panels.ChipPanel;

/**
 * Interface for classes that consume and manage a ChipPanel.
 * This interface provides the necessary methods to set and retrieve a ChipPanel, allowing for flexible integration
 * and management of chip panels within user interfaces.
 */
public interface ChipPanelConsumer {

    /**
     * Sets the ChipPanel associated with this consumer.
     * This method allows for dynamic replacement or initialization of a chip panel within the consumer.
     *
     * @param panel The ChipPanel to be set within the consumer.
     */
    void setChipPanel(ChipPanel panel);

    /**
     * Retrieves the currently associated ChipPanel.
     * This method allows access to the chip panel currently managed by this consumer,
     * enabling interactions and updates to the panel's state or appearance.
     *
     * @return The current ChipPanel associated with this consumer.
     */
    ChipPanel getChipPanel();
}
