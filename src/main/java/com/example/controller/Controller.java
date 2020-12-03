package com.example.controller;

import com.example.model.Canvas;
import com.example.model.Shapes.EllipticalShapes.EllipticalShapes;
import com.example.model.Shapes.Polygons.Polygons;
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
    @PostMapping("/copymove")
    public void CopyMove(@RequestParam(value = "dimensions") String dimension ,@RequestParam(value = "id") String id,@RequestParam(value = "state")String state) throws CloneNotSupportedException {
        Shape shape = myCanvas.getShapes().get(id);
        if(state.equalsIgnoreCase("move")){
            shape.move(dimension,id);
        }else if(state.equalsIgnoreCase("copy")){
            shape.copy(dimension,id);
        }

    }
    @CrossOrigin
    @RequestMapping("/shape")
    @ResponseBody
    public Map<Long, Shape> getAllShapes (){
        return myCanvas.getShapes();
    }

   /* @CrossOrigin
    @PutMapping("/copymove")
    public Shape MoveShape(@RequestParam(value = "operation") String operation,@RequestParam(value = "id") String id ,@RequestParam(value ="sX") String start_X ,
                             @RequestParam(value = "sY" ) String start_y,@RequestParam(value ="eX") String end_X , @RequestParam(value = "eY" ) String end_Y ){

        Shape shape = myCanvas.getShapes().get(id);
        shape.move(start_X,start_y,end_X,end_Y);
        return shape;
    }
    @CrossOrigin
    @PutMapping("/Resize")
    public Shape updateShape(@RequestParam(value = "id") String id ,@RequestParam(value ="sX") String start_X , @RequestParam(value = "eY" ) String start_y ){

        Shape shape = myCanvas.getShapes().get(id);
        shape.move(start_X,start_y,end_X,end_Y);
        return shape;
    }*/

    @CrossOrigin
    @DeleteMapping("/shapes")
    public int deleteShape (){
        myCanvas.clearCanvas();
        return myCanvas.getShapes().size();
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
