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
let NumberOfSides = 5;

export default {
  name: "Canvas",
  components: {
    toolsBar
  },

  data(){
    return{
      // Shape :{
      //   shapeType: "",
      //   jsondimensions : "",
      //   jsonproperties : ""
      // },
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
    this.canvas.addEventListener("mousedown",this.startShape)
    this.canvas.addEventListener("mouseup",this.finishShape)
  },
  methods:{
    setshape(value){
      this.selectedshape=value;
      if(this.selectedshape==="circle")
        this.canvas.addEventListener("mousemove",this.drawCircle)
      else if(this.selectedshape==="pentagon") {
        NumberOfSides = 5
        this.canvas.addEventListener("mousemove", this.drawPolygon);
      }
      else if(this.selectedshape==="rectangle")
        this.canvas.addEventListener("mousemove",this.drawRect);
      else if(this.selectedshape==="triangle")
        this.canvas.addEventListener("mousemove",this.drawTriangle);
      else if(this.selectedshape==="hexagon") {
        NumberOfSides = 6
        this.canvas.addEventListener("mousemove", this.drawPolygon);
      }
      else if(this.selectedshape==="line")
        this.canvas.addEventListener("mousemove",this.drawline);
      else if(this.selectedshape==="ellipse")
        this.canvas.addEventListener("mousemove",this.drawEllipse);
    },
    moveShape(e){
      const shape = {
        name: "hexagon",
        x_start: 222,
        y_start: 300,
        x_end: 400,
        y_end: 400,
        line_color: "black",
        line_width: 10,
        fill_color: "black",
        Transparent: true
      };
      this.drawShapeEdit(e,shape);
    },
    drawShapeEdit(e,shape){
      this.setshape(shape.name)
      if(this.selectedshape==="circle")
        this.drawCircleEdit(e, shape.x_center,shape.y_center,shape.x_radius );
      else if(this.selectedshape==="pentagon")
        this.drawPolygonEdit(5,e ,shape.x_end-shape.y_start);
      else if(this.selectedshape==="rectangle")
        this.drawRectEdit(e ,( shape.x_end - shape.x_start ),(shape.y_end - shape.y_start) );
      else if(this.selectedshape==="triangle")
        this.drawTriangleEdit(e ,( shape.x_end - shape.x_start )*2,shape.y_end - shape.y_start);
      else if(this.selectedshape==="hexagon")
        this.drawPolygonEdit(6,e,shape.x_end-shape.y_start);
      else if(this.selectedshape==="line")
        this.drawline(e);
      else if(this.selectedshape==="ellipse")
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
      for(let i = 0 ; i < this.shapesData.length; i++ ){
        const shape = this.shapesData[i];
        this.setshape(shape.name)
        if(this.selectedshape==="circle")
          this.drawCircle(null,shape.x_radius, shape.x_center,shape.y_center );
        else if(this.selectedshape==="pentagon")
          this.drawPolygon(5,null ,shape.x_start,shape.x_end,shape.y_start);
        else if(this.selectedshape==="rectangle")
          this.drawRect(null ,shape.x_start,shape.y_start,shape.x_end,shape.y_end );
        else if(this.selectedshape==="triangle")
          this.drawTriangle(null , shape.x_end , shape.y_end ,shape.x_start ,shape.y_start);
        else if(this.selectedshape==="hexagon")
          this.drawPolygon(6,null,shape.x_start,shape.x_end,shape.y_start);
        else if(this.selectedshape==="line")
          this.drawline(null);
        else if(this.selectedshape==="ellipse")
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
      this.setEndCoordinates(e);
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      this.StartPos = [startX, startY];
      // this.drawCircle(e)
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
      this.GetShapesData();
    },
    setShapeAttributes(lineColor,fillColor,lineOpacity,fill){
      console.log("i hate touuuuuuuuuuuuuuu")
      this.context.lineWidth = lineOpacity;
      console.log("tosaaa"+this.context.lineWidth);
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
      // const response = await axios.post(
      //     apiUrl + "/shape"
      //     ,data ,
      //     {headers: { 'Content-Type': 'application/json', }
      //   })
      // console.log(response.request.responseURL)
      await axios.post(
          apiUrl + "/shape"
          ,data ,
          {headers: { 'Content-Type': 'application/json', }
          })
      await this.GetShapesData()
    },
    async GetShapesData(){
      await axios.get(`http://localhost:8085/shape`)
          .then(Response => {
                //  console.log(Response.data)
                for(let i =0 ;i < Object.keys(Response.data).length;i++){
                  this.shapesData[i] = {
                    shapeType: Response.data[i].shapeType,
                    jsondimensions: JSON.parse(JSON.stringify(Response.data[i].jsondimensions)),
                    jsonproperties: JSON.parse(JSON.stringify(Response.data[i].jsonproperties))
                  }
                  console.log(this.shapesData)
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
    drawPolygon(numberOfSides,e ,sX = startX,eX = X,eY = Y){
      if(!drawing && !DrawingCanvasMode){return}
      if(!numberOfSides)
        numberOfSides = NumberOfSides
      if(drawing){
        this.context.putImageData(imageData,0,0);
        this.setEndCoordinates(e);
      }
      const radius = eX - sX;
      const step = 2 * Math.PI / numberOfSides;
      const shift = (Math.PI / 180.0) * -18;

      for(let i = 0 ; i <= numberOfSides ; i++ ){
        var curStep = i * step + shift;
        this.context.lineTo( eX + radius * Math.cos(curStep) ,eY + radius * Math.sin(curStep));
      }
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
    },
    drawPolygonEdit(numberOfSides ,e ,radius){
      if(!editing){return}
      this.context.putImageData(imageData,0,0);
      const step = 2 * Math.PI / numberOfSides;
      const shift = (Math.PI / 180.0) * -18;

      for(let i = 0 ; i <= numberOfSides ; i++ ){
        const curStep = i * step + shift;
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
      this.context.strokeRect(sX,sY,width,height)
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
    drawCircle(e , Radius =Math.sqrt(Math.pow((X - startX), 2) + Math.pow((Y - startY), 2)) ,sX = startX ,sY = startY   ) {
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
      this.context.stroke()
      if(this.fill){
        this.context.fill();
      }
      this.context.beginPath();
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