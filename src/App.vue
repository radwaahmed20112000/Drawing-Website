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
   <Canvas></Canvas>
  </div>
</template>

<script>
import Canvas from "@/components/Canvas";
export default {
  name: 'App',
  components: {
    Canvas
  }
  ,
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
/*NAVIGATION BAR STYLING*/
#toolBar {
  text-align: left;
  background-color: black;
  font-weight: 600;
  font-size: 15px;
  padding: 12px;
  width: 98.7vw;
  margin-left: -0.9%;
  margin-top: -1.3%;
  color: white;
  height: 50%;
}
#toolBar div{
  display: inline-block;
  margin-right: 35px;
  margin-left: 10px;
  font-size: 17px;
  cursor: pointer;
  height: 35px;
}
#appLogo {
  display: inline-block;
}
#appLogo h1{
  font-family: 'Jasmina FY';
  display: inline-block;
  margin-bottom: 0;
  margin-top: 10px;
  font-size: 40px;
}
#toolBar i {
  color: #DE3163 ;
}
#appLogo span {
  color: #DE3163;
}
#appLogo h4 {
  font-size: 15px;
  font-family: 'Jasmina FY';
  margin-top: 5px;
}
#saveDrawing ul, #edit ul{
  position: absolute;
  list-style-type: none;
  background-color:rgba(0, 0, 0, 0.616) ;
  margin-top: 5px;
  display: none;
}
#saveDrawing ul li, #edit ul li {
  margin-left: -40px;
  margin-bottom: 5px;
  height: 40px;
  width: 160px;
  padding-left:10px ;
  padding-top: 10px;
  cursor: pointer;
}
#saveDrawing ul li:hover, #edit ul li:hover {
  background-color: #DE3163;
}
/*END NAVIGATION BAR STYLING*/

</style>
