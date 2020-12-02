<template>
  <div>
    <canvas id="canvasSelect"></canvas>
    <canvas id="Canvas" ></canvas>
    <toolsBar id="toolsBar" @setshape="setshape" @setundo="setUndoMode"></toolsBar>
  </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8086"
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
import toolsBar from '@/components/toolsBar.vue'
let drawing = false;
let editing = false;
let DrawingCanvasMode = true;
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
      currentId : null
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
    document.getElementById("move").addEventListener("click", ()=>{
      if(DrawingCanvasMode)
      {
        this.currentId = null;
        this.canvas.addEventListener("mousedown",this.startShape)
        this.canvas.addEventListener("mousemove",this.selectShape)
        this.canvas.addEventListener("mouseup",this.finishShape)
      }
      else
      {
        this.selectedshape = '';
        this.canvas.addEventListener("click", this.select);
        this.canvas.addEventListener("mousedown",this.startEdit)
        this.canvas.addEventListener("mousemove",this.moveShape)
        this.canvas.addEventListener("mouseup",this.finishEdit)
      }
    })
    document.getElementById("delete").addEventListener("click",this.eraseShapes)
    this.canvas.addEventListener("mousedown",this.startShape)
    this.canvas.addEventListener("mousemove",this.selectShape)
    this.canvas.addEventListener("mouseup",this.finishShape)
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
      if(!drawing) return;
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
    },
    setselectmode(value)
    {
      DrawingCanvasMode = !value;
    },
    setshape(value){
      DrawingCanvasMode = true;
      this.selectedshape=value;
    },
    moveShape(e){
      this.drawShapeEdit(e,this.currentId);
    },
    drawShapeEdit(e,id=0){
      this.setshape(this.shapesData[id].shapeType)
      if(this.selectedshape==="circle")
        this.drawCircle(e)
      else if(this.selectedshape==="pentagon")
        this.drawPolygonEdit(5,e );
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
      this.drawShapeProgrammatically(0)
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
      if(this.selectedshape === '') return;
      drawing = true
      this.setStartCoordinates(e)
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    },
    async finishShape() {
      console.log(this.selectedshape);
      drawing = false
      this.context.beginPath();
      if(!this.mousemoved) return;
      this.mousemoved = false;
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
          CenterY: Math.abs(startY),
            start_X : startX,
            start_Y : startY,
            end_X : X,
            end_Y : Y
        }
         
      } else if (this.selectedshape === "triangle" || this.selectedshape === "rectangle"||
           this.selectedshape === "pentagon" || this.selectedshape === "hexagon")
           {
           
        Dimension = {
          start_X: startX,
          start_Y: startY,
          end_X: X,
          end_Y: Y
        }
      }
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
       console.log("hello"+this.selectedshape);
      await axios.post(apiUrl + "/shape",data)
    },
    async GetShapesData(para){
      await axios.get(apiUrl + para).then(Response => {
        console.log(Response.data)
        console.log("LENGTH"+Object.keys(Response.data).length)
        this.shapesData=[];
                for(let i =0 ;i < Object.keys(Response.data).length;i++){
                  console.log("RESPOSE"+Response.data[i])
                  this.shapesData[i] = {
                    id : Response.data[i].id,
                    shapeType: Response.data[i].shapeType,
                    jsondimensions: JSON.parse(JSON.stringify(Response.data[i].jsondimensions)),
                    jsonproperties: JSON.parse(JSON.stringify(Response.data[i].jsonproperties))
                  }
               console.log(this.shapesData);
                }})
    },
    async eraseShapes(){
      this.shapesData = []
      this.clearCanvas()
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
      this.context.beginPath();
      this.mousemoved = true;
    },
    drawRectEdit(e, width, height){
      if(!editing)
        return
      this.context.putImageData(imageData,0,0)
      if(this.fill){
        this.context.fill();
      }
      this.context.strokeRect(e.offsetX,e.offsetY,width,height);
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
      this.context.putImageData(imageData,0,0);
      this.context.beginPath()
      if(!editing) {
        this.radiusx = Math.sqrt(Math.pow((X - startX), 2) + Math.pow((Y - startY), 2))
        this.context.arc(startX, startY, this.radiusx, 0, 2 * Math.PI)
      }
      else
        this.context.arc(X, Y, this.radiusx, 0, 2 * Math.PI)
      if(this.fill){
        this.context.fill();
      }
      this.context.stroke()
    }	,
    drawShapeProgrammatically(id){
      let shape = this.shapesData[id]
      console.log(shape.shapeType);
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
        this.currentId = null;
        return;
      }
      for(let i = this.shapesData.length - 1; i>=0; i--)
      {
        const shape = this.shapesData[i];
        this.selectContext.beginPath();
        if(shape.type === "rectangle") this.rectSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        else if(shape.type === "circle" ) this.circleSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y, shape.jsondimensions.radiusX);
        else if(shape.type === "triangle") this.triangleSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        else if(shape.type === "ellipse") this.ellipseSelected(shape.shape.jsondimensions.end_X, shape.shape.jsondimensions.end_Y,
            shape.jsondimensions.radiusX,shape.jsondimensions.radiusY,shape.jsondimensions.CenterX, shape.jsondimensions.CenterY);
        else if(shape.type === "pentagon") this.polygonSelected(5, shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        else if(shape.type === "hexagon") this.polygonSelected(6,shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        else if(shape.type === "line") this.lineSelected(shape.jsondimensions.start_X, shape.jsondimensions.start_Y,
            shape.jsondimensions.end_X, shape.jsondimensions.end_Y);
        if(this.selectContext.isPointInPath(e.offsetX , e.offsetY ))
        {
          console.log(i);
          this.selectContext.closePath();
          this.selectContext.clearRect(0,0,this.selectCanvas.width, this.selectCanvas.height);
          this.currentId = shape.id;
          return;
        }
        this.currentId = null;
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
  display:none;
}
</style>