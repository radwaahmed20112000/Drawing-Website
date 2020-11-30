package com.example.model;
import com.example.model.EllipticalShapes.*;
import com.example.model.Polygons.*;

import java.util.HashMap;

public class ShapeFactory {
    public IShape createShape(String shapeName,long ID,HashMap<String,String> dimensions,HashMap<String,String> properties){
        IShape shape = null;
        if(shapeName.equalsIgnoreCase("RECTANGLE"))
            shape = new Rectangle();
        else if(shapeName.equalsIgnoreCase("CIRCLE"))
            shape = new Circle();
        else if(shapeName.equalsIgnoreCase("Ellipse"))
            shape = new Ellipse();
        else if(shapeName.equalsIgnoreCase("LineSegment"))
            shape = new LineSegment();
        else if(shapeName.equalsIgnoreCase("Pentagon"))
            shape = new Pentagon();
        else if(shapeName.equalsIgnoreCase("Hexagon"))
            shape = new Hexagon();
        else if(shapeName.equalsIgnoreCase("Triangle"))
            shape = new Triangle();

        assert shape != null;
        shape.setDimensions(dimensions);
        shape.setProperties(properties);
            return shape;
    }

}
