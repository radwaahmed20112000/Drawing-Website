<template>
  <div id="app">
    <div id="toolBar">
      <div id="appLogo">
        <h1><span>i</span>nspire <i class="fa fa-paint-brush"></i></h1>
        <h4>Best Drawing & Painting App</h4>
      </div>
      <div id="newDrawing"><i class="fa fa-plus"></i> New Drawing</div> 
      <div id="open"><i class="fa fa-folder-open"></i> Open Drawing</div>
      <div id="saveDrawing"><i class="fa fa-save"></i> Save Drawing
      <ul id="saveList">
        <li>Save as XMl file</li>
        <li>Save as an image</li>
        <li>Save as JSON file</li>
      </ul>
      </div>
      <div id="edit"><i class="fa fa-edit"></i> Edit
       <ul id="editList">
        <li>Undo</li>
        <li>Redo</li>
        <li>Copy</li>
        <li>Paste</li>
      </ul>
      </div>
    </div>
    <div class="shape"></div>
   
   
    
    
    <Tools></Tools>
    <Canvas></Canvas>
    <ToolsBar></ToolsBar>

  </div>
</template>

<script>

import Canvas from "@/components/Canvas.vue";
import Tools from './components/Tools.vue';
//import ShapeBar from './components/ShapeBar.vue';
//import LineBar from './components/LineBar.vue';

export default {
  name: 'App',
  components: {
    Canvas,
    //ShapeBar,
    //LineBar,
    ToolsBar,
    Tools,
  },
 

  mounted() {
    //NAVIGATION BAR SETTINGS : 
    const saveDrawing = document.getElementById("saveDrawing");
    const saveList = document.getElementById("saveList");
    const edit = document.getElementById("edit");
    const editList = document.getElementById("editList");
    saveDrawing.addEventListener('mouseover', () =>{
      saveList.style.display = "block";
    });
    saveDrawing.addEventListener('mouseout', () =>{
      saveList.style.display = "none";
    });
    edit.addEventListener('mouseover', () =>{
      editList.style.display = "block";
    });
    edit.addEventListener('mouseout', () =>{
      editList.style.display = "none";
    });
    //End Navigation Bar

  },

  methods:{
   
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
