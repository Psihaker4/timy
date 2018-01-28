package com.avantgarde.timy.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(indices = {@Index(value = "id",unique = true)})
public class Dash {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;
    private String category;
    private float duration;
    private boolean completed;

    private Date startTime;
    private Date endTime;

    @Ignore
    private Action action;
    @Ignore
    private Category cat;

    @Ignore
    public Dash() {
    }

    public Dash(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getDuration() {
        return duration;
    }

    public float getDuration(Date end) {
        calcDuration(end);
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
        if(endTime == null) return;
        calcDuration();
        setCompleted(true);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Category getCat() {
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

    private void calcDuration() {
        calcDuration(endTime);
    }

    private void calcDuration(Date end) {
        duration = (end.getTime() - startTime.getTime()) / 60000;
    }

    private String getStringDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        return (h > 9 ? "" : "0") + h + ":" + (m > 9 ? "" : "0") + m;
    }

    public String getStartStringDate() {
        return getStringDate(startTime);
    }

    public String getEndStringDate() {
        return getStringDate(endTime);
    }

    public String getStringDuration() {
        return ((int) duration / 60) + "h " + ((int) duration % 60) + "m";
    }

    public String getStringDuration(Date end) {
        calcDuration(end);
        return getStringDuration();
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("action", name);
        map.put("category", category);
        if (duration != 0)
            map.put("duration", duration);
        map.put("completed", completed);
        map.put("startTime", startTime);
        if (endTime != null)
            map.put("endTime", endTime);

        return map;
    }

    @Override
    public String toString() {
        return "Dash{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", duration=" + duration +
                ", completed=" + completed +
                '}';

    }
}
