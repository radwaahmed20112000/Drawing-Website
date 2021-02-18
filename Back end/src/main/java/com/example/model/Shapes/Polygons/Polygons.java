package com.example.model.Shapes.Polygons;

import com.example.model.Shapes.Shape;

import java.util.HashMap;

public class Polygons extends Shape {


    public Polygons( HashMap<String,String>  properties,  HashMap<String,String>  dimensions, String shapeType) {
        super(properties,dimensions,shapeType);
    }

//    public void move(String start_x, String start_y, String end_x , String end_y) {
//        this.getJSONDimensions().replace("CenterX", start_x);
//        this.getJSONDimensions().replace("CenterY", start_y);
//        this.getJSONDimensions().replace("start_X", start_x);
//        this.getJSONDimensions().replace("start_Y", start_y);
//        this.getJSONDimensions().replace("end_X", end_x);
//        this.getJSONDimensions().replace("end_Y", end_y);
//    }
//    public void copy(String start_x, String start_y, String end_x , String end_y) throws CloneNotSupportedException {
//        Shape shape = (Shape) this.myCanvas.getShapes().get(this.getID()).clone();
//        shape.setID(myCanvas.getCurrentID());
//        myCanvas.setCurrentID();
//        shape.move(start_x,start_y,end_x,end_y);
//        myCanvas.getShapes().put(shape.getID(),shape);
//    }

}
