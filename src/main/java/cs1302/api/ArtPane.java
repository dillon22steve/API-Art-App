package cs1302.api;


import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


public class ArtPane extends HBox {

    ImageView artView;
    Button next;
    Label info;

    public ArtPane() {
        this.setSpacing(30);
        initArtView();

        this.info = new Label();
        info.setWrapText(true);

        addToPane();
    } //constructor


    public void initArtView() {
        this.artView = new ImageView();
        this.artView.setFitWidth(300);
        this.artView.setFitHeight(300);
        this.artView.setPreserveRatio(true);
    } //initArtView


    public void addToPane() {
        this.getChildren().addAll(artView, info);
    } //addToPane

} //ArtPane
