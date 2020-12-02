package com.example.model;

import com.example.model.Shapes.Shape;
import com.example.model.Shapes.ShapeFactory;

import java.util.HashMap;

public class Canvas {
    private ShapeFactory factory;
    private HashMap<Long, Shape> shapes;
    private HashMap<String, Shape> operationsList;
    private Shape currentShape;
    private static Canvas canvas;
    private long currentID = 0;

    private Canvas(){
        shapes = new HashMap<Long, Shape>();
        factory = new ShapeFactory();
    }
    public static Canvas getInstance(){
        if(canvas == null){
            canvas = new Canvas();
        }
        return canvas;
    }

    public HashMap<Long, Shape> getShapes() {
        return shapes;
    }

    public void drawShape(Shape shape)
    {
        Shape newShape = factory.createShape(shape,currentID);
        shapes.put(currentID, newShape);
        currentID++;
    }
    public void clearCanvas(){
        getShapes().clear();
        currentID = 0;
    }


}
