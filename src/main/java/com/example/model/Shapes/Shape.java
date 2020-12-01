package com.example.model.Shapes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class Shape {
    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
        this.JSONProperties = fromJsonToMap(properties);
    }

    public String getDimensions() {
        return dimensions;
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

    String dimensions;
    String shapeType;
    HashMap<String,String> JSONDimensions;
    HashMap<String,String> JSONProperties;
     String properties;

    public Shape( HashMap<String,String>  JSONProperties,  HashMap<String,String>  JSONDimensions, String shapeType) {
        this.JSONProperties = JSONProperties;
        this.JSONDimensions = JSONDimensions;
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
