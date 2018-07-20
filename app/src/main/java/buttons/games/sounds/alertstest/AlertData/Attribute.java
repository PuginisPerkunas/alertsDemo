
package buttons.games.sounds.alertstest.AlertData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attribute {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("default")
    @Expose
    private List<Object> _default = null;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;
    @SerializedName("description")
    @Expose
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getDefault() {
        return _default;
    }

    public void setDefault(List<Object> _default) {
        this._default = _default;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
