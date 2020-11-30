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
    // this.canvas.addEventListener("mousedown",this.startSketch)
    // this.canvas.addEventListener("mouseup",this.finishSketch)
    // this.canvas.addEventListener("mousemove",this.sketch)
    this.canvas.addEventListener("mousedown",this.startRect)
    this.canvas.addEventListener("mouseup",this.finishRect)
    this.canvas.addEventListener("mousemove",this.drawRect)
    // this.canvas.addEventListener("mousedown",this.startCircle)
    // this.canvas.addEventListener("mouseup",this.finishCircle)
    // this.canvas.addEventListener("mousemove",this.drawCircle)
  },
  methods:{
  resizeCanvas :function() {
    const toolBarHeight = document.getElementById("toolBar").offsetHeight
    this.canvas.width = window.innerWidth
    this.canvas.height = window.innerHeight-toolBarHeight
  },
  setStartCoordinates : function (e){
    startX = e.clientX-this.canvas.offsetLeft
    startY = e.clientY-this.canvas.offsetTop
  },
    setEndCoordinates : function (e){
      X = e.clientX-this.canvas.offsetLeft
      Y = e.clientY-this.canvas.offsetTop
    },
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
  finishSketch : function(){
    drawing = false
    this.context.beginPath()
  },
  startRect : function (e){
    drawing = true
    this.setStartCoordinates(e)
    imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
    imageData.
    this.drawRect(e)
  },
  drawRect : function (e){
    if(drawing === false)
      return
    this.setEndCoordinates(e)
    let width = X-startX
    let height = Y-startY
    this.context.putImageData(imageData,0,0)
    this.context.strokeRect(startX,startY,width,height)
  },
  finishRect : function (){
    drawing = false
    this.context.beginPath()
    // var data = this.context.getImageData(0, 0, this.canvas.width, this.canvas.height);
    // console.log(data);
    // console.log(JSON.stringify(data));
  },
    startCircle : function (e){
      drawing = true
      this.setStartCoordinates(e)
      imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height)
      this.drawCircle(e)
    },
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
    finishCircle:function (){
      drawing = false
      this.context.beginPath()
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