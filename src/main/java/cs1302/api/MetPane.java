package cs1302.api;


import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
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
    private Button next;
    private Label departmentLabel;
    private ArrayList<Art> artArray;

    private final HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    private  Gson gson = new GsonBuilder()
         .setPrettyPrinting()
         .create();


    public MetPane() {
        this.search = new Button("Search");
        this.next = new Button("Next");
        this.departmentLabel = new Label("Click search to get a list of departments at the Met.");
        this.artArray = new ArrayList<Art>();

        initHandlers();

        HBox.setHgrow(departmentLabel, Priority.ALWAYS);
        this.getChildren().addAll(search, departmentLabel, next);
    }


    public void initHandlers() {
        EventHandler<ActionEvent> searchHandler = (ActionEvent e) -> {
            searchForDepts();
        };

        search.setOnAction(searchHandler);
    } //initHandlers


    public void searchForDepts() {
        System.out.println("Hello");

        try {
            String url = "https://collectionapi.metmuseum.org/public/collection/v1/search";
            String q = URLEncoder.encode("sunflowers", StandardCharsets.UTF_8);
            String query = "?q=" + q;
            url = url + query;
            URI location = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("status code 200");
                throw new IOException(response.toString());
            } //if


            String jsonString = response.body();

            System.out.println(jsonString);

            MetResponse metResponse = gson
                .fromJson(jsonString, MetResponse.class);


            for (int i = 0; i < metResponse.objectIDs.length; i++) {
                loadArtInfo(metResponse.objectIDs[i]);
            } //for


        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try

    } //searchForDepts



    public void loadArtInfo(int objectId) {
        try {
            String url = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"
                + objectId;
            URI location = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            String jsonString = response.body();
            ObjectResult objResult = gson
                .fromJson(jsonString, ObjectResult.class);

            String title = objResult.title;
            String artistName = objResult.artistDisplayName;
            String department = objResult.department;
            String period = objResult.period;

            artArray.add(new Art(title, artistName, department, period));
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
    } //loadArtistNames



    public void addToArrayList(String item, ArrayList<String> list) {
        if (item != null && !item.isEmpty()) {
            list.add(item);
            System.out.println(item);
        } //if
    } //addToArrayList
} //MetPane
