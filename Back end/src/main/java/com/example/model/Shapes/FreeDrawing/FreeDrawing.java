package com.example.model.Shapes.FreeDrawing;

import com.example.model.Shapes.Shape;

import java.util.HashMap;

public class FreeDrawing extends Shape {
//    public double[] getSketch() {
//        return sketch;
//    }
//
//    public void setSketch(double[] sketch) {
//        this.sketch = sketch;
//    }
//
//    double[] sketch ;

    public FreeDrawing(HashMap<String, String> JSONProperties, HashMap<String, String> JSONDimensions, String shapeType) {
        super(JSONProperties, JSONDimensions, shapeType);
    }
}
