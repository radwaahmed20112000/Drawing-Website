<template>
  <div>

    <canvas id="canvasSelect"></canvas>
    <canvas id="Canvas" ></canvas>
    <toolsBar id="toolsBar" @setshape="setshape"></toolsBar>
  </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8086"
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';


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
    document.getElementById("move").addEventListener("click", ()=>{
      this.canvas.addEventListener("mousedown",this.startEdit)
      this.canvas.addEventListener("mousemove",this.moveShape)
      this.canvas.addEventListener("mouseup",this.finishEdit)
    })
    document.getElementById("delete").addEventListener("click",this.eraseShapes)
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
      else if(this.selectedshape==="rectangle")
        this.drawRect(e);
      else if(this.selectedshape==="triangle")
        this.drawTriangle(e);
      else if(this.selectedshape==="hexagon")
        this.drawPolygon(6,e);
      else if(this.selectedshape==="line")
        this.drawline(e);
      else if(this.selectedshape==="ellipse")
        this.drawEllipse(e);

    },
    setshape(value){
      this.selectedshape=value;
    },
    moveShape(e,id=0){
      this.drawShapeEdit(e,id);
    },
    drawShapeEdit(e,id=0){
      this.setshape(this.shapesData[id].shapeType)
      if(this.selectedshape==="circle")
        this.drawCircle(e)
      // else if(this.selectedshape==="pentagon")
      //   this.drawPolygonEdit(5,e ,shape.x_end-shape.y_start);
      // else if(this.selectedshape==="rectangle")
      //   this.drawRectEdit(e ,( shape.x_end - shape.x_start ),(shape.y_end - shape.y_start) );
      // else if(this.selectedshape==="triangle")
      //   this.drawTriangleEdit(e ,( shape.x_end - shape.x_start )*2,shape.y_end - shape.y_start);
      else if(this.selectedshape==="hexagon"){
        this.drawPolygon(6,e);
      }
      // else if(this.selectedshape==="line")
      //   this.drawline(e);
      // else if(this.selectedshape==="ellipse")
      //   this.drawEllipseEdit(e ,shape.x_radius,shape.y_radius, shape.x_center,shape.y_center );
    },

    /* General Canvas Methods */
    resizeCanvas() {
      let toolsBar= document.getElementById("toolsBar")
      let toolsBarWidth = toolsBar.clientWidth
      this.toolBarHeight = document.getElementById("toolBar").offsetHeight
      this.canvas.style.marginLeft= "60px"
      this.canvas.width = window.innerWidth-toolsBarWidth
      this.canvas.height = window.innerHeight-this.toolBarHeight
      this.selectCanvas.width = window.innerWidth
      this.selectCanvas.height = window.innerHeight-this.toolBarHeight-toolsBarWidth
      toolsBar.style.height = this.canvas.height
    },

    drawCanvas(id=0){
      this.canvas.addEventListener("mousemove",()=>{})
      DrawingCanvasMode = true
      console.log("HAHA")
      this.clearCanvas()
      console.log(this.shapesData)
      for(let i = 0 ; i < this.shapesData.length; i++ ){
        if(i===id)
          continue
        this.drawShapeProgrammatically(i)
      }
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      this.drawShapeProgrammatically(id)
      DrawingCanvasMode = false
    },
    clearCanvas(){
      this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
    },
    startEdit(id=0){
      editing = true
      this.drawCanvas(id)
    },
    finishEdit(){
      editing = false
      this.context.beginPath();

    },
    startShape(e){
      drawing = true
      this.setStartCoordinates(e)
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    },
    async finishShape() {
      drawing = false
      this.context.beginPath();
      let style = {
            Color: this.currentCollor,
            fillColor: this.currentFillColor,
            Transparent: this.fill,
            lineWidth: this.currentLineWidth
          },
          Dimension;
      style = JSON.stringify(style);
      if (this.selectedshape === "circle" || this.selectedshape === "ellipse") {
        Dimension = {
          radiusX: this.radiusx,
          radiusY: this.radiusy,
          CenterX: Math.abs(startX),
          CenterY: Math.abs(startY)
        }
      } else if (this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          || this.selectedshape === "triangle" || this.selectedshape === "rectangle") {
        Dimension = {
          start_X: startX,
          start_Y: startY,
          end_X: X,
          end_Y: Y
        }
      }
      Dimension = JSON.stringify(Dimension);
      await this.sendShapeData(Dimension, style);
      await this.GetShapesData();
    },
    setShapeAttributes(lineColor,fillColor,lineOpacity,fill){
      this.context.lineWidth = lineOpacity;
      this.context.fillStyle = fillColor;
      this.fill = fill ;
      this.context.strokeStyle = lineColor;
    },
    /* Set Coordinates */
    setStartCoordinates : function (e,x,y){
      if(e) {
        startX = e.offsetX
        startY = e.offsetY

      }
      else{
        startX = x
        startY = y

      }
    },
    setEndCoordinates : function (e,x,y){
      if(e) {
        X = e.offsetX
        Y = e.offsetY
      }
      else{
        X = x
        Y = y
      }

    },

    /* Data Requests */
    async sendShapeData(Dimension, style) {
      let data = {
        shapeType: this.selectedshape,
        dimensions: Dimension,
        properties: style
      }
      await axios.post(apiUrl + "/shape",data)
    },
    async GetShapesData(){
      await axios.get(apiUrl + "/shape").then(Response => {
        console.log(Response.data)
        console.log("LENGTH"+Object.keys(Response.data).length)
                for(let i =0 ;i < Object.keys(Response.data).length;i++){
                  console.log("RESPOSE"+Response.data[i])
                  this.shapesData[i] = {
                    shapeType: Response.data[i].shapeType,
                    jsondimensions: JSON.parse(JSON.stringify(Response.data[i].jsondimensions)),
                    jsonproperties: JSON.parse(JSON.stringify(Response.data[i].jsonproperties))
                  }
               //   console.log(this.shapesData)
                }})
    },
    async eraseShapes(){
      this.shapesData = []
     const response =  await axios.delete(apiUrl + "/shapes")
      console.log("OOO"+response.data)
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
    drawTriangle(e) {
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      const base = (X - startX) * 2;
      this.context.moveTo(startX,startY);
      this.context.lineTo(X,Y);
      this.context.lineTo((X -base),Y);
      this.context.lineTo(startX,startY);
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
      this.context.stroke();
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
    drawPolygon(numberOfSides ,e){
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.setEndCoordinates(e);
      }
      // console.log(X+" "+Y+" "+startX+" "+startY+" "+numberOfSides)
      if(!editing)
        this.radiusx = X - startX;
      this.context.putImageData(imageData,0,0);
      const step = 2 * Math.PI / numberOfSides;
      const shift = (Math.PI / 180.0) * -18;

      for(let i = 0 ; i <= numberOfSides ; i++ ){
        const curStep = i * step + shift;
        this.context.lineTo( X + this.radiusx * Math.cos(curStep) ,Y + this.radiusx * Math.sin(curStep));
      }
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
    },

    drawRect(e){
      if(!drawing &&!DrawingCanvasMode)
        return
      if(drawing){
        this.setEndCoordinates(e)
        this.context.putImageData(imageData,0,0)
      }
      let width = X-startX
      let height = Y-startY
      if(this.fill){
        this.context.fill();
      }
      this.context.strokeRect(startX,startY,width,height)
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
    /* Elliptical Shapes Drawing Methods */
    drawEllipse(e){
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.setEndCoordinates(e);
        this.radiusx = Math.abs(X-startX)/2
        this.radiusy = Math.abs(Y-startY)/2
        // const centreX = Math.abs(X-this.radiusx)
        // const centreY = Math.abs(Y-this.radiusy);
        this.context.putImageData(imageData,0,0);
      }

      this.context.ellipse(startX,startY,this.radiusx,this.radiusy,Math.PI , 0 ,2 * Math.PI);
      this.context.stroke();
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
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
    drawCircle(e) {
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing === true ){
          this.setEndCoordinates(e)
      }
      if(!editing)
        this.radiusx = Math.sqrt(Math.pow((X - startX), 2) + Math.pow((Y - startY), 2))
      this.context.putImageData(imageData,0,0);
      this.context.beginPath()
      this.context.arc(startX, startY, this.radiusx, 0 , 2 * Math.PI)
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke()
    }	,
    drawShapeProgrammatically(id){
      let shape = this.shapesData[id]
      let shapeType = shape.shapeType
      if(shapeType==="circle"||shapeType==="ellipse") {
        this.setStartCoordinates(null, shape.jsondimensions.CenterX, shape.jsondimensions.CenterY)
        this.radiusx = shape.jsondimensions.radiusX
        this.radiusy = shape.jsondimensions.radiusY
        if(shapeType === "circle")
          this.drawCircle(null)
        else
          this.drawEllipse(null)
      }
      else{
        this.setStartCoordinates(null, shape.jsondimensions.start_X, shape.jsondimensions.start_Y)
        this.setEndCoordinates(null, shape.jsondimensions.end_X, shape.jsondimensions.end_Y)
        this.radiusx = X-startX
        if(this.selectedshape==="pentagon")
          this.drawPolygon(5,null);
        else if(this.selectedshape==="rectangle")
          this.drawRect(null );
        else if(this.selectedshape==="triangle")
          this.drawTriangle(null);
        else if(this.selectedshape==="hexagon")
          this.drawPolygon(6,null);
        else if(this.selectedshape==="line")
          this.drawline(null);
      }
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