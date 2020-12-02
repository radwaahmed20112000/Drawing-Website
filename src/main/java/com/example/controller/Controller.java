package com.example.controller;

import com.example.model.Canvas;
import com.example.model.Shapes.Shape;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;

@Configuration
class MyConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}

@RestController
public class Controller {
    Canvas myCanvas = Canvas.getInstance();
    @CrossOrigin
    @PostMapping("/shape")
    public Shape drawShape(@RequestBody Shape shape) {
        myCanvas.drawShape(shape);
        return shape;
    }
    @CrossOrigin
    @RequestMapping("/shape")
    @ResponseBody
    public Map<Long, Shape> getAllShapes (){
        return myCanvas.getShapes();
    }
    @CrossOrigin
    @PutMapping("/shape")
    public Shape updateShape(@RequestBody Shape shape){
        shape.updateShape();
        return shape;
    }

    @CrossOrigin
    @DeleteMapping("/shapes")
    public int deleteShape (){
        myCanvas.clearCanvas();
        return myCanvas.getShapes().size();
    }


}
