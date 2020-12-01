package com.example.model.Shapes.Polygons;

import com.example.model.Shapes.Shape;

import java.util.HashMap;

public class Polygons extends Shape {
    HashMap<String, String> properties;
    public String id;

    public Polygons( HashMap<String,String>  properties,  HashMap<String,String>  dimensions, String shapeType) {
        super(properties,dimensions,shapeType);
    }

}
