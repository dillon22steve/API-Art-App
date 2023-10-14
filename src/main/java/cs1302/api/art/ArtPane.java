package cs1302.api.art;


import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import cs1302.api.ApiApp;
import javafx.geometry.Insets;


/**
 * A custom component that contains an ImageView object that will display art results after the user
 * clicks search, as well as an ArtInfoPane that will display information about the art to the user.
 */
public class ArtPane extends HBox {

    ImageView artView;
    ArtInfoPane artInfoPane;
    Art displayedPiece;
    ApiApp apiApp;

    /**
     * Constructs an ArtPane object.
     * @param apiApp the application that this pane belongs to.
     */
    public ArtPane(ApiApp apiApp) {
        this.setSpacing(30);

        this.apiApp = apiApp;

        initArtView();

        artInfoPane = new ArtInfoPane(apiApp);
        artInfoPane.apiApp = this.apiApp;

        addToPane();
    } //constructor


    /**
     * Initializes the ImageView that will display the art piece to the user.
     */
    public void initArtView() {
        this.artView = new ImageView();
        this.artView.setFitWidth(300);
        this.artView.setFitHeight(300);
        this.artView.setPreserveRatio(true);
    } //initArtView


    /**
     * Adds the ImageView and ArtInfoPane to this ArtPane object.
     */
    public void addToPane() {
        this.setMargin(artView, new Insets(0, 0, 0, 5));
        this.getChildren().addAll(artView, artInfoPane);
    } //addToPane


    /**
     * Sets the displayedPiece to the value of the piece that is currently displayed in the artView.
     * @param piece the Art object to assign to the displayedPiece variable.
     */
    public void setDisplayedPiece(Art piece) {
        this.displayedPiece = piece;
    } //setDisplayedPiece


    /**
     * Returns the piece currently displayed.
     * @return the piece currently displayed.
     */
    public Art getDisplayedPiece() {
        return displayedPiece;
    } //getDisplayedPiece


    /**
     * Returns the artView object for this pane.
     * @return the artView object for this pane.
     */
    public ImageView getArtView() {
        return artView;
    } //getArtView


    /**
     * Sets the artView object for this pane.
     * @param artView the artView object to set for this pane.
     */
    public void setArtView(ImageView artView) {
        this.artView = artView;
    } //setArtView


    /**
     * Returns the artInfoPane object for this pane.
     * @return the artInfoPane object for this pane.
     */
    public ArtInfoPane getArtInfoPane() {
        return artInfoPane;
    } //getArtInfoPane


    /**
     * Sets the artInfoPane object for this pane.
     * @param artInfoPane the artInfoPane object to set for this pane.
     */
    public void setArtInfoPane(ArtInfoPane artInfoPane) {
        this.artInfoPane = artInfoPane;
    } //setArtInfoPane

} //ArtPane
