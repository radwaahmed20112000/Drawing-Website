package com.example.controller;

import com.example.model.IShape;
import com.example.model.ShapeFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class DrawingApp {
    private ShapeFactory factory;
    private HashMap<Long, IShape> shapes;
    private HashMap<String, IShape> operationsList;
    private IShape currentShape;
    private static DrawingApp drawingApp;
    private long currentID = 0;
    private DrawingApp(){
        shapes = new HashMap<Long, IShape>();
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
    public void drawShape(String shape,String JSONDimensions ,String JSONProperties)
    {
        HashMap<String,String> properties = fromJsonToMap(JSONProperties);
        HashMap<String,String> dimensions = fromJsonToMap(JSONDimensions);
        IShape newShape = factory.createShape(shape,currentID,dimensions,properties);
       // newShape.setProperties(properties);
        shapes.put(currentID, newShape);
        currentID++;
    }

    public HashMap<String, String> fromJsonToMap(String JSONString)
    {
        Map<String, String> retMap = new Gson().fromJson(
                JSONString, new TypeToken<HashMap<String, String>>() {}.getType()
        );
        return (HashMap<String, String>) retMap;
    }
}
