package com.example.androidproject.Map;

public class Events {

    private String eventType;
    private double longitude;
    private double latitude;
    private String description;
    private String capacity;
    private String eventName;
    private String eventAdminID;
    private String eventID;

    public Events() {
    }

    public Events(String eventType, String eventName, double longitude, double latitude, String description, String capacity, String eventAdminID, String eventID) {
        this.eventType = eventType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.eventName = eventName;
        this.description = description;
        this.capacity = capacity;
        this.eventAdminID = eventAdminID;
        this.eventID = eventID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventAdminID() {
        return eventAdminID;
    }

    public void setEventAdminID(String eventAdminID) {
        this.eventAdminID = eventAdminID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Events{" +
                "eventType='" + eventType + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}