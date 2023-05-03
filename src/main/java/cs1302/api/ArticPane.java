package cs1302.api;

import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;


/**
 * This is a custom component that will be displayed when the user clicks "Search for pieces by this
 * artist at the Art Institute of Chicago" button.
 */
public class ArticPane extends HBox {

    //The artInfoBox holds the information for the art, and the artBox holds the ImageView that
    //displays the piece to the user after they click a view button.
    public VBox artInfoBox, artBox;

    public HBox infoBox1, infoBox2, infoBox3, infoBox4;
    public HBox buttonBox;
    public Label artInfo1, artInfo2, artInfo3, artInfo4;
    public Button view1, view2, view3, view4;
    public Button nextPage, previousPage;
    public Button backToArticResults, backToMetResults;
    public Art[] articResults = new Art[4];

    public ImageView artView;

    /**
     * Constructs an ArticPane object.
     */
    public ArticPane() {
        this.artView = new ImageView();
        artView.setVisible(false);

        initInfoLabels();
        initButtons();
        initHandlers();
        initBoxes();
    } //constructor


    /**
     * Initializes the artInfo Label objects.
     */
    public void initInfoLabels() {
        artInfo1 = new Label("");
        artInfo2 = new Label("");
        artInfo3 = new Label("");
        artInfo4 = new Label("");
    } //initInfoLabels


    /**
     * Initializes the view buttons and previous and next page buttons.
     */
    public void initButtons() {
        view1 = new Button("view");
        view2 = new Button("view");
        view3 = new Button("view");
        view4 = new Button("view");

        nextPage = new Button("next page");
        previousPage = new Button("previous page");

        backToArticResults = new Button("back");
        backToMetResults = new Button("return to Met");
    } //initButtons


    /**
     * Initializes the EventHandlers to update the app when the user clicks a button.
     */
    public void initHandlers() {

    } //initHandlers


    /**
     * Initializes the infoBox HBoxes that will hold the info for the artic results as well as the
     * view buttons that the user can click to view the piece.
     */
    public void initBoxes() {
        artBox = new VBox(5);
        artBox.getChildren().addAll(artView, backToArticResults);

        infoBox1 = new HBox(5);
        infoBox1.getChildren().addAll(artInfo1, view1);
        infoBox2 = new HBox(5);
        infoBox2.getChildren().addAll(artInfo2, view2);
        infoBox3 = new HBox(5);
        infoBox3.getChildren().addAll(artInfo3, view3);
        infoBox4 = new HBox(5);
        infoBox4.getChildren().addAll(artInfo4, view4);

        buttonBox = new HBox(20);
        buttonBox.getChildren().addAll(previousPage, nextPage);

        artInfoBox = new VBox(5);
        artInfoBox.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4,
            buttonBox, backToMetResults);
    } //initInfoBoxes
} //ArticPane
