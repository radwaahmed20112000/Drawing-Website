package com.example.model;

import com.example.model.Shapes.Shape;

import java.util.Stack;

public class Undo {
    private Stack<Shape> undo;
    private Stack<Shape> redo;

    public Undo(){
        undo = new Stack<Shape>();
        redo = new Stack<Shape>();

    }

    public Stack<Shape> getUndo() {
        return undo;
    }

    public Stack<Shape> getRedo() {
        return redo;
    }
    public void addShape(Shape shape){
        undo.push(shape);
    }
    public Shape undoShape(){
        if(undo.size()==0)
            return null;
        Shape removedShape=undo.pop();
        if(removedShape.getShapeState().equalsIgnoreCase("AfterEdit")) {
            redo.push(removedShape);
            removedShape = undo.pop();

            redo.push(removedShape);
        }
        else{
            redo.push(removedShape);
        }


        System.out.println(removedShape.getShapeState());
        return removedShape;
    }
    public Shape redoShape(){
        if(redo.size()==0)
            return null;
        Shape removedShape=redo.pop();
        if(removedShape.getShapeState().equalsIgnoreCase("Edit")) {
            undo.push(removedShape);
            removedShape = redo.pop();

            undo.push(removedShape);
        }
        else{
            undo.push(removedShape);
        }


        return removedShape;
    }

/*
    public void pushRedo(Shape removedShape){
        removedShape=checkState(removedShape);
        redo.push(removedShape);

    }
    public void pushUndo(Shape removedShape){
        removedShape=checkState(removedShape);
        undo.push(removedShape);

    }

    public Shape checkState(Shape removedShape){
        if(removedShape.getShapeState().equalsIgnoreCase("created")){
            removedShape.setShapeState("deleted");
        }
        else{
            removedShape.setShapeState("created");
        }
        return removedShape;
    }

 */
}