package com.example.controller;

import com.example.model.Canvas;
import com.example.model.Shapes.EllipticalShapes.EllipticalShapes;
import com.example.model.Shapes.FreeDrawing.FreeDrawing;
import com.example.model.Shapes.Polygons.Polygons;
import com.example.model.Shapes.Shape;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
//    @CrossOrigin
//    @PostMapping("/sketch")
//    public FreeDrawing sketch(@RequestBody FreeDrawing sketch) {
//        myCanvas.drawShape(sketch);
//        return sketch;
//    }

    @CrossOrigin
    @RequestMapping("/copymove")
    public String CopyMove(@RequestParam(value = "dimensions") String dimension ,
                           @RequestParam(value = "id") String id,@RequestParam(value = "state")String state)
            throws CloneNotSupportedException, UnsupportedEncodingException {
        long idL = Long.parseLong(id);
        Shape shape = myCanvas.getShapes().get(idL);
        dimension = URLDecoder.decode(dimension, StandardCharsets.UTF_8.toString());
        if(state.equalsIgnoreCase("move")){
            shape.move(dimension,idL);
        }else if(state.equalsIgnoreCase("copy")){
            shape.copy(dimension,idL);
        }
    return "RECEIVE";
    }
    @CrossOrigin
    @RequestMapping("/shape")
    @ResponseBody
    public Map<Long, Shape> getAllShapes (){
        return myCanvas.getShapes();
    }

    @CrossOrigin
    @DeleteMapping("/shapes")
    public int deleteShape (){
        myCanvas.clearCanvas();
        return myCanvas.getShapes().size();
    }
    /*
    @CrossOrigin
    @DeleteMapping("/shape/{id}")
    public int deleteShape (@PathVariable Long id){
        myCanvas.getShapes().get(id).deleteShape();
        return myCanvas.getShapes().size();
    }*/

    @CrossOrigin
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "id") String id) {
        long idL = Long.parseLong(id);
        Shape shape = myCanvas.getShapes().get(idL);
        shape.deleteShape();
        System.out.println("BLABLA" + myCanvas.getShapes().size());
        return "RECEIVE";
    }

    @CrossOrigin
    @RequestMapping("/undo")
    @ResponseBody
    public Map<Long, Shape> undoCanvas (){
        myCanvas.undoCanvas();
        return myCanvas.getShapes();
    }
    @CrossOrigin
    @RequestMapping("/redo")
    @ResponseBody
    public Map<Long, Shape> redoCanvas (){
        myCanvas.redoCanvas();
        return myCanvas.getShapes();
    }

}
