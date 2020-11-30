package com.example.model;

import java.util.HashMap;

public interface IShape {
    HashMap<String, String> properties = null;
    public String id = null;
    public void setId(long id);
    public void setProperties(HashMap<String, String> properties);
    public void setDimensions(HashMap<String,String> dimensions);
    public void move(String x, String y);
    public void resize(String width, String height);
    public void color(String color);
    public void fillColor(String fillColor);
}
