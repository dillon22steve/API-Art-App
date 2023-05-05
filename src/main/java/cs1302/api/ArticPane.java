package cs1302.api;

import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        this.artView.setFitWidth(300);
        this.artView.setFitHeight(300);
        this.artView.setPreserveRatio(true);

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
        artInfo1.setWrapText(true);
        artInfo1.setFont(new Font("Book Antiqua", 13));

        artInfo2 = new Label("");
        artInfo2.setWrapText(true);
        artInfo2.setFont(new Font("Book Antiqua", 13));

        artInfo3 = new Label("");
        artInfo3.setWrapText(true);
        artInfo3.setFont(new Font("Book Antiqua", 13));

        artInfo4 = new Label("");
        artInfo4.setWrapText(true);
        artInfo4.setFont(new Font("Book Antiqua", 13));
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
        backToArticResults.setVisible(false);
        backToArticResults.setDisable(true);
        backToMetResults = new Button("Return to Met Museum Results");
    } //initButtons


    /**
     * Initializes the EventHandlers to update the app when the user clicks a button.
     */
    public void initHandlers() {
        view1.setOnAction(createViewHandler(0));
        view2.setOnAction(createViewHandler(1));
        view3.setOnAction(createViewHandler(2));
        view4.setOnAction(createViewHandler(3));
    } //initHandlers


    /**
     * This is a helper method that creates an EventHandler for the pane's view buttons.
     * @param artIndex the index to retrieve the Art object from the pane's art array.
     * @return an EventHandler that will display the art and it's info to the user when a view
     * button is clicked.
     */
    private EventHandler<ActionEvent> createViewHandler(int artIndex) {
        EventHandler<ActionEvent> handler = (ActionEvent e) -> {
            Art artToUpdate = articResults[artIndex];
            artView.setImage(artToUpdate.image);
            artInfo1.setText("\"" + artToUpdate.title + "\"");
            artInfo2.setText("By: " + artToUpdate.artist);
            artInfo3.setText(artToUpdate.period);
            artInfo4.setText(artToUpdate.department);

            updateViewBtns(true, false);
        };

        return handler;
    } //createViewHandler


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

        buttonBox = new HBox(30);
        buttonBox.getChildren().addAll(previousPage, nextPage);

        artInfoBox = new VBox(30);
        artInfoBox.setMinWidth(300);
        artInfoBox.setMinHeight(400);
        artInfoBox.setMargin(backToMetResults, new Insets(0, 0, 0, 230));
        artInfoBox.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4,
            buttonBox, backToMetResults);

        this.setMargin(artInfoBox, new Insets(0, 0, 0, 10));
        VBox.setVgrow(artInfoBox, Priority.ALWAYS);
        this.getChildren().addAll(artInfoBox, artBox);
    } //initInfoBoxes


    /**
     * Updates the art array and the information displayed to the user.
     * @param art the Art objects that will be used to update the pane.
     * @param indexToStart the index to start in the art ArrayList.
     */
    public void updateArt(ArrayList<Art> art, int indexToStart) {
        artInfo1.setText("\"" + art.get(indexToStart).title + "\"\n" +
            art.get(indexToStart).artist);
        articResults[0] = art.get(indexToStart);

        artInfo2.setText("\"" + art.get(indexToStart + 1).title + "\"\n" +
            art.get(indexToStart + 1).artist);
        articResults[1] = art.get(indexToStart + 1);

        artInfo3.setText("\"" + art.get(indexToStart + 2).title + "\"\n" +
            art.get(indexToStart + 2).artist);
        articResults[2] = art.get(indexToStart + 2);

        artInfo4.setText("\"" + art.get(indexToStart + 3).title + "\"\n" +
            art.get(indexToStart + 3).artist);
        articResults[3] = art.get(indexToStart + 3);
    } //updateArt


    /**
     * Updates the disable and visible values of the pane's view buttons.
     * @param disable the boolean value to set the buttons' disabled values.
     * @param visible the boolean value to set the buttons' visible values.
     */
    public void updateViewBtns(boolean disable, boolean visible) {
        view1.setDisable(disable);
        view1.setVisible(visible);
        view2.setDisable(disable);
        view2.setVisible(visible);
        view3.setDisable(disable);
        view3.setVisible(visible);
        view4.setDisable(disable);
        view4.setVisible(visible);
    } //updateBiewBtns
} //ArticPane
