<template>
  <div>
    <canvas id="canvasSelect"></canvas>
    <canvas id="Canvas" ></canvas>
    <toolsBar id="toolsBar" @setshape="setshape" @setselectmode = "setselectmode"/>
  </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8089"
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
import toolsBar from '@/components/toolsBar.vue'
let drawing = false;
let Selected = false;
let Moving = false;
let Resizing = false
let Copying = false
let Deleting = false
let Dimension = {};
let style = {};
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
      currentId : null,
      currentShape:null,
      width:0,
      height:0
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
    this.eraseShapes()
    this.rgbaCanvas = this.getCanvasRgba();
    console.log(this.rgbaCanvas);
    /***************/
    document.getElementById("move").addEventListener("click",()=> {
      Moving = true
      Copying = false
      Deleting = false
      Resizing = false
      this.currentShape = null
    })
    document.getElementById("copy").addEventListener("click",()=> {
      Copying = true
      Moving = false
      Resizing = false
      Deleting = false
      this.currentShape = null
    })
    document.getElementById("resize").addEventListener("click",()=> {
      Resizing = true
      Moving = false
      Copying = false
      Deleting = false
      this.currentShape = null
    })
    document.getElementById("delete").addEventListener("click",()=>{
      Deleting = true
      Moving = false
      Copying = false
      Resizing = false
      this.currentShape = null
    });
    this.canvas.addEventListener("mousedown", async (e)=> {
      if(Moving||Copying||Deleting||Resizing){//andmove
        await this.select(e)
        this.startEdit(e)
      }
      else
        this.startShape(e);
      if(Resizing&& Selected&& !drawing)
        this.finishEdit(e);
    });
  this.canvas.addEventListener("mousemove", (e)=>{
      if(Moving||Copying)//andmove
        this.moveShape(e);
      else if(Resizing)
        this.resizeShape(e);
      else if(drawing)
        this.selectShape(e);
    });
    this.canvas.addEventListener("mouseup", (e)=> {
      if((Moving||Copying ) && Selected)
        this.finishEdit(e);
      else if (drawing )
        this.finishShape(e);
      else if(Deleting)
        this.deleteShape()
    });
  },
  methods:{
    async setUndoMode(value){
      await this.GetShapesData("/"+value);
      this.clearCanvas();
      for(let i = 0 ; i < this.shapesData.length; i++ ){
        this.drawShapeProgrammatically(i)
      }
    },
    selectShape(e){
      drawing = true
      // if(!drawing) return;
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
        this.drawLine(e);
      else if(this.selectedshape==="ellipse")
        this.drawEllipse(e);
      else if(this.selectedshape==="sketch")
        this.sketch(e);
    },
    setselectmode()
    {
      Selected = true
      console.log("select moooooooooooode");
    },
    setshape(value){
      this.selectedshape=value;
      Moving = false
      Copying = false
      Deleting = false
    },
    resizeShape(e){

      //imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      this.selectShape(e)
    },
    moveShape(e){
      if(!Selected) return;
      this.drawShapeEdit(e,this.currentId);
    },
    drawShapeEdit(e/*,id*/){
     // console.log(id);
      //this.selectedshape=this.shapesData[id].shapeType;
      this.selectedshape=this.currentShape.shapeType;
      // const shape = this.shapesData[id];
      const shape = this.currentShape;
      if(this.selectedshape==="circle")
        this.drawCircleEdit(e,shape.jsondimensions.radiusX, shape.jsondimensions.CenterX, shape.jsondimensions.CenterY)
      else if(this.selectedshape==="pentagon")
        this.drawPolygonEdit(5,e );
      else if(this.selectedshape==="rectangle")
        this.drawRectEdit(e ,( shape.jsondimensions.end_X - shape.jsondimensions.start_X ),(shape.jsondimensions.end_Y - shape.jsondimensions.start_Y) );
      else if(this.selectedshape==="triangle")
        this.drawTriangleEdit(e ,( shape.jsondimensions.end_X - shape.jsondimensions.start_X )*2, shape.jsondimensions.end_Y - shape.jsondimensions.start_Y);
      else if(this.selectedshape==="hexagon")
        this.drawPolygon(6,e);
      else if(this.selectedshape==="line")
        this.drawLineEdit(e);
      else if(this.selectedshape==="ellipse")
        this.drawEllipseEdit(e ,shape.jsondimensions.radiusX, shape.jsondimensions.radiusY
            , shape.jsondimensions.CenterX, shape.jsondimensions.CenterY);
    },
    /* General Canvas Methods */
    resizeCanvas() {
      let toolsBar= document.getElementById("toolsBar")
      let toolsBarWidth = toolsBar.clientWidth
      this.toolBarHeight = document.getElementById("toolBar").offsetHeight
      this.canvas.style.marginLeft= "60px"
      this.canvas.width = window.innerWidth-toolsBarWidth
      this.canvas.height = window.innerHeight-this.toolBarHeight
      this.selectCanvas.style.marginLeft = "60px";
      this.selectCanvas.width = window.innerWidth - toolsBarWidth;
      this.selectCanvas.height = window.innerHeight-this.toolBarHeight;
      toolsBar.style.height = this.canvas.height
    },
    drawCanvas(id){
     // this.canvas.addEventListener("mousemove",()=>{})
      DrawingCanvasMode = true
     console.log("ALOOOO"+this.shapesData.length)
      this.clearCanvas()
      for(let i = 0 ; i < this.shapesData.length; i++ ){
        if(this.shapesData[i].id === id &&!Copying ){
          console.log("deleted")
          continue
        }
        this.drawShapeProgrammatically(i)
      }
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      if(!Deleting)
      this.drawShapeProgrammatically(this.currentId)
      DrawingCanvasMode = false
    },
    clearCanvas(){
      this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
    },
    startEdit(){
      if(!Selected)return
      this.readDimension()
      this.drawCanvas(this.currentId)
      if(Resizing){
        this.setshape(this.currentShape.shapeType)
        drawing = true
        this.setStartCoordinates(startX,startY)
      }
    },
    finishEdit(){
      Selected = false
      this.context.beginPath();
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      if(Resizing){
        drawing = false;
      }
      if(Moving ||Resizing)
        this.updateShape("move")
      else if(Copying)
        this.updateShape("copy")
    },
    startShape(e){
      if(this.selectedshape === '') return;
      drawing = true
      this.setStartCoordinates(e)
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    },
    async finishShape() {
      drawing = false;
      this.context.beginPath();
      //if(!this.mousemoved) return;
      this.mousemoved = false;
      this.setDimensions()
      this.setstyle()
      style = JSON.stringify(style); 
      Dimension = JSON.stringify(Dimension);
      await this.sendShapeData(Dimension, style);
      await this.GetShapesData("/shape");
    },
    setShapeAttributes(lineColor,fillColor,lineOpacity,fill){
      this.context.lineWidth = lineOpacity;
      this.context.fillStyle = fillColor;
      this.fill = fill ;
      this.context.strokeStyle = lineColor;
    },
/* Set Coordinates */
    setDimensions(){
      if (this.selectedshape === "circle" || this.selectedshape === "ellipse") {
        Dimension = {
          radiusX: this.radiusx,
          radiusY: this.radiusy,
          CenterX: Math.abs(startX),
          CenterY: Math.abs(startY),
          start_X : startX,
          start_Y : startY,
          end_X : X,
          end_Y : Y
        }
      }
      else if (this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          || this.selectedshape === "triangle" || this.selectedshape === "rectangle" || this.selectedshape === "line") {
        Dimension = {
          start_X: startX,
          start_Y: startY,
          end_X: X,
          end_Y: Y
        }
      }
    },
    readDimension(shape = this.currentShape){
      if (this.selectedshape === "circle" || this.selectedshape === "ellipse") {
      
        this.radiusX = shape.jsondimensions.radiusX
        this.radiusY = shape.jsondimensions.radiusY
       this.setStartCoordinates(shape.jsondimensions.CenterX,shape.jsondimensions.CenterY)
      }
      else if (this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          || this.selectedshape === "triangle" || this.selectedshape === "rectangle" || this.selectedshape === "line") {
       
        this.setStartCoordinates(shape.jsondimensions.start_X,shape.jsondimensions.start_Y)
        this.setEndCoordinates(shape.jsondimensions.end_X,shape.jsondimensions.end_Y)
        this.width = Math.abs(shape.jsondimensions.start_X-shape.jsondimensions.end_X)
        this.height = Math.abs(shape.jsondimensions.start_Y-shape.jsondimensions.end_Y)
      }
    },
    setstyle(){
      style = {
        Color: this.currentCollor,
        fillColor: this.currentFillColor,
        Transparent: this.fill,
        lineWidth: this.currentLineWidth
      }
    },
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
       // console.log("SARA HELLO GIRL",X+" "+Y+" ")
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
     // console.log("hello"+this.selectedshape);
      await axios.post(apiUrl + "/shape",data)
    },
    async GetShapesData(para){
      await axios.get(apiUrl + para).then(Response => {
      // console.log("HELLO BABA " + Response.data)
        //console.log("LENGTH"+Object.keys(Response.data).length)
        this.shapesData=[];
        // for(let i =0 ;i < Object.keys(Response.data).length;i++) {
        //   // console.log("RESPOSE"+Response.data[i])
        //   this.shapesData[i] = {
        //     id: Response.data[i].id,
        //     shapeType: Response.data[i].shapeType,
        //     jsondimensions: JSON.parse(JSON.stringify(Response.data[i].jsondimensions)),
        //     jsonproperties: JSON.parse(JSON.stringify(Response.data[i].jsonproperties))
        //   }
        // }
        let indeces = Object.keys(Response.data)
        //console.log(length)
        for(let i =0 ;i <indeces.length;i++){
         console.log(Response.data[indeces[i]])
          this.shapesData[i] = {
            id : Response.data[indeces[i]].id,
            shapeType: Response.data[indeces[i]].shapeType,
            jsondimensions: JSON.parse(JSON.stringify(Response.data[indeces[i]].jsondimensions)),
            jsonproperties: JSON.parse(JSON.stringify(Response.data[indeces[i]].jsonproperties))
         }
       }
    })
    },
    async updateShape(state){
    //  this.setDimensions()
   //   console.log("HI ANA WIDTH"+this.width+" "+ this.height)
      this.selectedshape = this.currentShape.shapeType
      if (this.selectedshape === "circle" || this.selectedshape === "ellipse") {
        Dimension = {
          radiusX: this.radiusx,
          radiusY: this.radiusy,
          CenterX: Math.abs(X),
          CenterY: Math.abs(Y),
          start_X : X,
          start_Y : Y,
          end_X : parseFloat(X)+parseFloat(this.radiusx),
          end_Y : parseFloat(Y)+parseFloat(this.radiusy),
        }
      }
      else if (this.selectedshape === "pentagon" || this.selectedshape === "hexagon"
          || this.selectedshape === "triangle" || this.selectedshape === "rectangle" || this.selectedshape === "line") {
        Dimension = {
          start_X: X,
          start_Y: Y,
          end_X: parseFloat(X)+parseFloat(this.width),
          end_Y: parseFloat(Y)+parseFloat(this.height),
        }
      }
      await axios.get(apiUrl+"/copymove",{params:{
          dimensions  : Dimension,
          id:encodeURIComponent(this.currentId),
          state:encodeURIComponent(state)
        }
    })
     // console.log("HELLO MAMA "+ respnse)
      await this.GetShapesData("/"+"shape")
    //     params:{
    //       id: this.currentId,
    //       sX : this.currentShape.start_X,
    //       sY:this.currentShape.start_Y,
    //       eX : this.currentShape.end_X,
    //       eY : this.currentShape.end_Y
    // })
    },
    async eraseShapes(){
      this.shapesData = []
      this.clearCanvas()
       await axios.delete(apiUrl + "/shapes")
    //  console.log("OOO"+response.data)
    },
    async deleteShape(){
      const response =  await axios.get(apiUrl + "/delete",{ params: {id: this.currentId } })
      await this.GetShapesData("/shape");
      console.log(JSON.stringify(response))
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
      this.mousemoved = true;
    },
    drawTriangleEdit(e , base,height ) {
      //if(!Selection){return}
      
      this.context.putImageData(imageData,0,0);
      this.context.moveTo(e.offsetX,e.offsetY);
      this.context.lineTo(e.offsetX + base/2 ,e.offsetY + height);
      this.context.lineTo(e.offsetX - base/2 ,e.offsetY + height);
      this.context.lineTo(e.offsetX,e.offsetY);
      this.setEndCoordinates(e)
      this.height = height
      this.width = base/2 
      if(this.fill){
        this.context.fill();
      }
      this.setEndCoordinates(e);
      this.context.beginPath();
      this.context.stroke();
    },
    drawPolygon(numberOfSides ,e){
      if(!drawing && !DrawingCanvasMode){return}
      if(drawing){
        this.setEndCoordinates(e);
      }
      // console.log(X+" "+Y+" "+startX+" "+startY+" "+numberOfSides)
      if(!Selected)
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
      this.mousemoved = true;
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
      this.context.beginPath();
      this.mousemoved = true;
    },
    drawRectEdit(e, width, height){
      if(!Selected)
        return
      this.context.putImageData(imageData,0,0)
      if(this.fill){
        this.context.fill();
      }
      // this.width = width
      // this.height = height
      this.setEndCoordinates(e)
      this.context.strokeRect(X,Y,width,height);
      this.context.beginPath();
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
      this.context.beginPath();
      this.mousemoved = true;
    },
    drawLineEdit(e) // needs editing
    {
      if(!Selected)
        return
      this.context.putImageData(imageData,0,0)
      this.context.moveTo(e.offsetX, e.offsetY);
      this.context.lineTo(e.offsetX+this.width, e.offsetY+this.height);
      //this.context.beginPath();
      this.setEndCoordinates(e)
      this.context.stroke();
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
      this.mousemoved = true;
    },
    drawEllipseEdit(e , radiusX ,radiusY  ){
      if(!Selected){return}
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
      if(!drawing &&!DrawingCanvasMode)
        return
      if(drawing){
        this.setEndCoordinates(e)
        this.context.putImageData(imageData,0,0)
        this.radiusx = Math.sqrt(Math.pow((X - startX), 2) + Math.pow((Y - startY), 2))
      }
      this.context.arc(startX, startY, this.radiusx, 0, 2 * Math.PI)
      this.context.stroke()
      if(this.fill) this.context.fill();
      this.context.beginPath()
    }	,
    drawCircleEdit(e , Radius  ) {
    //  if( !editing){return}
      this.setEndCoordinates(e)
      this.context.putImageData(imageData,0,0)
      this.context.beginPath()
      this.context.arc(e.offsetX, e.offsetY, Radius, 0 , 2 * Math.PI)
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke()
    },
    drawShapeProgrammatically(id){
      //let shape = this.currentShape
      let shape = this.shapesData[id]
      if(!shape){
        return
      }
     // console.log(shape.shapeType);
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
        if(shapeType==="pentagon")
          this.drawPolygon(5,null);
        else if(shapeType==="rectangle")
          this.drawRect(null );
        else if(shapeType==="triangle")
          this.drawTriangle(null);
        else if(shapeType==="hexagon")
          this.drawPolygon(6,null);
        else if(shapeType==="line")
          this.drawLine(null);
      }
      this.context.beginPath()
    },
    /****SELECTION****/
    checkColor(e)
    {
      const rgbaClick = this.getRGBA(e);
      console.log(rgbaClick);
      const bool = (JSON.stringify(this.rgbaCanvas) === (JSON.stringify(rgbaClick)));
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
    async select(e)
    {
      Selected = false
      if(this.checkColor(e))
      {
        console.log("wrong");
        this.currentId = null;
        return;
      }
      console.log(this.shapesData.length)
      for(let i = this.shapesData.length - 1; i>=0; i--)
      {
        const shape = this.shapesData[i];
        console.log("ELCNAVAS EL5AFEYYA"+JSON.stringify(shape.jsondimensions))
        this.selectContext.beginPath();
        if(shape.shapeType === "rectangle") {
          this.rectSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        }else if(shape.shapeType === "circle" ){
          this.circleSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y, shape.jsondimensions.radiusX);
        }else if(shape.shapeType === "triangle"){
          this.triangleSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        }else if(shape.shapeType === "ellipse") {
          this.ellipseSelected(shape.jsondimensions.end_X, shape.jsondimensions.end_Y,shape.jsondimensions.radiusX,shape.jsondimensions.radiusY,shape.jsondimensions.CenterX, shape.jsondimensions.CenterY);
        }else if(shape.shapeType === "pentagon"){
          this.polygonSelected(5, shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        }else if(shape.shapeType === "hexagon") {
          this.polygonSelected(6,shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        }else if(shape.shapeType === "line") {
          this.lineSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        }
        if(this.selectContext.isPointInPath(e.offsetX , e.offsetY ))
        {
          this.selectContext.closePath();
          this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
         Selected = true
          this.currentId = shape.id;
         this.currentShape = shape
          console.log(shape.id)
          console.log("Selected")
          return;
        }
        this.currentId = null;
        this.selectContext.closePath();
        this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
      }
      console.log("not Selected")
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
      this.selectContext.setTransform(1.2, 0, 0, 1.2, -xe*0.14, -ye*0.18);
      this.selectContext.arc(x,y,r,0, 2*Math.PI);
      this.selectContext.fill();
    },
    triangleSelected(xs, ys, xe, ye)
    {
      const b = (xe - xs) * 2;
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
      const radius = xe - xs;
      const step = 2 * Math.PI / n;
      const shift = (Math.PI / 180.0) * -18;
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
  border: 3px solid black;
  display: none;
}
</style>