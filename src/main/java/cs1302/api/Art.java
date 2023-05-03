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


/**
 * Art objects will be used to store data about each piece of art returned from a museum query.
 */
public class Art {

    public String title;
    public String artist;
    public String department;
    public String period;
    public String country;
    public String imageUrl;
    public Image image;

    /**
     * Constructs an Art object that will store the art's data. The constructor will also call the
     * fetchImage method which will create an Image object of the piece using the imageUrl.
     *
     * @param title the title of the piece.
     * @param artist the artist who created the piece.
     * @param department the department within the museum that the piece belongs to.
     * @param period the time period that the piece was created in.
     * @param country the country that the piece was created in.
     * @param imageUrl the url for the image that will be used to created an Image object of the
     */
    public Art(String title, String artist, String department, String period, String country,
        String imageUrl) {

        this.title = title;
        this.artist = artist;
        this.department = department;
        this.period = period;

        if (imageUrl != null && !(imageUrl.isEmpty())) {
            this.imageUrl = imageUrl;
            loadImage();
        } //if
    } //constructor



    /**
     * Creates an Art object for a Chicago Art Institute query.
     * @param title the title of the piece.
     * @param artist the artist who created the piece.
     * @param department the department within the museum that the piece belongs to.
     * @param period the time period that the piece was created in.
     * @param country the country that the piece was created in.
     * @param image an image of the piece.
     */
    public Art(String title, String artist, String department, String period, String country,
        Image image) {
        this.title = title;
        this.artist = artist;
        this.department = department;
        this.period = period;
        this.country = country;
        this.image = image;
    } //constructor



    /**
     * Creates an image object of the art using the imageUrl.
     */
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
