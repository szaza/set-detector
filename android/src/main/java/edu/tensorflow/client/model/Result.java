package edu.tensorflow.client.model;

/**
 * Created by Zoltan Szabo on 3/13/18.
 */

public class Result {
    private String URL;
    private String description;

    public Result() {
        this(null, null);
    }

    public Result(String URL, String description) {
        this.URL = URL;
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
