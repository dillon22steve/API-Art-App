package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.Builder;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.InputStream;
import java.io.IOException;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene, articScene;
    VBox root, articRoot;
    // demonstrate how to load local asset using "file:resources/"
    Image bannerImage = new Image("file:resources/readme-banner.png");
    ImageView banner = new ImageView(bannerImage);

    MetPane metPane;
    ArtPane artPane;
    BottomButtonBar bottomBtnBar;
    ArticPane articPane;
    public ProgressPane progressPane;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        articRoot = new VBox();

        metPane = new MetPane();
        metPane.apiApp = this;

        artPane = new ArtPane(this);

        bottomBtnBar = new BottomButtonBar();
        bottomBtnBar.apiApp = this;

        progressPane = new ProgressPane();

        articPane = new ArticPane();
        createArticScene();
    } // ApiApp


    /**
     * Loads an image of the Met Museum in New York City to be displayed at the top of the app.
     * @return an image of the Met Museum in New York City to be displayed at the top of the app.
     */
    public Image loadMetImg() {
        try {
            String url = "https://www.metmuseum.org/-/media/images/visit/plan-your-visit/individual"
                + "-locations/fifth-avenue/fifthave_1520x1520.jpg?as=1&mh=3040&mw=3040&sc_"
                + "lang=en&hash=BFF78F8B47EA58D354A9FE842B717611";

            URI location = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(location)
                .build();

            HttpResponse<InputStream> response = MetPane.HTTP_CLIENT.send(request,
                BodyHandlers.ofInputStream());

            InputStream imageStream = response.body();
            Image metMuseum = new Image(imageStream);
            return metMuseum;
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } //try
        return new Image("file:resources/readme-banner.png");
    } //loadMetImg


    /**
     * setup articScene that will be displayed to the user when they click the Search Art
     * Institute of Chicago button.
     */
    public void createArticScene() {
        // setup articScene that will be displayed to the user when they click the Search Art
        // Institute of Chicago button.
        articRoot.setSpacing(10);
        articRoot.getChildren().addAll(banner, metPane, articPane, progressPane);
        articScene = new Scene(articRoot);
    } //createArticScene


    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        //Image metMuseum = loadMetImg();
        //ImageView banner = new ImageView(metMuseum);
        banner.setPreserveRatio(true);
        banner.setFitWidth(720);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        root.setSpacing(10);
        root.getChildren().addAll(banner, metPane, artPane, bottomBtnBar, progressPane);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
