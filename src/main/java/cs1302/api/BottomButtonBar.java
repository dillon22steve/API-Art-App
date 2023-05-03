package cs1302.api;


import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;


/**
 * A custom component that contains the previous button and next buttons which, when pressed, will
 * display the next Art object to the user.
 */
public class BottomButtonBar extends HBox {

    Button previousBtn, nextBtn;
    ApiApp apiApp;

    /**
     * Constructs a BottomButtonBar object.
     */
    public BottomButtonBar() {
        this.previousBtn = new Button("Previous Piece");
        previousBtn.setDisable(true);
        previousBtn.setVisible(false);
        this.nextBtn = new Button("Next Piece");
        nextBtn.setVisible(false);
        nextBtn.setDisable(true);

        initHandlers();

        addToBar();
    } //constructor


    /**
     * Initializes the EventHandlers for the two buttons.
     */
    public void initHandlers() {
        EventHandler<ActionEvent> previousHandler = (ActionEvent e) -> {
            apiApp.metPane.previousArt();
        };

        EventHandler<ActionEvent> nextHandler = (ActionEvent e) -> {
            apiApp.metPane.nextArt();
        };

        previousBtn.setOnAction(previousHandler);
        nextBtn.setOnAction(nextHandler);
    } //initHandlers


    /**
     * Adds the buttons to the ButtonBar.
     */
    public void addToBar() {
        this.setSpacing(230);
        this.setMargin(previousBtn, new Insets(0, 0, 0, 70));
        this.getChildren().addAll(previousBtn, nextBtn);
    } //addToBar


    /**
     * Updates the previous piece and next piece buttons so that they are either re-enabled or
     * disabled, specified by the boolean values in the parameters.
     *
     * @param previous disables the previous piece button if {@code true}, enables the button
     * otherwise.
     * @param next disables the next piece button if {@code true}, enables the button otherwise.
     * @param visible makes the buttons visible if {@code true}, invisible otherwise.
     */
    public void updateButtons(boolean previous, boolean next, boolean visible) {
        previousBtn.setDisable(previous);
        previousBtn.setVisible(visible);
        nextBtn.setDisable(next);
        nextBtn.setVisible(visible);
    } //updateButtons


    /**
     * Resets the previous piece and next piece buttons so that they are disabled and not visible.
     */
    public void resetButtons() {
        previousBtn.setDisable(true);
        previousBtn.setVisible(false);
        nextBtn.setDisable(true);
        nextBtn.setVisible(false);
    } //resetButtons
} //BottomButtonBar
