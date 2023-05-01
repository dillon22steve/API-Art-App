package cs1302.helpers;

import cs1302.api.*;

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
} //HelperMethods
