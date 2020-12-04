package com.example.model.Shapes;

import com.example.model.Canvas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Shape implements IShape,Cloneable, Serializable {
    String dimensions;
    String shapeType;
    String shapeState;
    HashMap<String,String> JSONDimensions;
    HashMap<String,String> JSONProperties;
    String properties;
    protected Canvas myCanvas = Canvas.getInstance();

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setShapeState(String shapeState) {
        this.shapeState = shapeState;
    }

    public String getShapeState() {
        return shapeState;
    }


    @Override
    public void move(String dimensions,long id) throws CloneNotSupportedException {
        System.out.println("oldddd"+this.myCanvas.getShapes().get(id).getJSONDimensions().toString());
        Shape shape = this.myCanvas.getShapes().get(id);
        Shape movedShape=(Shape)shape.clone();
        Shape newShape=(Shape)shape.clone();
        newShape.setShapeState("Edit");
        myCanvas.addToUndoStack(newShape);
        HashMap<String,String> dimension = this.fromJsonToMap(dimensions);
        movedShape.setJSONDimensions(dimension);
        newShape=(Shape)movedShape.clone();
        newShape.setShapeState("AfterEdit");
        myCanvas.addToUndoStack(newShape);
        myCanvas.getShapes().put(movedShape.getID(),movedShape);
        System.out.println("newwwww"+this.myCanvas.getShapes().get(id).getJSONDimensions().toString());

    }

    @Override
    public void copy(String dimensions, long id) throws CloneNotSupportedException {
        Shape shape = (Shape) this.myCanvas.getShapes().get(id).clone();
        shape.setID(myCanvas.getCurrentID());
        myCanvas.setCurrentID();
        HashMap<String, String> dimension = this.fromJsonToMap(dimensions);
        shape.setJSONDimensions(dimension);
        myCanvas.getShapes().put(shape.getID(), shape);
        myCanvas.addToUndoStack(shape);
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    @Override
    public void createShape() {

    }

    @Override
    public void updateShape() {
        //undo n redo
        myCanvas.getShapes().put(ID,this);
    }

    @Override
    public void deleteShape() {
    myCanvas.getShapes().remove(ID);
    }

    public void setJSONDimensions(HashMap<String, String> JSONDimensions) {
        this.JSONDimensions = JSONDimensions;
    }

    public void setJSONProperties(HashMap<String, String> JSONProperties) {
        this.JSONProperties = JSONProperties;
    }

    long ID;

    public Shape( HashMap<String,String>  JSONProperties,  HashMap<String,String>  JSONDimensions, String shapeType) {
        this.JSONProperties = JSONProperties;
        this.JSONDimensions = JSONDimensions;
        this.shapeType = shapeType;
    }

    public HashMap<String, String> getJSONProperties() {
        return JSONProperties;
    }

    public HashMap<String, String> getJSONDimensions() {
        return JSONDimensions;
    }


    public void setProperties(String properties) {
        this.properties = properties;
        this.JSONProperties = fromJsonToMap(properties);
    }


    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
        this.JSONDimensions = fromJsonToMap(dimensions);
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }


    private HashMap<String, String> fromJsonToMap(String JSONString)
    {
        Map<String, String> retMap = new Gson().fromJson(
                JSONString, new TypeToken<HashMap<String, String>>() {}.getType()
        );
        return (HashMap<String, String>) retMap;
    }
}
