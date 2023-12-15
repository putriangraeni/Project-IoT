package com.example.kuncilocker.model;

public class History {
    String key;
    String name;
    String device;
    long time;
    int type;

    public History(String key, String name, String device, long time, int type) {
        this.key = key;
        this.name = name;
        this.device = device;
        this.time = time;
        this.type = type;
    }

    public History() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
