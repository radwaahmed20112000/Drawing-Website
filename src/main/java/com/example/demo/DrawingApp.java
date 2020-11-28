package com.example.demo;

import java.util.HashMap;
import java.util.LinkedList;

/*
    Singleton Class
 */

public class DrawingApp {

    private ShapeFactory shapeFactory;
    private LinkedList<Shape> shapes;
    private HashMap<Shape,String> operationsList;
    private Shape currentShape;
    private static DrawingApp drawingApp;

    private DrawingApp(){
        shapes = new LinkedList<Shape>();
    }

    public static DrawingApp getInstance(){
        if(drawingApp == null){
            drawingApp = new DrawingApp();
        }
        return drawingApp;
    }

    /*
        input: ShapeName (String), Properties (List) 'front-end'
        Creates new shape , Add its Properties .
        return shape
     */
    public void addShape(String shapeName , LinkedList properties){

    }
    public void removeShape(String shapeName , LinkedList properties){

    }


}
