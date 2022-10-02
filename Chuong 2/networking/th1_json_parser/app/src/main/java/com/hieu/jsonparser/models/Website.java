package com.hieu.jsonparser.models;

import java.util.List;

import com.google.gson.Gson;

public class Website {
    private int id;
    private String name;
    private List<String> websites;
    private Address address;

    public static Website fromJson(String json) {
        return new Gson().fromJson(json, Website.class);
    }
    public String toJson() {
        return new Gson().toJson(this);
    }
}
