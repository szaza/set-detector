package edu.tensorflow.client.model;

/**
 * Result model to store the results returned by the server.
 * Created by Zoltan Szabo on 3/13/18.
 */

public class Result {
    private String URL;
    private String title;
    private String description;

    public Result() {
        this(null, null, null);
    }

    public Result(final String URL, final String title, final String description) {
        this.URL = URL;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
