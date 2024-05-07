package gui;

import javafx.scene.control.ListView;

/**
 * MessageArea represents a custom ListView for displaying messages.
 * It extends the JavaFX ListView class with a String type parameter.
 *
 * @see ListView Represents a control that displays a scrollable list of items.
 * @see PackageCalculator Used for performing calculations related to packaging.
 */
public class MessagesArea extends ListView<String> {
    /**
     * Sets an info text message in the MessagesArea and displays it to the user.
     * @param text The text message to be displayed.
     */
    public void setMessage(String text) {
        // Clear previous messages
        this.getItems().clear();

        // Add the new message
        this.getItems().add(text);
    }

    /**
     * Retrieves the current message displayed in the MessagesArea.
     * Returns null if no message is displayed.
     *
     * @return the message text, or null if no message is present
     */
    public String getMessage() {
        return this.getItems().isEmpty() ? null : this.getItems().get(0);
    }

    /**
     * Clears the message text from the MessagesArea and hides it.
     */
    public void clearMessage() {
        this.getItems().clear();
    }
}