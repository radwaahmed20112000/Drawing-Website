<template>
  <div>
  
    <canvas id="canvasSelect"></canvas>
    <canvas id="Canvas" ></canvas>
    <toolsBar @setshape="setshape"/>
  </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8080"
import toolsBar from '@/components/toolsBar.vue'

let drawing = false;
let editing = false;
let startX = 0;
let startY = 0;
let imageData = null;
let X = 0;
let Y = 0;
let width = 0;
let height = 0;
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
    shapesData : [],
    StartPos : [],
    endPos : [],
    id : 0,
    toolBarHeight :0,
    selectCanvas : null,
    selectContext : null,
    mousemoved : false,
    selectedshape:'',
    currentCollor:"",
    currentFillColor:"",
    fill:true,
    currentLineWidth:0,
    radiusx:0,
    radiusy:0
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

     this.canvas.addEventListener("mousedown",this.startShape)
    this.canvas.addEventListener("mousemove",this.selectShape)
    this.canvas.addEventListener("mouseup",this.finishShape)
     

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
      this.drawline(e);
    else if(this.selectedshape=="ellipse")
      this.drawEllipse(e);
    
  },
    setshape(value){
      this.selectedshape=value;
    },

  /* General Canvas Methods */
    resizeCanvas() {
      this.toolBarHeight = document.getElementById("toolBar").offsetHeight
      this.canvas.width = window.innerWidth
      this.canvas.height = window.innerHeight-this.toolBarHeight
      this.selectCanvas.width = window.innerWidth
      this.selectCanvas.height = window.innerHeight-this.toolBarHeight
    },
    clearCanvas(){
      this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
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
          CenterY : Math.abs(Y-this.radiusy)
        }
      }else if(this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          ||this.selectedshape === "triangle" || this.selectedshape === "rectangle" ){
        Dimension = {
          start_X : startX,
          start_Y : startY,
          end_X : X,
          end_Y : Y
        }
      }
      Dimension = JSON.stringify(Dimension);
      this.sendShapeData(Dimension,style);
      /*this.endPos = [e.offsetX, e.offsetY];
      if(!this.mousemoved) return;
      this.mousemoved = false;
      const object = {
        type: "rect",
        fill: false,
        lineWidth: this.context.lineWidth,
        xstart: this.StartPos[0],
        ystart: this.StartPos[1],
        xend: this.endPos[0],
        yend: this.endPos[1],
        width: width,
        height: height,
      };
      this.shapesData[this.id] = object;
      this.id++;*/
    },
    setShapeAttributes(lineColor,fillColor,lineOpacity){
      this.context.lineWidth = lineOpacity;
      this.context.fillStyle = fillColor;
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

  /* Rectangle Drawing Method*/
    drawRect : function (e, sX = startX, sY = startY , eX = X , eY = Y){
      if(!drawing && !editing)
        return
      if(drawing){
        this.setEndCoordinates(e)
        this.context.putImageData(imageData,0,0)
      }
      width = eX-sX
      height = eY-sY
      this.context.strokeStyle = "black";
      this.context.lineWidth = 10;
      this.context.strokeRect(startX,startY,width,height)
      this.mousemoved = true;
    },
  /* Data Requests */
    async sendShapeData(Dimension, style) {
      let data = {
        shapeType: this.selectedshape,
        dimensions: Dimension,
        properties: style
      }
      const response = await axios.post(
          apiUrl+"/shapes",data,{headers: { 'Content-Type': 'application/json', } }
          )
      console.log(response.request.responseURL)
    },
    GetShapesData(){
      axios.get(
        `http://localhost:8080/`)
      .then(Response => {
        this.shapesData = Response.data;
      }
      )
    },
  /* Select Shapes Method */
    checkColor(e)
    {
      const rgbaClick = this.getRGBA(e, true);
      console.log(rgbaClick);
      console.log(this.rgbaCanvas)
      const bool = (JSON.stringify(this.rgbaCanvas) === JSON.stringify(rgbaClick));
      console.log(bool);
      return bool;
    },
    getRGBA(e)
    {
      const positions = this.context.getImageData(e.offsetX, e.offsetY, 1, 1);
      const rgbaClick = [];
      rgbaClick[0] = positions.data[0];
      rgbaClick[1] = positions.data[1];
      rgbaClick[2] = positions.data[2];
      rgbaClick[3] = positions.data[3] / 255;
      return rgbaClick;
    },
    getCanvasRgba()
    {
      const positions = this.context.getImageData(0, 0, 1, 1);
      const rgbaClick = [];
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
      //console.log(JSON.stringify(this.shapesData));
      for(let i =0; i<this.shapesData.length; i++)
      {
        const shape = this.shapesData[i];
        this.selectContext.beginPath();
        console.log(shape.lineWidth);
        this.selectContext.lineWidth = shape.lineWidth ;
        if(shape.type === "rect")
        {
          this.rectSelected(shape.fill, shape.xstart, shape.ystart, shape.width, shape.height);
        }
        if(this.selectContext.isPointInPath(e.offsetX, e.offsetY))
        {
          console.log(i);
          return i;
        }
        this.selectContext.closePath();
        this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
      }
    },
    rectSelected(fill, x, y, w, h)
    {
      if(!fill) this.selectContext.rect(x,y, w, h);
      else this.selectContext.fillRect(x,y,w,h);
    },

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
  /* Circle Drawing Method */
    drawCircle(e , sX = startX ,sY = startY , Radius = 0) {
      if(!drawing && !editing){return}
      if(drawing === true){
        this.context.putImageData(imageData,0,0)
        this.setEndCoordinates(e)
        Radius = Math.sqrt(Math.pow((X - sX), 2) - Math.pow((Y - sY), 2))
        this.radiusx = Radius
        this.radiusy = Radius
      }
      this.context.beginPath()
      this.context.arc(sX, sY, Radius, 0 , 2 * Math.PI)
      this.context.fill();
      this.context.stroke()
    },
  /* Triangle Drawing Method*/
    drawTriangle(e , endx = X ,endy = Y ,sX = startX, sY = startY) {
      if(!drawing && !editing){return}
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      const base = (endx - sX) * 2;
      this.context.moveTo(sX,sY);
      this.context.lineTo(endx,endy);
      this.context.lineTo((endx -base),endy);
      this.context.lineTo(sX,sY);
      this.context.fill();
      this.context.beginPath();
      this.context.stroke();
    },
  /* Ellipse Drawing Method */
    drawEllipse(e , radiusX = 0 ,radiusY = 0 ,centreX = 0,centreY = 0 ){
      if(!drawing && !editing){return}
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
      this.context.fill();
      this.context.beginPath();
    },
    drawPolygon(numberOfSides ,e ,sX = startX,eX = X,eY = Y){
      if(!drawing && !editing){return}
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      const radius = eX - sX;
      const step = 2 * Math.PI / numberOfSides;
      const shift = (Math.PI / 180.0) * -18;

      for(let i = 0 ; i <= numberOfSides ; i++ ){
        const curStep = i * step + shift;
        this.context.lineTo( eX + radius * Math.cos(curStep) ,eY + radius * Math.sin(curStep));
      }
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
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