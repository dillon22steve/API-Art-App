package cs1302.artTest;


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


public class MetPane extends HBox {

    private Button search;
    private Button next;
    private Label departmentLabel;
    private ArrayList<Object> departments;

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
        departments = new ArrayList<Object>();

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
            String url = "https://collectionapi.metmuseum.org/public/collection/v1/departments";
            URI location = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            String jsonString = response.body();

            MetResponse metResponse = gson
                .fromJson(jsonString, MetResponse.class);

            for (int i = 0; i < metResponse.results.length; i++) {
                departments.add(metResponse.results[i]);
            } //for

            //departmentLabel.setText(metResponse.resultCount);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {

        } //try

    } //searchForDepts
} //MetPane
