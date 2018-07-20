package buttons.games.sounds.alertstest.AlertData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Type {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return  title;
    }
}
