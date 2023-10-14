package cs1302.api.art;


import cs1302.api.ApiApp;
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
import javafx.application.Platform;


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
            "Search for works by this artist at the \nArt Institute of Chicago";
        this.search = new Button(searchBtnText);
        search.setWrapText(true);
        this.artInfo = new Label();
        artInfo.setWrapText(true);
        artInfo.setFont(new Font("Book Antiqua", 15.0));

        initMuseumSearchBox();
        initBtnHandlers();

        addToPane();
    } //constructor


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
            HelperMethods.runNow( () -> {
                apiApp.getMetPane().searchArtic();
                Platform.runLater( () -> apiApp.switchScenes(false));
            });
        };

        search.setOnAction(searchHandler);
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
     * Updates the art information that is displayed to the user.
     * @param art the Art object who's information that will be displayed to the user.
     */
    public void updateArtInfo(Art art) {
        String info =
            "Title: \"" + art.title + "\"\n\n" +
            "Artist: " + art.artist + "\n\n" +
            "Period: " + art.period + "\n\n" +
            "Department: " + art.department;


        artInfo.setText(info);
    } //updateArtInfo


    /**
     * Resets the artInfo label to an empty String.
     */
    public void resetArtInfo() {
        artInfo.setText("");
    } //resetArtInfo
} //ArtInfoPane
