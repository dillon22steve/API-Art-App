package cs1302.api;


import cs1302.helpers.*;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;


/**
 * A custom component that holds the artist info and creates the functionality of the app that
 * allows the user to query other museums for art by the same artist.
 */
public class ArtInfoPane extends VBox {

    public Button search;
    public Label artInfo;
    public ComboBox<String> museums;
    public HBox museumSearch;
    public ApiApp apiApp;

    public HBox infoBox1, infoBox2, infoBox3, infoBox4;
    public Label info1, info2, info3, info4;
    public Button view1, view2, view3, view4;

    public Art[] articResults = new Art[4];


    /**
     * Constructs an ArtInfoPane object.
     * @param apiApp the application that the pane is going to be added to.
     */
    public ArtInfoPane(ApiApp apiApp) {
        String searchBtnText =
            "Search for other works by this artist at the \nArt Institute of Chicago";
        this.search = new Button(searchBtnText);
        search.setWrapText(true);
        this.artInfo = new Label();
        artInfo.setWrapText(true);
        artInfo.setFont(new Font("Book Antiqua", 15.0));

        initDropDown();
        initMuseumSearchBox();
        initArticBoxes();
        initBtnHandlers();

        addToPane();
    } //constructor


    /**
     * Initializes the museum drop down that allows the user to choose which museum they want to
     * query.
     */
    public void initDropDown() {
        this.museums = new ComboBox<String>();
        museums.getItems().addAll(
            "Art Institute of Chicago",
            "Harvard Art Museum",
            "Both");

        museums.setValue("Art Institute of Chicago");
    } //initDropDown


    /**
     * Initializes the HBox that contains the search button and museum drop down.
     */
    public void initMuseumSearchBox() {
        this.museumSearch = new HBox(5);
        //museumSearch.getChildren().addAll(museums, search);
        museumSearch.getChildren().add(search);
    } //initMuseumSearchBox


    /**
     * Initializes the EventHandler to handle when the user clicks the search button.
     */
    public void initBtnHandlers() {
        EventHandler<ActionEvent> searchHandler = (ActionEvent e) -> {
            //search.setDisable(true);
            //search.setVisible(false);
            //HelperMethods.runNow( () -> apiApp.metPane.searchArtic());
            System.out.println("Method entered.");
            apiApp.stage.setScene(apiApp.articScene);
            apiApp.stage.show();
            System.out.println("Method ended.");
        };

        search.setOnAction(searchHandler);
        view1.setOnAction(createViewBtnHandler(0));
        view2.setOnAction(createViewBtnHandler(1));
        view3.setOnAction(createViewBtnHandler(2));
        view4.setOnAction(createViewBtnHandler(3));
    } //initSearchHandler


    /**
     * Creates an EventHandler that will update the app when the user clicks a view button.
     * @param index the index with which to retrieve an Art object from the articResults array.
     * @return an EventHandler that will update the app when the user clicks a view button.
     */
    public EventHandler<ActionEvent> createViewBtnHandler(int index) {
        EventHandler<ActionEvent> viewBtnHandler = (ActionEvent e) -> {
            Art artToDisplay = articResults[index];
            apiApp.artPane.artView.setImage(artToDisplay.image);
            info1.setText("Title: " + artToDisplay.title);
            info2.setText("Artist: " + artToDisplay.artist);
            info3.setText("Period: " + artToDisplay.period);
            info4.setText("Department: " + artToDisplay.department);

            updateViewBtns(true, false);
        };

        return viewBtnHandler;
    } //createViewBtnHandler


    /**
     * Initializes the HBoxes that will contain the information related to the results of the user's
     * query.
     */
    public void initArticBoxes() {
        this.infoBox1 = new HBox(8);
        this.infoBox2 = new HBox(8);
        this.infoBox3 = new HBox(8);
        this.infoBox4 = new HBox(8);

        this.info1 = new Label("");
        info1.setWrapText(true);
        this.info2 = new Label("");
        info2.setWrapText(true);
        this.info3 = new Label("");
        info3.setWrapText(true);
        this.info4 = new Label("");
        info4.setWrapText(true);

        this.view1 = new Button("view");
        view1.setDisable(true);
        view1.setVisible(false);
        this.view2 = new Button("view");
        view2.setDisable(true);
        view2.setVisible(false);
        this.view3 = new Button("view");
        view3.setDisable(true);
        view3.setVisible(false);
        this.view4 = new Button("view");
        view4.setDisable(true);
        view4.setVisible(false);

        infoBox1.getChildren().addAll(info1, view1);
        infoBox2.getChildren().addAll(info2, view2);
        infoBox3.getChildren().addAll(info3, view3);
        infoBox4.getChildren().addAll(info4, view4);
    } //initArticBoxes


    /**
     * Adds the artInfo Label object and museumSearch HBox to the pane.
     */
    public void addToPane() {
        this.setSpacing(8);
        this.museumSearch.setVisible(false);
        this.setMargin(museumSearch, new Insets(30, 0, 0, 0));

        this.getChildren().addAll(infoBox1, infoBox2, infoBox3, infoBox4, museumSearch);
    } //addToPane


    /**
     * Resets the labels to empty Strings, and resets the view buttons so that they are no longer
     * visible and are disabled.
     */
    public void resetLabelsAndButtons() {
        apiApp.artPane.artInfoPane.museumSearch.setVisible(false);

        info1.setText("");
        info2.setText("");
        info3.setText("");
        info4.setText("");

        view1.setVisible(false);
        view1.setDisable(true);
        view2.setVisible(false);
        view2.setDisable(true);
        view3.setVisible(false);
        view3.setDisable(true);
        view4.setVisible(false);
        view4.setDisable(true);
    } //resetLabels


    /**
     * Updates the labels after the user clicks one of the view buttons and queries the Art
     * Institute of Chicago.
     * @param artArray the ArrayList populated by the Art Institute of Chicago query.
     * @param indexToStart the index of the ArrayList to display to the user.
     */
    public void updateLabels(ArrayList<Art> artArray, int indexToStart) {
        info1.setText(("\"" + artArray.get(indexToStart).title + "\"\n by " +
            artArray.get(indexToStart).artist));
        articResults[0] = artArray.get(indexToStart);

        info2.setText(("\"" + artArray.get(indexToStart + 1).title + "\"\n by " +
            artArray.get(indexToStart + 1).artist));
        articResults[1] = artArray.get(indexToStart + 1);

        info3.setText(("\"" + artArray.get(indexToStart + 2).title + "\"\n by " +
            artArray.get(indexToStart + 2).artist));
        articResults[2] = artArray.get(indexToStart + 2);

        info4.setText(("\"" + artArray.get(indexToStart + 3).title + "\"\n by " +
            artArray.get(indexToStart + 3).artist));
        articResults[3] = artArray.get(indexToStart + 3);
    } //updateLabels


    /**
     * Updates the view buttons based on the boolean values in the parameters.
     * @param disabled the boolean value to set the buttons' disable value.
     * @param visible the boolean value to set the buttons' visible value.
     */
    public void updateViewBtns(boolean disabled, boolean visible) {
        view1.setDisable(disabled);
        view1.setVisible(visible);
        view2.setDisable(disabled);
        view2.setVisible(visible);
        view3.setDisable(disabled);
        view3.setVisible(visible);
        view4.setDisable(disabled);
        view4.setVisible(visible);
    } //updateViewBtns
} //ArtInfoPane
