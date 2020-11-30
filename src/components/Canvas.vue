<template>
  <div>
    
  <canvas id="Canvas" ></canvas>
  <toolsBar @setShape="setShape" />
  
  
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
export default {
name: "Canvas",
components: {
   toolsBar
    
    },

  
data(){
  return{
    canvas :null,
    context : null,
    selectedshape:'',
    
  }
  },
  mounted() {
    this.canvas = document.getElementById("Canvas")
    this.context = this.canvas.getContext("2d")

    this.resizeCanvas()
    window.addEventListener('resize',() => {
      this.resizeCanvas()
    })

       this.canvas.addEventListener("mousedown",this.startShape)
     this.canvas.addEventListener("mouseup",this.finishShape)
     this.canvas.addEventListener("mousemove",this.selectShape)
    
  },
  methods:{
    selectShape(e){
      if(this.selectedshape=="circle")
      this.drawCircle(e);
      else if(this.selectedshape=="pentagon")
      this.drawpentagon(e);
      else if(this.selectedshape=="rectangle")
      this.drawRect(e);
      else if(this.selectedshape=="triangle")
      this.drawTriangle(e);
      else if(this.selectedshape=="hexagon")
      this.drawRect(e);
      else if(this.selectedshape=="line")
      this.drawline(e);
      else if(this.selectedshape=="eclipse")
      this.drawEllipse(e);

    },
     setShape(value){
      
      this.selectedshape=value;
      console.log(this.selectedshape)
      
    },

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
    this.context.fill();

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
    var base = (X-startX)*2;
    this.context.moveTo(startX,startY);
    this.context.lineTo(X,Y);
    this.context.lineTo((X-base),Y);
    this.context.lineTo(startX,startY);
    this.context.stroke();
    this.context.fill();
    this.context.beginPath();
  },
/* Ellipse Drawing Method */
  drawEllipse(){

  },


/* pentagon Drawing Method */
  drawpentagon(e){
    if(!drawing){
        return
    }
    this.context.putImageData(imageData,0,0);
    this.setEndCoordinates(e);  
    var numberOfSides=5; 
    var radius=X-startX;
    //Precalculate step value
    var step  = 2 * Math.PI / numberOfSides;
    //Quick fix 
    var shift = (Math.PI / 180.0) * -18;
   
    for(var i=0;i<=numberOfSides;i++){
      var curStep = i * step + shift;
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
  border: darkgrey 2px solid
}
</style>