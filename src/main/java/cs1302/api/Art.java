package cs1302.api;

import javafx.scene.image.Image;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.Builder;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.InputStream;
import java.io.IOException;


public class Art {

    public String title;
    public String artist;
    public String department;
    public String period;
    public String imageUrl;
    public Image image;

    public Art(String title, String artist, String department, String period, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.department = department;
        this.period = period;

        if (imageUrl != null && !(imageUrl.isEmpty())) {
            this.imageUrl = imageUrl;
            loadImage();
        } //if
    } //constructor



    private void loadImage() {
        try {
            URI location = URI.create(this.imageUrl);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();
            HttpResponse<InputStream> response = MetPane.HTTP_CLIENT.send(request,
                BodyHandlers.ofInputStream());

            InputStream imageStream = response.body();
            image = new Image(imageStream);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
    } //loadImage
} //Art
