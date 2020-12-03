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
        addToUndoStack(newShape);
        currentID++;
    }
    public void clearCanvas(){
        getShapes().clear();
        currentID = 0;
    }


    public void addToUndoStack(Shape newShape){
        myUndo.addShape(newShape);
    }
    public void undoCanvas(){
        Shape removedShape= myUndo.undoShape();
        //System.out.println(removedShape.getShapeState()+"ttttt");
        if(removedShape!=null){
            if(removedShape.getShapeState().equalsIgnoreCase("created")){
                shapes.remove(removedShape.getID());
                currentID--;

            }
            else if(removedShape.getShapeState().equalsIgnoreCase("deleted")){
                //removedShape.setID(currentID);
                shapes.put(removedShape.getID(), removedShape);
                //currentID++;
            }
            else if(removedShape.getShapeState().equalsIgnoreCase("Edit")){
                shapes.put(removedShape.getID(),removedShape);
            }
        }
    }


    public void redoCanvas(){
        Shape removedShape= myUndo.redoShape();
       // System.out.println(removedShape.getShapeState());

        if(removedShape!=null) {
            if(removedShape.getShapeState().equalsIgnoreCase("deleted")){
                shapes.remove(removedShape.getID());
                currentID--;
            }
            else if(removedShape.getShapeState().equalsIgnoreCase("created")){
                removedShape.setID(currentID);
                shapes.put(currentID, removedShape);
                currentID++;
            }
            else if(removedShape.getShapeState().equalsIgnoreCase("AfterEdit")){
                shapes.put(removedShape.getID(),removedShape);
            }
        }
    }

    public void undoDeletion(Shape shape) throws CloneNotSupportedException {
        Shape newShape=(Shape) shape.clone();
        newShape.setShapeState("deleted");
        myUndo.addShape(newShape);
        System.out.println(myUndo.getUndo().size());


    }


}
