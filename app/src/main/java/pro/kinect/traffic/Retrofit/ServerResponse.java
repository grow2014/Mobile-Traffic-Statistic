package pro.kinect.traffic.Retrofit;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pro.kinect.traffic.Main.App;
import pro.kinect.traffic.Models.AppItem;
import retrofit2.Response;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class ServerResponse {

    @SerializedName("added_entries")
    private List<IdEntries> idEntriesList;
    public List<IdEntries> getIdEntriesList() {
        return idEntriesList;
    }

    private class IdEntries {
        @SerializedName("id")
        private String id;
        public String getId() {
            return id;
        }
    }



    public static void parse(Response<ServerResponse> response) {
        if (response != null && response.body() != null) {
            List<IdEntries> idEntriesList = response.body().getIdEntriesList();
            if (idEntriesList != null && idEntriesList.size() > 0) {
                for (IdEntries entry : idEntriesList) {
                    AppItem.clearItem(entry.getId());
                }
            }
        }
    }
}
