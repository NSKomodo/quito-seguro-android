package co.profapps.quitoseguro.model;

public class Report {
    private String offense;
    private long date;
    private double lat;
    private double lng;
    private String platform;

    public String getOffense() {
        return offense;
    }

    public void setOffense(String offense) {
        this.offense = offense;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
