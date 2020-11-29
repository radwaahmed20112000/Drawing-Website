<template>
  <div id="app">
    <Tools></Tools>
    <canvas id="canvas"></canvas>
  </div>
</template>

<script>
import Tools from './components/Tools.vue'
export default {
  name: 'App',
  components: {
    Tools
  },
  data() {
    return {
      canvas : null,
      ctx : null,
      painting: false,
      x : 0,
      y : 0,
      endX : 0,
      endY : 0,
      image : null,
      objects : [],
      id : 0
    }
  },
  mounted() {
    this.canvas  = document.getElementById("canvas");
    this.ctx = this.canvas.getContext("2d");
    this.canvas.width = window.innerWidth ;
    this.canvas.height = window.innerHeight - window.innerHeight * 0.164;
    window.addEventListener('resize', ()=> {
      this.canvas.width = window.innerWidth ;
      this.canvas.height = window.innerHeight - window.innerHeight * 0.164;
    });
    this.canvas.addEventListener('mousedown', this.start);
    this.canvas.addEventListener('mouseup', this.finish);
    this.canvas.addEventListener('mousemove', this.drawRect);
    this.canvas.addEventListener('click', this.check);
  },
  methods: {
    start: function(e)
    {
      this.painting = true;
      this.ctx.beginPath();
      this.ctx.strokeStyle = "green";
      this.x = e.offsetX;
      this.y = e.offsetY;
      this.image = this.ctx.getImageData(0,0, this.canvas.width, this.canvas.height);
    },
    drawRect: function(e){
      if(!this.painting) return;
      this.ctx.lineWidth = 7;
      this.endX = e.offsetX - this.x;
      this.endY = e.offsetY - this.y;
      this.ctx.putImageData(this.image, 0,0);
      this.ctx.strokeRect(this.x, this.y, this.endX, this.endY );
      console.log(e.offsetX);      
    },
    finish: function()
    {
      this.painting = false;
      var object = {color : "black", x : this.x, y : this.y, image : this.ctx.getImageData(0,0, this.canvas.width, this.canvas.height), width : this.endX, height : this.endY};
      this.objects[this.id] = object;
      this.id++;
      for(var i=0; i<this.objects.length; i++)
      {
        console.log(this.objects[i]);
      }
    },
    getColorIndicesForCoord: function(x, y, width) {
      var red = y * (width * 4) + x * 4;
      return [red, red + 1, red + 2, red + 3];
    },
    check: function(e){
      var myImage = this.ctx.getImageData(0,0, this.canvas.width, this.canvas.height);
      var colors = this.getColorIndicesForCoord(e.offsetX, e.offsetY, this.canvas.width);
      console.log(colors);
      console.log(myImage.data[colors[0]]);
      console.log(myImage.data[colors[1]]);
      console.log(myImage.data[colors[2]]);
      console.log(myImage.data[colors[3]]);
    }
  }
}
</script>

<style>
@font-face {
    font-family: 'Jasmina FY';
    src: url('myfonts/JasminaFY-Regular.eot');
    src: local('myfonts/Jasmina FY Regular'), local('JasminaFY-Regular'),
        url('myfonts/JasminaFY-Regular.eot?#iefix') format('embedded-opentype'),
        url('myfonts/JasminaFY-Regular.woff2') format('woff2'),
        url('myfonts/JasminaFY-Regular.woff') format('woff'),
        url('myfonts/JasminaFY-Regular.ttf') format('truetype');
    font-weight: normal;
    font-style: normal;
}
#app {
  margin: 0;
  padding: 0;
  font-family: 'Open sans', sans-serif;
}
#canvas {
  background-color: #ffffff;
  margin : 0;
  margin-left: -0.7%;
}
</style>
