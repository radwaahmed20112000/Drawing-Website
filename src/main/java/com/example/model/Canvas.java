package com.example.model;

import com.example.model.Shapes.Shape;
import com.example.model.Shapes.ShapeFactory;

import java.util.HashMap;

public class Canvas {
    private ShapeFactory factory;
    private HashMap<Long, Shape> shapes;
    private HashMap<String, Shape> operationsList;
    Undo myUndo;
    private Shape currentShape;
    private static Canvas canvas;
    private long currentID = 0;

    public long getCurrentID() {
        return currentID;
    }

    public void setCurrentID() {
        this.currentID = currentID + 1;
    }

    private Canvas(){
        shapes = new HashMap<Long, Shape>();
        factory = new ShapeFactory();
        myUndo = new Undo();

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
        myUndo.addShape(newShape);
        System.out.println(myUndo.getUndo().size());
        currentID++;
    }
    public void clearCanvas(){
        getShapes().clear();
        currentID = 0;
    }

    public void undoCanvas(){

       Shape removedShape= myUndo.undoShape();
       if(removedShape!=null){
           shapes.remove(removedShape.getID());
           currentID--;
       }


    }

    public void redoCanvas(){

        Shape removedShape= myUndo.redoShape();
        if(removedShape!=null) {
            removedShape.setID(currentID);
            shapes.put(currentID, removedShape);
            currentID++;
        }

    }


}
