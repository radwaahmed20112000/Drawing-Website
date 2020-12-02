<template>
  <div>
    <canvas id="canvasSelect"></canvas>
    <canvas id="Canvas" ></canvas>
    <toolsBar @setshape="setshape"/>
  </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8085"
import toolsBar from '@/components/toolsBar.vue'

let drawing = false;
let editing = false;
let DrawingCanvasMode = false;
let startX = 0;
let startY = 0;
let imageData = null;
let X = 0;
let Y = 0;
export default {
name: "Canvas",
components: {
   toolsBar
    },

data(){
  return{
    canvas :null,
    context : null,
    rgbaCanvas : [],
    selectContext : null,
    selectCanvas: null,
    toolBarHeight :0,
    mousemoved : false,
    shapesData:[],
    selectedshape:'',
    currentCollor:"",
    currentFillColor:"",
    fill:true,
    currentLineWidth:0,
    radiusx:0,
    radiusy:0,
    currentShape : null
  }
  },
  mounted() {
    this.canvas = document.getElementById("Canvas")
    this.context = this.canvas.getContext("2d")
    this.selectCanvas = document.getElementById("canvasSelect");
    this.selectContext = this.selectCanvas.getContext("2d");
    this.resizeCanvas()
    window.addEventListener('resize',() => {
      this.resizeCanvas()
    })
    this.rgbaCanvas = this.getCanvasRgba();
    //this.canvas.addEventListener("mousedown",this.startShape)
    //this.canvas.addEventListener("mousemove",this.selectShape)
    //this.canvas.addEventListener("mouseup",this.finishShape)
    this.canvas.addEventListener("click", this.select);
    this.canvas.addEventListener("mousedown",this.startEdit)
    this.canvas.addEventListener("mousemove",this.moveShape)
    this.canvas.addEventListener("mouseup",this.finishEdit)
  },
  methods:{
    selectShape(e){
      this.setEndCoordinates(e);
      if(this.selectedshape==="circle")
        this.drawCircle(e);
      else if(this.selectedshape==="pentagon")
        this.drawPolygon(5,e);
      else if(this.selectedshape=="rectangle")
        this.drawRect(e);
      else if(this.selectedshape=="triangle")
        this.drawTriangle(e);
      else if(this.selectedshape=="hexagon")
        this.drawPolygon(6,e);
      else if(this.selectedshape=="line")
        this.drawLine(e);
      else if(this.selectedshape=="ellipse")
        this.drawEllipse(e);
      
    },
    setshape(value){
      this.selectedshape=value;
    },
    moveShape(e){
      this.drawShapeEdit(e,this.currentShape);
    },
    drawShapeEdit(e,shape){
      this.setshape(shape.name)
      if(this.selectedshape==="circle")
        this.drawCircleEdit(e, shape.x_center,shape.y_center,shape.x_radius );
      else if(this.selectedshape==="pentagon")
        this.drawPolygonEdit(5,e ,shape.x_end-shape.y_start);
      else if(this.selectedshape=="rectangle")
        this.drawRectEdit(e ,( shape.x_end - shape.x_start ),(shape.y_end - shape.y_start) );
      else if(this.selectedshape=="triangle")
        this.drawTriangleEdit(e ,( shape.x_end - shape.x_start )*2,shape.y_end - shape.y_start);
      else if(this.selectedshape=="hexagon")
        this.drawPolygonEdit(6,e,shape.x_end-shape.y_start);
      else if(this.selectedshape=="line")
        this.drawLineEdit(e, shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      else if(this.selectedshape=="ellipse")
        this.drawEllipseEdit(e ,shape.x_radius,shape.y_radius, shape.x_center,shape.y_center );
    },

  /* General Canvas Methods */
    resizeCanvas() {
      this.toolBarHeight = document.getElementById("toolBar").offsetHeight
      this.canvas.width = window.innerWidth
      this.canvas.height = window.innerHeight-this.toolBarHeight
      this.selectCanvas.width = window.innerWidth
      this.selectCanvas.height = window.innerHeight-this.toolBarHeight
    },

    drawCanvas(){
      for(var i = 0 ; i < this.shapesData.length; i++ ){
        var shape = this.shapesData[i];
        this.setshape(shape.name)
        if(this.selectedshape==="circle")
          this.drawCircle(null,shape.x_radius, shape.x_center,shape.y_center );
        else if(this.selectedshape==="pentagon")
          this.drawPolygon(5,null ,shape.x_start,shape.x_end,shape.y_start);
        else if(this.selectedshape=="rectangle")
          this.drawRect(null ,shape.x_start,shape.y_start,shape.x_end,shape.y_end );
        else if(this.selectedshape=="triangle")
          this.drawTriangle(null , shape.x_end , shape.y_end ,shape.x_start ,shape.y_start);
        else if(this.selectedshape=="hexagon")
          this.drawPolygon(6,null,shape.x_start,shape.x_end,shape.y_start);
        else if(this.selectedshape=="line")
          this.drawLine(null);
        else if(this.selectedshape=="ellipse")
          this.drawEllipse(null,shape.x_radius,shape.y_radius, shape.x_center,shape.y_center );
      }
    },
    clearCanvas(){
      this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
    },
    startEdit(){
      editing = true 
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    },
    finishEdit(){
      editing = false 
      this.context.beginPath();

    },
    startShape(e){
      drawing = true
      this.setStartCoordinates(e)
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      this.StartPos = [startX, startY];
    },
    finishShape(){
      drawing = false
      this.context.beginPath();
      if(!this.mousemoved) return;
      this.mousemoved = false;
      let style = {
        Color : this.currentCollor,
        fillColor : this.currentFillColor,
        Transparent : this.fill,
        lineWidth : this.currentLineWidth
      },
      Dimension;
      style = JSON.stringify(style);
      if(this.selectedshape ==="circle" || this.selectedshape === "ellipse"){
        Dimension = {
          radiusX : this.radiusx,
          radiusY : this.radiusy,
          CenterX : Math.abs(X-this.radiusx),
          CenterY : Math.abs(Y-this.radiusy),
          start_X : startX,
          start_Y : startY,
          end_X : X,
          end_Y : Y
        }
      }else if(this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          ||this.selectedshape === "triangle" || this.selectedshape === "rectangle" || this.selectedshape === "line"){
        Dimension = {
          start_X : startX,
          start_Y : startY,
          end_X : X,
          end_Y : Y
        }
      }
      Dimension = JSON.stringify(Dimension);
      this.sendShapeData(Dimension,style);
      this.GetShapesData();
    },
    setShapeAttributes(lineColor,fillColor,lineOpacity,fill){
      this.context.lineWidth = lineOpacity;
      this.context.fillStyle = fillColor;
      this.fill = fill ;
      this.context.strokeStyle = lineColor;
    },
  /* Set Coordinates */
    setStartCoordinates : function (e){
      startX = e.offsetX
      startY = e.offsetY
    },
    setEndCoordinates : function (e){
      X = e.offsetX
      Y = e.offsetY
    },

  /* Data Requests */
    async sendShapeData(Dimension, style) {
      let data = {
        shapeType: this.selectedshape,
        dimensions: Dimension,
        properties: style
      }
      const response = await axios.post(
          apiUrl + "/shapes"
          ,data ,
          {headers: { 'Content-Type': 'application/json', }
        })
      console.log(response.request.responseURL)
    },
    async GetShapesData(){
      await axios.get(`http://localhost:8085/shapes`)
      .then(Response => {
        console.log(Response.data)
        for(let i =0 ;i < Object.keys(Response.data).length;i++){
          let shape;
          if(Response.data[i].shapeType === "circle" || Response.data[i].shapeType === "ellipse" ){
            shape = {
              name : Response.data[i].shapeType,
              x_radius : Response.data[i].jsondimensions.radiusX,
              y_radius : Response.data[i].jsondimensions.radiusY,
              x_center : Response.data[i].jsondimensions.CenterX,
              y_center : Response.data[i].jsondimensions.CenterY,
              x_start : Response.data[i].jsondimensions.start_X,
              y_start : Response.data[i].jsondimensions.start_Y,
              x_end : Response.data[i].jsondimensions.end_X,
              y_end : Response.data[i].jsondimensions.end_Y,
              line_color : Response.data[i].jsonproperties.Color,
              line_width : Response.data[i].jsonproperties.lineWidth,
              fill_color : Response.data[i].jsonproperties.fillColor,
              Transparent : Response.data[i].jsonproperties.Transparent
            }
          }else{
            shape = {
              name : Response.data[i].shapeType,
              x_start : Response.data[i].jsondimensions.start_X,
              y_start : Response.data[i].jsondimensions.start_Y,
              x_end : Response.data[i].jsondimensions.end_X,
              y_end : Response.data[i].jsondimensions.end_Y,
              line_color : Response.data[i].jsonproperties.Color,
              line_width : Response.data[i].jsonproperties.lineWidth,
              fill_color : Response.data[i].jsonproperties.fillColor,
              Transparent : Response.data[i].jsonproperties.Transparent
            }
          }
          this.shapesData[i] = shape
        }
      }
      )
    },
  /* Free Sketching Methods */
    startSketch : function(e){
      drawing = true
      this.sketch(e)
      this.context.fillStyle = "#f56909"
      this.context.fillRect(0,0,this.canvas.width,this.canvas.height);
    },
    sketch : function(e){
      if(drawing){
        this.context.lineWidth = 10
        this.context.lineCap = "round"
        this.setStartCoordinates(e)
        this.context.lineTo(startX,startY)
        this.context.stroke()
      }
    },
  /* Polygons Drawing Methods*/
    drawTriangle(e , endx = X ,endy = Y ,sX = startX, sY = startY) {
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      const base = (endx - sX) * 2;
      this.context.moveTo(sX,sY);
      this.context.lineTo(endx,endy);
      this.context.lineTo((endx -base),endy);
      this.context.lineTo(sX,sY);
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
      this.context.stroke();
      this.mousemoved = true;
    },
    drawTriangleEdit(e , base,height ) {
      if(!editing){return}
      this.context.putImageData(imageData,0,0);
      this.context.moveTo(e.offsetX,e.offsetY);
      this.context.lineTo(e.offsetX + base/2 ,e.offsetY + height);
      this.context.lineTo(e.offsetX - base/2 ,e.offsetY + height);
      this.context.lineTo(e.offsetX,e.offsetY);
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
      this.context.stroke();
    },
    drawPolygon(numberOfSides ,e ,sX = startX,eX = X,eY = Y){
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      var radius = eX-sX;
      var step  = 2 * Math.PI / numberOfSides;
      var shift = (Math.PI / 180.0) * -18;
    
      for(var i = 0 ; i <= numberOfSides ; i++ ){
        var curStep = i * step + shift;
        this.context.lineTo( eX + radius * Math.cos(curStep) ,eY + radius * Math.sin(curStep));
      }
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
      this.mousemoved = true;
    },
    drawPolygonEdit(numberOfSides ,e ,radius){
      if(!editing){return}
      this.context.putImageData(imageData,0,0);
      var step  = 2 * Math.PI / numberOfSides;
      var shift = (Math.PI / 180.0) * -18;

      for(var i = 0 ; i <= numberOfSides ; i++ ){
        var curStep = i * step + shift;
        this.context.lineTo( e.offsetX + radius * Math.cos(curStep) ,e.offsetY + radius * Math.sin(curStep));
      }
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
    },

    drawRect(e, sX = startX, sY = startY , eX = X , eY = Y){
      if(!drawing &&!DrawingCanvasMode)
        return
      if(drawing){
        this.setEndCoordinates(e)
        this.context.putImageData(imageData,0,0)
      }
      let width = eX-sX
      let height = eY-sY
      if(this.fill){
        this.context.fill();
      }
      this.context.strokeRect(sX,sY,width,height);
      this.mousemoved = true;
    },

    drawRectEdit(e, width, height){
      if(!editing)
        return
      this.context.putImageData(imageData,0,0)
      if(this.fill){
        this.context.fill();
      }
      this.context.strokeRect(e.offsetX,e.offsetY,width,height)
    },
    drawLine(e){
      if(!drawing &&!DrawingCanvasMode)
        return
      if(drawing){
        this.setEndCoordinates(e)
        this.context.putImageData(imageData,0,0)
      }
      this.context.moveTo(startX, startY);
      this.context.lineTo(X, Y);
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke();
      this.mousemoved = true;
    },
    /*drawLineEdit() // needs editing
    {
      if(!editing)
        return
      this.context.putImageData(imageData,0,0)
      if(this.fill){
        this.context.fill();
      }
      this.context.moveTo(e.offsetX, e.offsetY);
      this.context.lineTo(X, Y);
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke();
    },*/

  /* Elliptical Shapes Drawing Methods */
    drawEllipse(e , radiusX = 0 ,radiusY = 0 ,centreX = 0,centreY = 0 ){
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.setEndCoordinates(e);
        radiusX = Math.abs(X-startX)/2
        radiusY = Math.abs(Y-startY)/2
        centreX = Math.abs(X-radiusX)
        centreY = Math.abs(Y-radiusY);
        this.context.putImageData(imageData,0,0);
      }
      this.radiusx = radiusX
      this.radiusy = radiusY
      this.context.ellipse(centreX,centreY,radiusX,radiusY,Math.PI , 0 ,2 * Math.PI);
      this.context.stroke();
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
      this.mousemoved = true;
    },
    drawEllipseEdit(e , radiusX ,radiusY  ){
      if(!editing){return}
        this.setEndCoordinates(e);
      this.context.putImageData(imageData,0,0);
      this.context.ellipse(e.offsetX,e.offsetY,radiusX,radiusY,Math.PI , 0 ,2 * Math.PI);
      this.context.stroke();
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
    },
    drawCircle(e , Radius =Math.sqrt(Math.pow((X - startX), 2) - Math.pow((Y - startY), 2)) ,sX = startX ,sY = startY   ) {
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing === true){
        this.setEndCoordinates(e)
        // Radius = Math.sqrt(Math.pow((X - sX), 2) - Math.pow((Y - sY), 2))
        this.radiusx = Radius
        this.radiusy = Radius
      }
      this.context.putImageData(imageData,0,0)
      this.context.beginPath()
      this.context.arc(sX, sY, Radius, 0 , 2 * Math.PI)
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke();
      if(Radius >=1)
      this.mousemoved = true;
      else this.mousemoved = false;
    },
    drawCircleEdit(e , Radius  ) {
      if( !editing){return}
      //this.setEndCoordinates(e)
      this.context.putImageData(imageData,0,0)
      this.context.beginPath()
      this.context.arc(e.offsetX, e.offsetY, Radius, 0 , 2 * Math.PI)
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke()
    },

    /****SELECTION****/
  checkColor(e)
  {
    var rgbaClick = this.getRGBA(e, true);
    console.log(rgbaClick);
    console.log(this.rgbaCanvas)
    var bool =  (JSON.stringify(this.rgbaCanvas) == JSON.stringify(rgbaClick))
    console.log(bool);
    return bool;
  },
  getRGBA(e)
  {
    var positions = this.context.getImageData(e.offsetX, e.offsetY, 1, 1); 
    var rgbaClick = [];
    rgbaClick[0] = positions.data[0];
    rgbaClick[1] = positions.data[1];
    rgbaClick[2] = positions.data[2];
    rgbaClick[3] = positions.data[3] / 255;
    return rgbaClick;
  },
  getCanvasRgba()
  {
    var positions = this.context.getImageData(0, 0, 1, 1); 
    var rgbaClick = [];
    rgbaClick[0] = positions.data[0];
    rgbaClick[1] = positions.data[1];
    rgbaClick[2] = positions.data[2];
    rgbaClick[3] = positions.data[3] / 255;
    return rgbaClick;
  },
  select(e)
  {
    if(this.checkColor(e)) 
    {
      console.log("wrong");
      return;
    }
    for(var i = this.shapesData.length - 1; i>=0; i--)
    {
      var shape = this.shapesData[i];
      this.selectContext.beginPath();
      if(shape.type === "rectangle") this.rectSelected(shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      else if(shape.type === "circle" ) this.circleSelected(shape.x_start, shape.y_start, shape.x_end, shape.y_end, shape.x_radius);
      else if(shape.type === "triangle") this.triangleSelected(shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      else if(shape.type === "ellipse") this.ellipseSelected(shape.x_end, shape.y_end, shape.x_radius ,shape.y_radius, shape.x_center, shape.y_center);
      else if(shape.type === "pentagon") this.polygonSelected(5, shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      else if(shape.type === "hexagon") this.polygonSelected(6, shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      else if(shape.type === "line") this.lineSelected(shape.x_start, shape.y_start, shape.x_end, shape.y_end);
      if(this.selectContext.isPointInPath(e.offsetX , e.offsetY ))
      {
        console.log(i);
        this.selectContext.closePath();
        this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
        this.currentShape = shape;
      }
      this.selectContext.closePath();
      this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
    }
  },
  rectSelected( x, y, xe, ye)
  {
    let width = xe-x;
    let height = ye-y;
    this.selectContext.setTransform(1.3, 0, 0, 1.3, -xe*0.27, -ye*0.3);
    this.selectContext.rect(x,y, width, height);
    this.selectContext.fill();
  },
  circleSelected( x, y, xe, ye, r)
  {
    this.selectContext.setTransform(1.3, 0, 0, 1.3, -xe*0.27, -ye*0.3);
    this.selectContext.arc(x,y,r,0, 2*Math.PI);
    this.selectContext.fill();
  },
  triangleSelected(xs, ys, xe, ye)
  {
    var b = (xe - xs) * 2;
    this.selectContext.setTransform(1.3, 0, 0, 1.3, -xe*0.25, -ye*0.3);
    this.selectContext.moveTo(xs,ys);
    this.selectContext.lineTo(xe,ye);
    this.selectContext.lineTo((xe-b),ye);
    this.selectContext.lineTo(xs,ys);
    this.selectContext.fill();
  },
  ellipseSelected(xe, ye, rx,ry,cx,cy)
  {
    this.selectContext.setTransform(1.3, 0, 0, 1.3, -xe*0.28, -ye*0.3);
    this.selectContext.ellipse(cx,cy,rx,ry,Math.PI , 0 ,2 * Math.PI);
    this.selectContext.fill();
  },
  polygonSelected(n, xs, ys, xe, ye)
  {
    this.selectContext.setTransform(1.3, 0, 0, 1.3, -xe*0.3, -ye*0.3);
    var radius = xe-xs;
    var step  = 2 * Math.PI / n;
    var shift = (Math.PI / 180.0) * -18;
    for(let i = 0 ; i <= n; i++ ){
      const curStep = i * step + shift;
      this.selectContext.lineTo( xe + radius * Math.cos(curStep) ,ye + radius * Math.sin(curStep));
    }
    this.selectContext.fill();
  },
  lineSelected(x, y, xe, ye)
  {
    this.selectContext.moveTo(x+10, y + 2);
    this.selectContext.lineTo(x -10, y - 2);
    this.selectContext.lineTo(xe - 10, ye - 2);
    this.selectContext.lineTo(xe + 10, ye + 2);
    this.selectContext.fill();
  }
  }
}
</script>

<style scoped>
#Canvas{
  position: absolute;
  padding: 0;
  margin: 0;
  /*border: darkgrey 2px solid;*/
  border: 3px solid black;
  

}
#canvasSelect{
  padding: 0;
  margin : 0;
 display:none;
}
</style>