<template>
  <canvas id="Canvas" ></canvas>
</template>

<script>
let drawing = false;
let startX = 0;
let startY = 0;
let imageData = null;
let X = 0;
let Y = 0;
export default {
name: "Canvas",data(){
  return{
    canvas :null,
    context : null
  }
  }, mounted() {
    this.canvas = document.getElementById("Canvas")
    this.context = this.canvas.getContext("2d")

    this.resizeCanvas()
    window.addEventListener('resize',() => {
      this.resizeCanvas()
    })
    // this.canvas.addEventListener("mousedown",this.startShape)
     this.canvas.addEventListener("mousedown",this.startSketch)
     this.canvas.addEventListener("mousemove",this.sketch)
    // this.canvas.addEventListener("mousemove",this.drawRect)
    // this.canvas.addEventListener("mousemove",this.drawCircle)
    // this.canvas.addEventListener("mousemove",this.drawTriangle)
    this.canvas.addEventListener("mouseup",this.finishShape)
  },
  methods:{
/* General Canvas Methods */
  resizeCanvas :function() {
    const toolBarHeight = document.getElementById("toolBar").offsetHeight
    this.canvas.width = window.innerWidth
    this.canvas.height = window.innerHeight-toolBarHeight
  },
  clearCanvas(){
    this.context.clearRect(0,0,window.innerWidth,window.innerHeight);
  },
  startShape(e){
    drawing = true
    this.setStartCoordinates(e)
    imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
  },
  finishShape(){
    drawing = false
    this.context.beginPath()
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
/* Free Sketching Methods */
  startSketch : function(e){
    drawing = true
    this.sketch(e)
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
/* Rectangle Drawing Method*/
  drawRect : function (e){
    if(drawing === false)
      return
    this.setEndCoordinates(e)
    let width = X-startX
    let height = Y-startY
    this.context.putImageData(imageData,0,0)
    this.context.strokeRect(startX,startY,width,height)

  },
/* Circles Drawing Method*/
  drawCircle:function (e) {
    if(drawing === false)
      return
    this.context.putImageData(imageData,0,0)
    this.setEndCoordinates(e)
    let radius = Math.sqrt(Math.pow((X - startX), 2) - Math.pow((Y - startY), 2))
    this.context.beginPath()
    this.context.arc(startX, startY, radius,0,2*Math.PI)
    this.context.stroke()
  },
/* Triangle Drawing Method*/ 
  drawTriangle(e){
    if(!drawing){
      return
    }
    this.context.putImageData(imageData,0,0);
    this.setEndCoordinates(e);
    var base = (X-startX)*2;
    this.context.moveTo(startX,startY);
    this.context.lineTo(X,Y);
    this.context.lineTo((X-base),Y);
    this.context.lineTo(startX,startY);
    this.context.stroke();
    this.context.beginPath();
  },
/* Ellipse Drawing Method */
  drawEllipse(){

  }
}
}
</script>

<style scoped>
#Canvas{
  padding: 0;
  margin: 0;
  border: darkgrey 2px solid
}
</style>