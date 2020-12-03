package com.example.model.Shapes;

import java.util.HashMap;

public interface IShape {
    public void setID(long id);
//    public void setProperties(HashMap<String, String> properties);
//    public void setDimensions(HashMap<String,String> dimensions);
public void move(String dimensions, long id);
    public void copy(String dimensions, long id) throws CloneNotSupportedException;
//    public void resize(String width, String height);
//    public void color(String color);
//    public void fillColor(String fillColor);
    public void createShape();
    public void updateShape();
    public void deleteShape();
}
