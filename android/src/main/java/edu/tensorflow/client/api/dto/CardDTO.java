package edu.tensorflow.client.api.dto;

import java.io.Serializable;

/**
 * Data Transfer Objects for card.
 * Created by Zoltan Szabo on 3/8/18.
 */

public class CardDTO implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
