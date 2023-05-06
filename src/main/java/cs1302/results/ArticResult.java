package cs1302.api;

import com.google.gson.annotations.SerializedName;


/**
 * The fields that will be populated when an Artic query is completed.
 */
public class ArticResult {

    public int id;
    public String title;
    public @SerializedName("artist_display") String artistDisplay;
    public @SerializedName("artist_title") String artistName;
    public @SerializedName("gallery_title") String gallery;
    public @SerializedName("place_of_origin") String country;
    public @SerializedName("date_display") String period;
    public @SerializedName("image_id") String imageId;
    public @SerializedName("is_on_view") boolean isOnView;

} //ArticResult
