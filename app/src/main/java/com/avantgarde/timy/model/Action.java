package com.avantgarde.timy.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = "name",unique = true)})
public class Action {

    @PrimaryKey
    @NonNull
    private String name;
    private String category;
    private float averageDuration;

    @Ignore
    public Action() {
    }

    public Action(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(float averageDuration) {
        this.averageDuration = averageDuration;
    }

    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", averageDuration=" + averageDuration +
                '}';
    }
}
