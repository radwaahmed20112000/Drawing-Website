package com.example.model.Shapes;
import com.example.model.Shapes.EllipticalShapes.*;
import com.example.model.Shapes.Polygons.*;

public class ShapeFactory {
    public Shape createShape(Shape shape){
        if(shape.shapeType.equalsIgnoreCase("RECTANGLE"))
            shape = new Rectangle(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("CIRCLE"))
            shape = new Circle(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Ellipse"))
            shape = new Ellipse(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("LineSegment"))
            shape = new LineSegment(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Pentagon"))
            shape = new Pentagon(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Hexagon"))
            shape = new Hexagon(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Triangle"))
            shape = new Triangle(shape.JSONDimensions,shape.JSONProperties,shape.shapeType);
            return shape;
    }

}
