package com.sourceit.task1.model;

/**
 * Created by User on 15.02.2016.
 */
public class BroadcasterModel {
    private String title;
    private Boolean state;

    public BroadcasterModel(String title, Boolean state) {
        this.title = title;
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getState() {
        return state;
    }
}
