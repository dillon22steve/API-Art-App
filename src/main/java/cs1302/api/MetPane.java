package cs1302.api;

import cs1302.api.art.Art;
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
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
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
    protected static int DISPLAYED_ART_INDEX = 0;

    private Button search;
    private ComboBox<String> dropDown;
    private Label testLabel;
    private TextField searchBar;
    private ArrayList<Art> artArray;
    protected ArrayList<Art> articArtArray;
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
     * @param apiApp the ApiApp object that this pane belongs to.
     */
    public MetPane(ApiApp apiApp) {
        this.apiApp = apiApp;

        this.setSpacing(8);
        this.search = new Button("Search");
        this.searchBar = new TextField("Type an artist, or any other search term.");
        this.artArray = new ArrayList<Art>();
        this.articArtArray = new ArrayList<Art>();

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
            "Title");

        dropDown.setValue("Keyword");
    } //initDropDown


    /**
     * Initializes the EventHandler for the app's search button.
     */
    public void initHandlers() {
        EventHandler<ActionEvent> searchHandler = (ActionEvent e) -> {
            String term = searchBar.getText().trim();
            String filter = dropDown.getValue();

            //if the app is currently on the artic scene, the app switches scenes and updates the
            //root scene's MetPane instead of the articScene's metPane.
            if (apiApp.stage.getScene() != apiApp.scene) {
                apiApp.articPane.resetPane();
                apiApp.metPane.searchBar.setText(term);
                apiApp.metPane.dropDown.setValue(filter);
                apiApp.switchScenes(true);
                apiApp.metPane.search.setDisable(true);
                HelperMethods.runNow( () -> apiApp.metPane.searchMet(term, filter));
            } else {
                search.setDisable(true);
                HelperMethods.runNow( () -> searchMet(term, filter));
            }
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
            MetResponse metResponse = gson.fromJson(jsonString, MetResponse.class);
            if (metResponse.objectIDs == null) {
                throw new IOException("The query did not return any results.");
            } //if

            double progressIncrement =
                HelperMethods.calculateIncrement(metResponse.objectIDs.length);
            int percentIncrement = 2;

            for (int i = 0; i < 50 && i < metResponse.objectIDs.length; i++) {
                loadMetArtInfo(metResponse.objectIDs[i]);

                //Updates the progress bar after each iteration of the loop.
                Platform.runLater(
                    HelperMethods.createRunnableForProgressPane(
                        progressIncrement, percentIncrement, apiApp)
                );
            } //for

            if (artArray.size() == 0) {
                throw new IOException("The query did not return any results."
                    + "Please try another search.");
            } //if

            //Adds the first image to the ArtPane's ImageView. Updates the ArtInfoPane's Label so
            //that it displays information about the artist and the piece.
            updateArtPane(artArray);

            //Updates the bottom buttons so that the previous piece and next piece buttons are
            //visible and enabled.
            Platform.runLater( () -> apiApp.bottomBtnBar.updateButtons(true, false, true));
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater( () -> {
                HelperMethods.throwAlert(e.getMessage());
            });
        } finally {
            Platform.runLater( () -> {
                apiApp.progressPane.resetProgress();
                search.setDisable(false);
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
            apiApp.artPane.getArtInfoPane().resetArtInfo();
            apiApp.artPane.getArtInfoPane().museumSearch.setVisible(false);
            apiApp.bottomBtnBar.resetButtons();
            apiApp.artPane.getArtView().setImage(null);
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

            //Checks that the image URL is not null or empty
            if (HelperMethods.ensureGoodImage(objResult.primaryImage) == true) {
                //Creates an art object to store this data.
                String title = objResult.title;
                String artistName = objResult.artistDisplayName;
                String department = objResult.department;
                String period = objResult.objectDate;
                String country = objResult.country;
                String imageUrl = objResult.primaryImage;
                Art artToAdd = new Art(
                    title, artistName, department, period, country, imageUrl);

                artArray.add(artToAdd);
            } //if
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater( () -> HelperMethods.throwAlert(e.getMessage()));
        } //try
    } //loadArtistNames



    /**
     * Queries the Art Institute of Chicago's collection and populates an array of Art objects based
     * on the results of the query.
     */
    public void searchArtic() {
        articArtArray.clear();

        try {
            String url = "https://api.artic.edu/api/v1/artworks/search";
            String term = apiApp.artPane.getDisplayedPiece().artist;
            String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
            String query = "?q=" + q +
                "&fields=id,title,artist_display,artist_title,gallery_title,place_of_origin,"
                + "date_display,image_id&limit=12";

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

            ArticResponse articResponse = gson
                .fromJson(jsonString, ArticResponse.class);

            for (int i = 0; i < 50 && i < articResponse.data.length; i++) {
                loadArticArtInfo(articResponse.data[i]);
            } //for

            Platform.runLater( () -> {
                apiApp.articPane.updateArt(articArtArray, 0);
            });
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater( () -> HelperMethods.throwAlert(e.getMessage()));
        } //try
    } //searchArtic



    /**
     * Takes in an ArticResult object from the query, creates an Art object from its data, and adds
     * the Art object to the articArt array.
     * @param articResult the ArticResult object to get the art data from.
     */
    public void loadArticArtInfo(ArticResult articResult) {
        try {
            String title = articResult.title;
            String artist = articResult.artistDisplay;
            String gallery = articResult.gallery;
            String country = articResult.country;
            String period = articResult.period;
            boolean isOnDisplay = articResult.isOnView;

            String imageUrl = "https://www.artic.edu/iiif/2/" + articResult.imageId +
                "/full/843,/0/default.jpg";
            Art artToAdd = new Art(title, artist, gallery, period, country, imageUrl, isOnDisplay);
            articArtArray.add(artToAdd);
        } catch (IllegalArgumentException e) {
            HelperMethods.throwAlert(e.getMessage());
        } //try
    } //loadArticArtInfo



    /**
     * Updates the GUI so it displays the first art piece whose image URL is not null. It then
     * displays the art's information (title, artist, time period, and department within the Met.
     *
     * @param artArray the Art arrayList to pull artist data from.
     */
    public void updateArtPane(ArrayList<Art> artArray) {
        Art artToDisplay = artArray.get(0);
        apiApp.artPane.setDisplayedPiece(artToDisplay);

        Platform.runLater( () -> {
            apiApp.artPane.getArtView().setImage(artToDisplay.image);

            apiApp.artPane.getArtInfoPane().updateArtInfo(artToDisplay);

            apiApp.artPane.getArtInfoPane().museumSearch.setVisible(true);

            apiApp.bottomBtnBar.nextBtn.setDisable(false);
        });
    } //updateArtPane


    /**
     * This is a helper method that creates a String for the artInfoPane's info Label.
     * Returns a String of the Art's information (title, artist, period, and department).
     *
     * @param art the Art object to retrieve information from.
     */
    public void updateArtInfo(Art art) {
        apiApp.artPane.getArtInfoPane().updateArtInfo(art);
    } //createArtInfo


    /**
     * Updates the ArtPane so that the next Art object in the artArray is displayed to the user.
     */
    public void nextArt() {
        try {
            apiApp.bottomBtnBar.previousBtn.setDisable(false);
            int index = DISPLAYED_ART_INDEX + 1;

            if (DISPLAYED_ART_INDEX < artArray.size()) {
                DISPLAYED_ART_INDEX += 1;
                if (DISPLAYED_ART_INDEX == artArray.size() - 1) {
                    apiApp.bottomBtnBar.nextBtn.setDisable(true);
                } else {
                    apiApp.bottomBtnBar.nextBtn.setDisable(false);
                } //if
            } //if

            Art artToUpdateTo = artArray.get(index);
            apiApp.artPane.getArtView().setImage(artToUpdateTo.image);
            apiApp.artPane.setDisplayedPiece(artToUpdateTo);
            updateArtInfo(artToUpdateTo);
        } catch (IndexOutOfBoundsException e) {
            String errMessage = "There are no more pieces to display.";
            HelperMethods.throwAlert(errMessage);
        } //try
    } //nextArt


    /**
     * Updates the ArtPane so that the previous Art object in the artArray is displayed to the user.
     */
    public void previousArt() {
        try {
            if (DISPLAYED_ART_INDEX > 0) {
                int index = DISPLAYED_ART_INDEX - 1;
                Art artToUpdateTo = artArray.get(index);

                apiApp.artPane.getArtView().setImage(artToUpdateTo.image);
                apiApp.artPane.setDisplayedPiece(artToUpdateTo);
                updateArtInfo(artToUpdateTo);

                DISPLAYED_ART_INDEX -= 1;
                if (DISPLAYED_ART_INDEX == 0) {
                    apiApp.bottomBtnBar.previousBtn.setDisable(true);
                } else {
                    apiApp.bottomBtnBar.previousBtn.setDisable(false);
                } //if
            } //if
        } catch (IndexOutOfBoundsException e) {
            String errMessage = "There is not another piece before this one.";
            HelperMethods.throwAlert(errMessage);
        } //try
    } //previousArt
} //MetPane
