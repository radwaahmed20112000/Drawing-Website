package com.example.controller;

import com.example.model.Canvas;
import com.example.model.Shapes.Shape;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Controller {
    Canvas myCanvas = Canvas.getInstance();
    @PostMapping("/shapes")
    public Shape drawShape(@RequestBody Shape shape) {
        System.out.println(shape);
        return shape;
    }
//    Canvas myCanvas = Canvas.getInstance();
//    @PostMapping("/shapes")
//    public String drawShape(@RequestBody String shape) {
//        System.out.println(shape);
//        return shape;
//    }

}
