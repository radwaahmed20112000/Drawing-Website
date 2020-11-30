<template>
  <div>
  <canvas id="Canvas" ></canvas>
    <toolsBar @setShape="setShape" />
    <canvas id="canvasSelect"></canvas>
  </div>
</template>

<script>
import toolsBar from '@/components/toolsBar.vue'
let drawing = false;

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
props:['shapedrawing']
  ,
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
    mousemoved : false
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
    this.setShapeAttributes("red","blue",7);
    this.canvas.addEventListener("mouseup",this.finishShape)
    this.canvas.addEventListener("click", this.select)
  },
  methods:{
    selectShape(e){
        if(this.selectedshape==="circle")
      this.drawCircle(e);
      else if(this.selectedshape==="pentagon")
      this.drawpentagon(e);
      else if(this.selectedshape==="rectangle")
      this.drawRect(e);
      else if(this.selectedshape==="triangle")
      this.drawTriangle(e);
      else if(this.selectedshape==="hexagon")
      this.drawRect(e);
      else if(this.selectedshape==="line")
      this.drawline(e);
      else if(this.selectedshape==="eclipse")
      this.drawEllipse(e);

    },
     setShape(value){

      this.selectedshape=value;
      console.log(this.selectedshape)

    },

/* General Canvas Methods */
  resizeCanvas :function() {
    this.toolBarHeight = document.getElementById("toolBar").offsetHeight
    this.canvas.width = window.innerWidth
    this.canvas.height = window.innerHeight-this.toolBarHeight
    this.selectCanvas.width = window.innerWidth
    this.selectCanvas.height = window.innerHeight-this.toolBarHeight
  },
  // clearCanvas(){
  //   this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
  // },
  startShape(e){
    drawing = true
    this.setStartCoordinates(e)
    imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    this.StartPos = [startX, startY];
  },
  finishShape(e){
    drawing = false
    this.context.beginPath();
    this.endPos = [e.offsetX, e.offsetY];
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
    this.id++;
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
  drawRect : function (e){
    if(drawing === false)
      return
    this.setEndCoordinates(e)
    width = X-startX
    height = Y-startY
    this.context.strokeStyle = "black";
    this.context.lineWidth = 10;
    this.context.putImageData(imageData,0,0)
    this.context.strokeRect(startX,startY,width,height)
    this.mousemoved = true;
  },

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

  drawCircle:function (e) {
    if(drawing === false)
      return
    this.context.putImageData(imageData,0,0)
    this.setEndCoordinates(e)
    let radius = Math.sqrt(Math.pow((X - startX), 2) - Math.pow((Y - startY), 2))
    this.context.beginPath()
    this.context.arc(startX, startY, radius,0,2*Math.PI)
    this.context.fill();
    this.context.stroke()
  },
/* Triangle Drawing Method*/
  drawTriangle(e){
    if(!drawing){
      return
    }
    this.context.putImageData(imageData,0,0);
    this.setEndCoordinates(e);
    const base = (X - startX) * 2;
    this.context.moveTo(startX,startY);
    this.context.lineTo(X,Y);
    this.context.lineTo((X-base),Y);
    this.context.lineTo(startX,startY);
    this.context.stroke();
    this.context.fill();
    this.context.beginPath();
  },
 //Ellipse Drawing Method
    drawEllipse(e){
      if(!drawing){
        return
      }
      this.setEndCoordinates(e);
      const radiusX = Math.abs(X - startX) / 2,
          radiusY = Math.abs(Y - startY) / 2,
          centreX = Math.abs(X - radiusX),
          centreY = Math.abs(Y - radiusY);
      this.context.putImageData(imageData,0,0);
      console.log(X,Y,centreX,centreY,radiusX,radiusY);
      this.context.ellipse(centreX,centreY,radiusX,radiusY,Math.PI , 0 ,2 * Math.PI);
      this.context.stroke();
      this.context.fill();
      this.context.beginPath();
    },
  drawpentagon(e){
    if(!drawing){
        return
    }
    this.context.putImageData(imageData,0,0);
    this.setEndCoordinates(e);
    const numberOfSides = 5;
    const radius = X - startX;
    //Precalculate step value
    const step = 2 * Math.PI / numberOfSides;
    //Quick fix
    const shift = (Math.PI / 180.0) * -18;

    for(let i=0; i<=numberOfSides; i++){
      const curStep = i * step + shift;
      this.context.lineTo(X+radius*Math.cos(curStep),Y+radius*Math.sin(curStep));
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
  background: rebeccapurple;

}
#canvasSelect{
  padding: 0;
  margin : 0;
  opacity: 0;
}
</style>