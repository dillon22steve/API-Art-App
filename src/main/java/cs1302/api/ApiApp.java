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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.Builder;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.InputStream;
import java.io.IOException;

/**
 * This app allows the user to query the Metropolitan Museum of Art in New York City to find
 * artworks based on their search parameters. It then displays the art along with information about
 * the piece, including the title of the work, the name of the artist, the time period it was made,
 * and the museum department that it belongs to. It then allows the user to search for works by the
 * same artist at the Art Institute of Chicago (Artic). It uses the results from the Met query to
 * search Artic for works by the same artist and then switches scenes so that the Artic results are
 * displayed in a list format. The user can then click the view buttons to display the image of
 * their choosing as well as information about the image, such as whether or not it is currently on
 * display at Artic, the title, the artist, and the time period it was created in. The user can
 * return back to the Met museum results at any time.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene, articScene;
    VBox root, articRoot;

    // demonstrate how to load local asset using "file:resources/"
    Image bannerImage = new Image("file:resources/MetMuseum.jpg");
    ImageView banner = new ImageView(bannerImage);
    Label bannerInfo = new Label("The above image is the Metropolitan Museum of Art, found at "
        + "https://www.metmuseum.org/");

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
        this.root = new VBox();
        this.articRoot = new VBox();

        this.metPane = new MetPane(this);

        this.artPane = new ArtPane(this);

        this.bottomBtnBar = new BottomButtonBar();
        bottomBtnBar.apiApp = this;

        this.progressPane = new ProgressPane();

        this.articPane = new ArticPane(this);
        createArticScene();
    } // ApiApp


    /**
     * setup articScene that will be displayed to the user when they click the Search Art
     * Institute of Chicago button.
     */
    public void createArticScene() {
        // setup articScene that will be displayed to the user when they click the Search Art
        // Institute of Chicago button.
        articRoot.setSpacing(10);
        ImageView imgView = new ImageView(new Image("file:resources/ArtInstituteOfChicago.JPG"));
        Label imgInfo = new Label("The above image is the Art Institute of Chicago, found at "
            + "https://chicago.suntimes.com");
        imgInfo.setFont(new Font("Book Antiqua", 11));

        imgView.setPreserveRatio(true);
        imgView.setFitWidth(720);
        imgView.setFitHeight(300);

        articRoot.setMargin(imgView, new Insets(0, 150, 0, 150));
        articRoot.setAlignment(Pos.CENTER);
        articRoot.getChildren().addAll(imgView, imgInfo, new MetPane(this), articPane);
        articScene = new Scene(articRoot);
    } //createArticScene


    /**
     * Switches the app's scene to the main scene if the isMainScene value is true, switches to the
     * articScene otherwise.
     * @param isMainScene the boolean value that determines which scene the app will switch to.
     */
    public void switchScenes(boolean isMainScene) {
        if (isMainScene == true) {
            stage.setScene(scene);
            stage.show();
        } else {
            stage.setScene(articScene);
            stage.sizeToScene();
            stage.show();
        } //if
    } //switchScenes


    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        banner.setPreserveRatio(true);
        banner.setFitWidth(720);
        banner.setFitHeight(300);
        bannerInfo.setFont(new Font("Book Antiqua", 11));

        // setup scene
        root.setSpacing(10);
        root.setMargin(banner, new Insets(0, 20, 0, 20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(banner, bannerInfo, metPane, artPane, bottomBtnBar, progressPane);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("Art API App!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
