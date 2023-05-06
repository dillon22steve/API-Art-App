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
        this.root = new VBox();
        this.articRoot = new VBox();

        this.metPane = new MetPane();
        this.metPane.apiApp = this;

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
        ImageView imgView = new ImageView(new Image("file:resources/readme-banner.png"));
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(720);
        articRoot.getChildren().addAll(imgView, new MetPane(), articPane);
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
