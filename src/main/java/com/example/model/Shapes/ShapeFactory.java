package com.example.model.Shapes;
import com.example.model.Shapes.EllipticalShapes.*;
import com.example.model.Shapes.Polygons.*;

public class ShapeFactory {
    public Shape createShape(Shape shape,long ID){
        System.out.println(shape.getJSONDimensions());
        if(shape.shapeType.equalsIgnoreCase("RECTANGLE"))
            shape = new Rectangle(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("CIRCLE"))
            shape = new Circle(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Ellipse"))
            shape = new Ellipse(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("LineSegment"))
            shape = new LineSegment(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Pentagon"))
            shape = new Pentagon(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Hexagon"))
            shape = new Hexagon(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        else if(shape.shapeType.equalsIgnoreCase("Triangle"))
            shape = new Triangle(shape.JSONProperties,shape.JSONDimensions,shape.shapeType);
        shape.setID(ID);
        shape.setShapeState("created");
        return shape;
    }

}
