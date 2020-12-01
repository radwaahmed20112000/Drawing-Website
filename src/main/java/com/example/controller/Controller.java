package com.example.controller;

import com.example.model.Canvas;
import com.example.model.Shapes.Shape;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class Controller {
    Canvas myCanvas = Canvas.getInstance();
    @PostMapping("/shapes")
    public Shape drawShape(@RequestBody Shape shape) {
        myCanvas.drawShape(shape);
        return shape;
    }

    @RequestMapping("/shapes")
    @ResponseBody
    public Map<Long, Shape> getAllShapes (){
        return myCanvas.getShapes();
}
}
