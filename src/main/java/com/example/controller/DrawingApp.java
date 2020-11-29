package com.example.controller;

import com.example.model.Shape;
import com.example.model.ShapeFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DrawingApp {
    private ShapeFactory factory;
    private HashMap<String, Shape> shapes;
    private HashMap<String, Shape> operationsList;
    private Shape currentShape;
    private static DrawingApp drawingApp;

    private DrawingApp(){
        shapes = new HashMap<String, Shape>();
        factory = new ShapeFactory();
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
    public void drawShape(String shape, String id, String JSONproperties)
    {
        HashMap<String,String> properties = fromJsonToMap(JSONproperties);
        Shape newShape = factory.createShape(shape, id);
        newShape.setProperties(properties);
        shapes.put(id, newShape);
    }

    public HashMap<String, String> fromJsonToMap(String JSONproperties)
    {
        Map<String, String> retMap = new Gson().fromJson(
                JSONproperties, new TypeToken<HashMap<String, String>>() {}.getType()
        );
        return (HashMap<String, String>) retMap;
    }
}
