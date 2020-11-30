package com.example.controller;
import com.example.model.IShape;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class Controller {
    @PostMapping("/shapes")
    public HashMap<Long, IShape> getShapes() {
        return null;
    }


}
