package cs1302.api;

import cs1302.api.art.Art;
import cs1302.helpers.HelperMethods;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;


/**
 * This is a custom component that will be displayed when the user clicks "Search for pieces by this
 * artist at the Art Institute of Chicago" button.
 */
public class ArticPane extends HBox {

    //The artInfoBox holds the information for the art, and the artBox holds the ImageView that
    //displays the piece to the user after they click a view button.
    public VBox artInfoBox, artBox;

    private ApiApp apiApp;

    public HBox infoBox1, infoBox2, infoBox3, infoBox4;
    public HBox buttonBox;
    public Label artInfo1, artInfo2, artInfo3, artInfo4;
    public Button view1, view2, view3, view4;
    public Button nextPage, previousPage;
    public Button backToArticResults, backToMetResults;
    public Art[] articResults = new Art[4];
    public int indexCurrentlyDisplayed = 0;

    public ImageView artView;

    private static Font DEFAULT_FONT = new Font("Book Antiqua", 13);

    /**
     * Constructs an ArticPane object.
     * @param apiApp the application that this ArticPane object was added to.
     */
    public ArticPane(ApiApp apiApp) {
        this.apiApp = apiApp;

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
        artInfo1.setFont(DEFAULT_FONT);

        artInfo2 = new Label("");
        artInfo2.setWrapText(true);
        artInfo2.setFont(DEFAULT_FONT);

        artInfo3 = new Label("");
        artInfo3.setWrapText(true);
        artInfo3.setFont(DEFAULT_FONT);

        artInfo4 = new Label("");
        artInfo4.setWrapText(true);
        artInfo4.setFont(DEFAULT_FONT);
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
        HelperMethods.updateButton(previousPage, true, true);

        backToArticResults = new Button("Back");
        HelperMethods.updateButton(backToArticResults, true, false);
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

        //When the user clicks previous page, the app displays the four previous results. If the new
        //results are the first four, it disables the button to avoid an index out of bounds error.
        EventHandler<ActionEvent> prev = (ActionEvent e) -> {
            nextPage.setDisable(false);
            indexCurrentlyDisplayed = indexCurrentlyDisplayed - 4;
            updateArt(apiApp.metPane.articArtArray, indexCurrentlyDisplayed);

            if (indexCurrentlyDisplayed == 0) {
                previousPage.setDisable(true);
            } //if
        };

        //When the user clicks next page, the app displays the next four results. If the new
        //results are the last four, it disables the button to avoid an index out of bounds error.
        EventHandler<ActionEvent> next = (ActionEvent e) -> {
            previousPage.setDisable(false);
            indexCurrentlyDisplayed = indexCurrentlyDisplayed + 4;
            updateArt(apiApp.metPane.articArtArray, indexCurrentlyDisplayed);

            if (indexCurrentlyDisplayed == 8) {
                nextPage.setDisable(true);
            } //if
        };

        //When the user clicks Back, it returns the app back to the list of different works by the
        //artist and resets the view buttons so the user can view another piece.
        EventHandler<ActionEvent> backToArticHandler = (ActionEvent e) -> {
            artView.setImage(null);
            resetLabelProperties();
            updateArt(apiApp.metPane.articArtArray, indexCurrentlyDisplayed);
            HelperMethods.updateButton(backToArticResults, true, false);
            HelperMethods.updateButtons(view1, view2, view3, view4, false, true);
            previousPage.setVisible(true);
            nextPage.setVisible(true);
        };

        //Returns the app back to the Met Museum scene where the user can continue viewing different
        //works from their original query.
        EventHandler<ActionEvent> backToMetHandler = (ActionEvent e) -> {
            resetPane();
            apiApp.switchScenes(true);
        };

        previousPage.setOnAction(prev);
        nextPage.setOnAction(next);
        backToArticResults.setOnAction(backToArticHandler);
        backToMetResults.setOnAction(backToMetHandler);
    } //initHandlers


    /**
     * This is a helper method that creates an EventHandler for the pane's view buttons. When the
     * user clicks view, it displays the art's image and changes the labels to display more detailed
     * information about the piece.
     * @param artIndex the index to retrieve the Art object from the pane's art array.
     * @return an EventHandler that will display the art and it's info to the user when a view
     * button is clicked.
     */
    private EventHandler<ActionEvent> createViewHandler(int artIndex) {
        EventHandler<ActionEvent> handler = (ActionEvent e) -> {
            Art artToUpdate = articResults[artIndex];
            artView.setImage(artToUpdate.image);

            //The text for artInfo1 appears green if the piece is currently on display at the Art
            //Institute of Chicago. The text appears red if it is not on display.
            artInfo1.setText(artToUpdate.isOnDisplay);
            artInfo1.setFont(new Font("Book Antiqua", 15));
            artInfo1.setTextFill(HelperMethods.createTextFillColor(artToUpdate.isOnDisplay));

            artInfo2.setFont(new Font("Book Antiqua", 15));
            artInfo2.setText("\"" + artToUpdate.title + "\"");
            artInfo3.setFont(new Font("Book Antiqua", 15));
            artInfo3.setText("By: " + artToUpdate.artist);
            artInfo4.setFont(new Font("Book Antiqua", 15));
            artInfo4.setText("Period: " + artToUpdate.period);

            //enables the back button which will allow the user to return to the list of artworks.
            //Renders the view, previous page and next page buttons invisible to the user.
            HelperMethods.updateButton(backToArticResults, false, true);
            previousPage.setVisible(false);
            nextPage.setVisible(false);
            HelperMethods.updateButtons(view1, view2, view3, view4, true, false);
        };

        return handler;
    } //createViewHandler


    /**
     * Initializes the infoBox HBoxes that will hold the info for the artic results as well as the
     * view buttons that the user can click to view the piece.
     */
    public void initBoxes() {
        artBox = new VBox(5);
        artBox.setMargin(artView, new Insets(0, 10, 0, 0));
        //artBox.setMargin(backToArticResults, new Insets(0, 0, 0, 50));
        artBox.setAlignment(Pos.TOP_CENTER);
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
        artInfoBox.setMargin(backToMetResults, new Insets(5, 0, 0, 230));
        backToMetResults.setAlignment(Pos.BOTTOM_CENTER);
        artInfoBox.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4,
            buttonBox, backToMetResults);

        this.setMargin(artInfoBox, new Insets(0, 0, 0, 10));
        VBox.setVgrow(artInfoBox, Priority.ALWAYS);
        this.getChildren().addAll(artInfoBox, artBox);
    } //initInfoBoxes


    /**
     * Updates the art array and the information that is displayed to the user.
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
     * Resets the artInfo Label properties back to their original Font size and color.
     */
    public void resetLabelProperties() {
        artInfo1.setFont(DEFAULT_FONT);
        artInfo1.setTextFill(Color.web("0x000000ff"));
        artInfo2.setFont(DEFAULT_FONT);
        artInfo3.setFont(DEFAULT_FONT);
        artInfo4.setFont(DEFAULT_FONT);
    } //resetLabelProperties


    /**
     * Resets the pane when the user returns to the Met Museum results so that the next time they
     * switch to the articScene it has the correct settings.
     *
     * Disables the previousPage button and makes it visible. Enables the nextPage button and
     * makes it visible. Enables the view buttons and makes them visible, and disables the Back
     * button and makes it invisible.
     *
     * Resets the artInfo Labels so they are not displaying any information, and resets the artView
     * so it is not displaying any images. Also resets the properties of the artInfo labels so they
     * have the correct font and text color.
     *
     * Removes the art from the results array.
     */
    public void resetPane() {
        articResults[0] = null;
        articResults[1] = null;
        articResults[2] = null;
        articResults[3] = null;

        resetLabelProperties();
        artInfo1.setText("");
        artInfo2.setText("");
        artInfo3.setText("");
        artInfo4.setText("");

        artView.setImage(null);

        HelperMethods.updateButton(previousPage, true, true);
        HelperMethods.updateButton(nextPage, false, true);
        HelperMethods.updateButtons(view1, view2, view3, view4, false, true);
        HelperMethods.updateButton(backToArticResults, true, false);
    } //resetPane
} //ArticPane
