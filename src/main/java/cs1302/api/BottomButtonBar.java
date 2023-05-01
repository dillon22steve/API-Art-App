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
        this.nextBtn = new Button("Next Piece");
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
} //BottomButtonBar
