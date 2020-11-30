<template>
  <div id="app">
    <Tools></Tools>
    <Canvas></Canvas>

  </div>
</template>

<script>
import Tools from './components/Tools.vue';
//import ShapeBar from './components/ShapeBar.vue';
//import LineBar from './components/LineBar.vue';
import Canvas from './components/Canvas.vue';
// import toolsBar from './components/toolsBar.vue'
export default {
  name: 'App',
  components: {
    Canvas,
    //ShapeBar,
    //LineBar,
    Tools,
  },
  data() {

  },
  mounted() {

  },
  methods: {
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
</style>
