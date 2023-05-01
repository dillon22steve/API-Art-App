package cs1302.api;

import cs1302.helpers.*;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.Builder;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;


/**
 * A custom component that contains the search bar, drop own box, and search button. When the user
 * clicks search, the app queries the Met Museum's art collection based on the search parameters
 * collected from the user's inputs in the search bar and drop down box.
 */
public class MetPane extends HBox {

    //This index will be used to determine which Art object will be displayed to the user when they
    //click the "Next Piece" or "Previous Piece" buttons.
    private static int DISPLAYED_ART_INDEX = 0;

    private Button search;
    private ComboBox<String> dropDown;
    private Label testLabel;
    private TextField searchBar;
    private ArrayList<Art> artArray;
    protected ApiApp apiApp;

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    private  Gson gson = new GsonBuilder()
         .setPrettyPrinting()
         .create();


    /**
     * Constructs a MetPane object.
     */
    public MetPane() {
        this.setSpacing(8);
        this.search = new Button("Search");
        this.searchBar = new TextField("Type an artist, or any other search term.");
        this.artArray = new ArrayList<Art>();

        initDropDown();
        initHandlers();

        HBox.setHgrow(searchBar, Priority.ALWAYS);
        this.setMargin(searchBar, new Insets(0, 0, 0, 5));
        this.setMargin(search, new Insets(0, 5, 0, 0));
        this.getChildren().addAll(searchBar, dropDown, search);
    }


    /**
     * Initializes the dropDown that allows the user to specify what they are searching for.
     */
    public void initDropDown() {
        dropDown = new ComboBox<String>();
        dropDown.getItems().addAll(
            "Keyword",
            "Artist",
            "Title",
            "Country/Region");

        dropDown.setValue("Keyword");
    } //initDropDown


    /**
     * Initializes the EventHandler for the app's search button.
     */
    public void initHandlers() {
        EventHandler<ActionEvent> searchHandler = (ActionEvent e) -> {
            String term = searchBar.getText().trim();
            String filter = dropDown.getValue();
            HelperMethods.runNow( () -> searchMet(term, filter));
            //searchArtic();
        };

        search.setOnAction(searchHandler);
    } //initHandlers



    /**
     * Queries the Met Museum's collection of art based on search paramters provided by the user in
     * the drop down and search bar.
     *
     * @param term the term to search, collected from the search bar TextField.
     * @param filter the type of item to search for (artist, title, etc.), collected from the drop
     * down.
     */
    public void searchMet(String term, String filter) {
        resetArt();

        try {
            String url = craftUrl(term, filter);
            URI location = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("status code 200");
                throw new IOException(response.toString());
            } //if

            String jsonString = response.body();

            MetResponse metResponse = gson
                .fromJson(jsonString, MetResponse.class);

            double progressIncrement = 1.0 / 50.0;
            int percentIncrement = 2;
            if (metResponse.objectIDs.length < 50) {
                progressIncrement = (1.0 / metResponse.objectIDs.length);
                percentIncrement = (100 / metResponse.objectIDs.length);
            } //if

            System.out.println("Entering first for loop.");
            for (int i = 0; i < 50 && i < metResponse.objectIDs.length; i++) {
                loadMetArtInfo(metResponse.objectIDs[i]);

                //Updates the progress bar each iteration of the loop.
                Platform.runLater(
                    HelperMethods.createRunnableForProgressPane(
                        progressIncrement, percentIncrement, apiApp)
                );
            } //for
            System.out.println("First for loop finished");

            updateArtPane();
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater( () -> {
                Alert alert = new Alert(AlertType.ERROR,
                    ("Exception: " + e.getMessage()));
                alert.showAndWait();
            });
        } finally {
            Platform.runLater( () -> {
                apiApp.progressPane.setProgress(0.0, 0);
                apiApp.progressPane.progressPercent = 0;
            });
        } //try
    } //searchForDepts



    /**
     * Resets the GUI and artArray when the user starts a new query so that it will be repopulated
     * with the results from the new query.
     */
    public void resetArt() {
        artArray.clear();

        Platform.runLater( () -> {
            apiApp.artPane.artInfoPane.artInfo.setText("");
            apiApp.artPane.artInfoPane.museumSearch.setVisible(false);
            apiApp.artPane.artView.setImage(null);
        });
    } //resetArt



    /**
     * This is a helper method that creates a url for the loadMetArtInfo method.
     *
     * @param term the search term collected from the search bar.
     * @param filter the type of item to search for (artist, title, etc.), collected from the drop
     * down.
     * @return a url that will be used to query the Met's object API.
     */
    public String craftUrl(String term, String filter) {
        String url =
            "https://collectionapi.metmuseum.org/public/collection/v1/search?hasImages=true&";

        switch (filter) {
        case "Keyword":
            break;
        case "Title":
            url += "title=true&";
            break;
        case "Artist":
            url += "artistOrCulture&";
            break;
            /*
        case "Country/Region":
            String location = URLEncoder.encode(term, StandardCharsets.UTF_8);
            return (url += "geoLocation=" + location + "&q=");
            */
        } //switch

        String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
        String query = "q=" + q;
        return url = url + query;
    } //craftUri



    /**
     * Takes an objectId from the original query and queries the Met Museum's API for information
     * about the object at that unique ID. It stores the art's title, artist, time period,
     * department within the Met, and image URL in an Art object. The Art object's constructor will
     * also create an Image using the url.
     *
     * @param objectId the object to query the Met's API for.
     */
    public void loadMetArtInfo(int objectId) {
        try {
            String url = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"
                + objectId;
            URI location = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            String jsonString = response.body();
            ObjectResult objResult = gson
                .fromJson(jsonString, ObjectResult.class);

            //Creates an art object to store this data.
            String title = objResult.title;
            String artistName = objResult.artistDisplayName;
            String department = objResult.department;
            String period = objResult.objectDate;
            String country = objResult.country;
            String imageUrl = objResult.primaryImage;
            Art artToAdd = new Art(title, artistName, department, period, country, imageUrl);

            artArray.add(artToAdd);

        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
    } //loadArtistNames



    public void searchArtic(String term) {
        try {
            String url = "https://api.artic.edu/api/v1/artworks/search";
            String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
            String query = "?q=" + q;
            url = url + query;
            URI location = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("status code 200");
                throw new IOException(response.toString());
            } //if

            String jsonString = response.body();

            System.out.println(jsonString);

            ArticResponse articResponse = gson
                .fromJson(jsonString, ArticResponse.class);

            /*
            for (int i = 0; i < articResponse.results.length; i++) {
                loadArticArtInfo(articResponse.results[i]);
            } //for
            */


        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
    } //searchArtic



    public void loadArticArtInfo(ArticResult articResult) {
        System.out.println(articResult.title);
    } //loadArticArtInfo



    public void addToArrayList(String item, ArrayList<String> list) {
        if (item != null && !item.isEmpty()) {
            list.add(item);
            System.out.println(item);
        } //if
    } //addToArrayList



    /**
     * Updates the GUI so it displays the first art piece whose image URL is not null. It then
     * displays the art's information (title, artist, time period, and department within the Met.
     */
    public void updateArtPane() {

        //This loop finds the first image that is not null and displays it to the user, as well
        //as information about the artist.
        for (int i = 0; i < artArray.size(); i++) {
            if (artArray.get(i).image != null) {
                Art artToDisplay = artArray.get(i);
                apiApp.artPane.setDisplayedPiece(artToDisplay);

                Platform.runLater( () -> {
                    apiApp.artPane.artView.setImage(artToDisplay.image);

                    apiApp.artPane.artInfoPane.artInfo.setText(createArtInfo(artToDisplay));

                    apiApp.artPane.artInfoPane.museumSearch.setVisible(true);

                    apiApp.bottomBtnBar.nextBtn.setDisable(false);
                });
                break;
            }
        } //for
    } //updateArtPane


    /**
     * This is a helper method that creates a String for the artInfoPane's info Label.
     * Returns a String of the Art's information (title, artist, period, and department).
     *
     * @param art the Art object to generate the information String for.
     * @return a String of the Art's information (title, artist, period, and department).
     */
    public String createArtInfo(Art art) {
        String title = "Title: " + art.title;
        String artist = "Artist: " + art.artist;
        String period = "Period: " + art.period;
        String department = "Department: " + art.department;
        String info = title + "\n\n" + artist + "\n\n" + period + "\n\n" + department;

        return info;
    } //createArtInfo


    /**
     * Updates the ArtPane so that the next Art object in the artArray is displayed to the user.
     */
    public void nextArt() {
        apiApp.bottomBtnBar.previousBtn.setDisable(false);
        int index = DISPLAYED_ART_INDEX + 1;

        if (DISPLAYED_ART_INDEX < artArray.size()) {
            DISPLAYED_ART_INDEX += 1;
            if (DISPLAYED_ART_INDEX == artArray.size()) {
                apiApp.bottomBtnBar.nextBtn.setDisable(true);
            } else {
                apiApp.bottomBtnBar.nextBtn.setDisable(false);
            } //if
        } //if

        Art artToUpdateTo = artArray.get(index);
        apiApp.artPane.artView.setImage(artToUpdateTo.image);
        apiApp.artPane.setDisplayedPiece(artToUpdateTo);
        apiApp.artPane.artInfoPane.artInfo.setText(createArtInfo(artToUpdateTo));
    } //nextArt


    /**
     * Updates the ArtPane so that the previous Art object in the artArray is displayed to the user.
     */
    public void previousArt() {
        if (DISPLAYED_ART_INDEX > 0) {
            int index = DISPLAYED_ART_INDEX - 1;
            Art artToUpdateTo = artArray.get(index);

            apiApp.artPane.artView.setImage(artToUpdateTo.image);
            apiApp.artPane.setDisplayedPiece(artToUpdateTo);
            apiApp.artPane.artInfoPane.artInfo.setText(createArtInfo(artToUpdateTo));

            DISPLAYED_ART_INDEX -= 1;
            if (DISPLAYED_ART_INDEX == 0) {
                apiApp.bottomBtnBar.previousBtn.setDisable(true);
            } else {
                apiApp.bottomBtnBar.previousBtn.setDisable(false);
            } //if
        } //if
    } //previousArt
} //MetPane
