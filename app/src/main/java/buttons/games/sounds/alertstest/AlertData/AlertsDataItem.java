package buttons.games.sounds.alertstest.AlertData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertsDataItem {

    @SerializedName("devices")
    @Expose
    private List<Device> devices = null;
    @SerializedName("geofences")
    @Expose
    private List<Geofence> geofences = null;
    @SerializedName("types")
    @Expose
    private List<Type> types = null;
    @SerializedName("schedules")
    @Expose
    private List<Schedule> schedules = null;
    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;
    @SerializedName("alert_zones")
    @Expose
    private List<AlertZone> alertZones = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Geofence> getGeofences() {
        return geofences;
    }

    public void setGeofences(List<Geofence> geofences) {
        this.geofences = geofences;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<AlertZone> getAlertZones() {
        return alertZones;
    }

    public void setAlertZones(List<AlertZone> alertZones) {
        this.alertZones = alertZones;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
