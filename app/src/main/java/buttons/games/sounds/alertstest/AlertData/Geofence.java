package buttons.games.sounds.alertstest.AlertData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geofence {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("title")
    @Expose
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
