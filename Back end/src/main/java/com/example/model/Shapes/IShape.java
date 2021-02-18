package com.example.model.Shapes;

import java.util.HashMap;

public interface IShape {
    public void setID(long id);
public void move(String dimensions, long id) throws CloneNotSupportedException;
    public void copy(String dimensions, long id) throws CloneNotSupportedException;
    public void createShape();
    public void updateShape();
    public void deleteShape();
}
