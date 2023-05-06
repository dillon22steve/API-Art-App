package cs1302.helpers;

import cs1302.api.*;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

/**
 * This class will be used to make other classes shorter. It will contain static helper methods that
 * other classes will use.
 */
public class HelperMethods {

    /**
     * Creates a new thread using a Runnable provided in the parameters.
     * @param target a runnable used to create a thread.
     */
    public static void runNow(Runnable target) {
        Thread thread = new Thread(target);
        thread.setDaemon(true);
        thread.start();
    } //runNow


    /**
     * Creates a runnable that can be used to create a new Thread object.
     *
     * @param progressIncrement the amount to increment the progress bar.
     * @param percentIncrement the amount to increment the percent Label.
     * @param apiApp the apiApp object to update.
     *
     * @return a Runnable that can be used to create a Thread object.
     */
    public static Runnable createRunnableForProgressPane(
        double progressIncrement, int percentIncrement, ApiApp apiApp) {

        return ( () -> {
            apiApp.progressPane.setProgress(progressIncrement, percentIncrement);
        });
    } //createRunnableForProgressPane


    /**
     * Updates the disable and visible properties of the input button.
     * @param btn1 a button to be updated.
     * @param disable the boolean value to update the buttons' disable property to.
     * @param visible the boolean value to update the buttons' visible property to.
     */
    public static void updateButton(Button btn1, boolean disable, boolean visible) {
        btn1.setDisable(disable);
        btn1.setVisible(visible);
    } //updateButton


    /**
     * Updates the disable and visible properties of the input buttons.
     * @param btn1 a button to be updated.
     * @param btn2 a button to be updated.
     * @param disable the boolean value to update the buttons' disable property to.
     * @param visible the boolean value to update the buttons' visible property to.
     */
    public static void updateButtons(Button btn1, Button btn2, boolean disable, boolean visible) {
        btn1.setDisable(disable);
        btn1.setVisible(visible);
        btn2.setDisable(disable);
        btn2.setVisible(visible);
    } //updateButtons


    /**
     * Updates the disable and visible properties of the input buttons.
     * @param btn1 a button to be updated.
     * @param btn2 a button to be updated.
     * @param btn3 a button to be updated.
     * @param btn4 a button to be updated.
     * @param disable the boolean value to update the buttons' disable property to.
     * @param visible the boolean value to update the buttons' visible property to.
     */
    public static void updateButtons(Button btn1, Button btn2, Button btn3, Button btn4,
        boolean disable, boolean visible) {
        btn1.setDisable(disable);
        btn1.setVisible(visible);
        btn2.setDisable(disable);
        btn2.setVisible(visible);
        btn3.setDisable(disable);
        btn3.setVisible(visible);
        btn4.setDisable(disable);
        btn4.setVisible(visible);
    } //updateButtons


    /**
     * Creates an Alert object and displays it to the user so that they can no longer use the app
     * until they close the Alert box.
     * @param errMessage the error message to be displayed to the user.
     */
    public static void throwAlert(String errMessage) {
        Alert alert = new Alert(AlertType.ERROR, "Error: " + errMessage);
        alert.showAndWait();
    } //e


    /**
     * Calculates the increment that the progress bar should be incremented by. If the query results
     * more than 50 results, this method returns 1 / 50. If there are less than 50 results, this
     * method returns 1 / the number of results.
     *
     * @param resultSize the number of results from the query.
     * @return the amount that the progress bar should be incremented by when parsing a query.
     */
    public static double calculateIncrement(int resultSize) {
        if (resultSize >= 50) {
            return (1.0 / 50.0);
        } else {
            return (1.0 / resultSize);
        } //if
    } //calculateIncrement


    /**
     * Creates a color that will be used to update the artInfo labels in the ArticPane. If the art
     * currently on display at the museum, a green color will be returned. If it is not currently on
     * display, a red color will be returned.
     * @param isOnDisplay the text that will be used to determine the color that is returned.
     * @return returns a green color if the art is currently on display, a red color if it is
     * not currently on display.
     */
    public static Color createTextFillColor(String isOnDisplay) {
        if (isOnDisplay.equals("Currently on display")) {
            return Color.rgb(0, 204, 0);
        } else {
            return Color.rgb(230, 0, 0);
        } //if
    } //createTextFillColor


    /**
     * Returns {@code true} if the image URL is not null or empty, false otherwise.
     * @param imageUrl the URL to check.
     * @return returns {@code true} if the image URL is not null or empty, false otherwise.
     */
    public static boolean ensureGoodImage(String imageUrl) {
        if (imageUrl != null) {
            if (!(imageUrl.isEmpty())) {
                return true;
            } //if
        }

        return false;
    } //ensureGoodImage
} //HelperMethods
