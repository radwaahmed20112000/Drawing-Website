package com.example.model.Shapes;

public class testShape {
    String properties;
    int haha;
    int lolo;
    public testShape(int haha,int lolo){
        this.haha =haha;
        this.lolo = lolo;
    }
    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    String dimensions;
    String shapeType;
}
