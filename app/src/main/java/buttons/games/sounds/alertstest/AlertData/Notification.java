package buttons.games.sounds.alertstest.AlertData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("input")
    @Expose
    private String input = "-";
    @SerializedName("description")
    @Expose
    private String description;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        if(input == null){
            this.input = input;
        }
        else {
            this.input = "-";
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
