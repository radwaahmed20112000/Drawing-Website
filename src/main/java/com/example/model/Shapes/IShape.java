package com.example.model.Shapes;

import java.util.HashMap;

public interface IShape {
    public void setId(long id);
    public void setProperties(HashMap<String, String> properties);
    public void setDimensions(HashMap<String,String> dimensions);
    public void move(String x, String y);
    public void resize(String width, String height);
    public void color(String color);
    public void fillColor(String fillColor);
}
