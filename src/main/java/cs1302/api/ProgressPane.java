package cs1302.api;


import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;


/**
 * A custom component that contains a progress bar showing the user the progress of the query.
 */
public class ProgressPane extends HBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public int progressPercent = 0;

    /**
     * Constructs a new ProgressPane object.
     */
    public ProgressPane() {
        progressBar = new ProgressBar(0);
        progressBar.setMinWidth(380);

        this.progressLabel = new Label("");

        this.setSpacing(8);
        this.setMargin(progressLabel, new Insets(5, 30, 5, 0));
        this.setMargin(progressBar, new Insets(5, 0, 5, 100));
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        this.getChildren().addAll(progressBar, progressLabel);
    } //constructor


    /**
     * Increases the progress of the progress bar by the specified increment, and increments the
     * progress percent label by the specified increment.
     *
     * @param progressIncrement the amount to increase the progress bar by.
     * @param percentIncrement the amount to increase the progress percent label by
     */
    public void setProgress(double progressIncrement, int percentIncrement) {
        progressBar.setProgress(progressBar.getProgress() + progressIncrement);

        progressPercent += percentIncrement;
        if (progressPercent > 100) {
            progressPercent = 100;
        } //if

        progressLabel.setText(("" + progressPercent + "% complete"));
    } //setProgress


    /**
     * Resets the progress bar to 0 percent and the progress label to 0 percent. Called after a
     * query is finished loading.
     */
    public void resetProgress() {
        progressBar.setProgress(0);
        progressPercent = 0;
        progressLabel.setText("");
    } //resetProgress
} //ProgressPane
