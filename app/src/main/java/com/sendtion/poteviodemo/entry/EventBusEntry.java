package com.sendtion.poteviodemo.entry;

public class EventBusEntry {
    private String status;
    private int type;
    private String msg;
    private Object object;

    public EventBusEntry(String status) {
        this.status = status;
    }

    public EventBusEntry(String status, int type) {
        this.status = status;
        this.type = type;
    }

    public EventBusEntry(String status, int type, String msg) {
        this.status = status;
        this.type = type;
        this.msg = msg;
    }

    public EventBusEntry(String status, int type, String msg, Object object) {
        this.status = status;
        this.type = type;
        this.msg = msg;
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
