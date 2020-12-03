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
        redo.push(removedShape);
        return removedShape;
    }


    public Shape redoShape(){
        if(redo.size()==0)
            return null;
        Shape removedShape=redo.pop();
        undo.push(removedShape);
        return removedShape;
    }

}
