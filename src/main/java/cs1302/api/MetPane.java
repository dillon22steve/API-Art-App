package cs1302.api;

import cs1302.api.*;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.IOException;
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


public class MetPane extends HBox {

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


    public MetPane() {
        this.setSpacing(8);
        this.search = new Button("Search");
        this.searchBar = new TextField("Type an artist, or any other search term.");
        this.artArray = new ArrayList<Art>();

        initDropDown();
        initHandlers();

        HBox.setHgrow(searchBar, Priority.ALWAYS);
        this.getChildren().addAll(searchBar, dropDown, search);
    }


    public void initDropDown() {
        dropDown = new ComboBox<String>();
        dropDown.getItems().addAll(
            "Artist",
            "Country/Region",
            "Title");
    } //initDropDown


    public void initHandlers() {
        EventHandler<ActionEvent> searchHandler = (ActionEvent e) -> {
            String term = searchBar.getText().trim();
            searchMet(term);
            //searchArtic();
        };

        search.setOnAction(searchHandler);
    } //initHandlers



    public void searchMet(String term) {
        try {
            String url =
                "https://collectionapi.metmuseum.org/public/collection/v1/search?hasImages=true&";
            String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
            String query = "q=" + q;
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

            MetResponse metResponse = gson
                .fromJson(jsonString, MetResponse.class);


            System.out.println("Entering first for loop.");
            for (int i = 0; i < 50 && i < metResponse.objectIDs.length; i++) {
                loadMetArtInfo(metResponse.objectIDs[i]);
            } //for
            System.out.println("First for loop finished");

            updateArtPane();
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try

    } //searchForDepts



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

            String title = objResult.title;
            String artistName = objResult.artistDisplayName;
            String department = objResult.department;
            String period = objResult.period;
            String imageUrl = objResult.primaryImage;
            Art artToAdd = new Art(title, artistName, department, period, imageUrl);

            artArray.add(artToAdd);

        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
    } //loadArtistNames



    public void searchArtic() {
        try {
            String url = "https://api.artic.edu/api/v1/artworks/search";
            String q = URLEncoder.encode("river", StandardCharsets.UTF_8);
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


    public void updateArtPane() {

        //This loop finds the first image that is not null and displays it to the user, as well
        //as information about the artist.
        for (int i = 0; i < artArray.size(); i++) {
            if (artArray.get(i).image != null) {
                Art artToDisplay = artArray.get(i);
                apiApp.artPane.artView.setImage(artToDisplay.image);

                String title = "Title: " + artToDisplay.title;
                String artist = "Artist: " + artToDisplay.artist;
                String period = "Period: " + artToDisplay.period;
                String artInfo = title + "\n" + artist + "\n" + period;
                apiApp.artPane.info.setText(artInfo);

                break;
            }
        } //for
    } //updateArtPane
} //MetPane
