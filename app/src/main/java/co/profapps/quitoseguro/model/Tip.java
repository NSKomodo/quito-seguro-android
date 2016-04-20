package co.profapps.quitoseguro.model;

import java.io.Serializable;

public class Tip implements Serializable {
    private String titleEN;
    private String titleES;
    private String summaryEN;
    private String summaryES;
    private String textEN;
    private String textES;
    private String thumbnail;
    private String header;

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getTitleES() {
        return titleES;
    }

    public void setTitleES(String titleES) {
        this.titleES = titleES;
    }

    public String getSummaryEN() {
        return summaryEN;
    }

    public void setSummaryEN(String summaryEN) {
        this.summaryEN = summaryEN;
    }

    public String getSummaryES() {
        return summaryES;
    }

    public void setSummaryES(String summaryES) {
        this.summaryES = summaryES;
    }

    public String getTextEN() {
        return textEN;
    }

    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }

    public String getTextES() {
        return textES;
    }

    public void setTextES(String textES) {
        this.textES = textES;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
