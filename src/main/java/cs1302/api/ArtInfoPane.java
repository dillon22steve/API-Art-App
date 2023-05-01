package cs1302.api;


import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;


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


    /**
     * Constructs an ArtInfoPane object.
     */
    public ArtInfoPane() {
        this.search = new Button("Search");
        this.artInfo = new Label();
        artInfo.setWrapText(true);
        artInfo.setFont(new Font("Book Antiqua", 15.0));

        initDropDown();
        initMuseumSearchBox();
        initSearchHandler();

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
        museumSearch.getChildren().addAll(museums, search);
    } //initMuseumSearchBox


    /**
     * Initializes the EventHandler to handle when the user clicks the search button.
     */
    public void initSearchHandler() {
        switch (museums.getValue()) {
        case "Art Institute of Chicago":
            break;
        case "Harvard Art Museum":
            break;
        case "both":
            break;
        } //switch
    } //initSearchHandler


    /**
     * Adds the artInfo Label object and museumSearch HBox to the pane.
     */
    public void addToPane() {
        this.setSpacing(8);
        this.museumSearch.setVisible(false);
        this.setMargin(museumSearch, new Insets(30, 0, 0, 0));
        this.getChildren().addAll(artInfo, museumSearch);
    } //addToPane


    /**
     * Adds the museumSearch box after the user clicks search.
     */
    public void addSearchBox() {
        this.getChildren().add(museumSearch);
    } //addSearchBox
} //ArtInfoPane
