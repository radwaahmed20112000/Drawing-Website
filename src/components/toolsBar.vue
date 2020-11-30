<template>
   <div class="grid-container">
     <button  class="grid-item" id="tools"><span style='color:white;'>Tools</span> </button>
     
     <button  class="grid-item" id="select" title="select" >   <i class="material-icons">near_me</i> </button>
     <button  class="grid-item" id="delete" title="delete" >   <i class="material-icons">delete</i> </button>
      <button  class="grid-item" id="copy" title="copy" >   <i class="material-icons">content_copy</i> </button>
    <button  class="grid-item" id="move" title="move" >   <i class="material-icons">content_cut</i> </button>
     <button  class="grid-item" id="resize" title="resize" >   <i class="material-icons">photo_size_select_small</i> </button>

     <button   class="grid-item" id="brush" title="brush" > <i class="material-icons">brush</i>   </button>
     <button  class="grid-item" id="pen" title="pen" >   <i class="material-icons">create</i> </button>
	

     <button  class="grid-item" id="line" title="line" >   <span style='font-size:20px;'>&#9586;</span> </button>
     <button  class="grid-item" id="circle" title="circle" >   <i class="material-icons">panorama_fish_eye</i> </button>   
     <button  class="grid-item" id="triangle" title="triangle" >   <i class="material-icons">change_history</i> </button>
    <button  class="grid-item" id="rectangle" title="rectangle" >   <span style='font-size:30px;'>&#9645;</span> </button>
    <button  class="grid-item" id="eclipse" title="eclipse" >   <span style='font-size:30px;'>&#x2B2D;</span> </button>
    <button  class="grid-item" id="hexagon" title="hexagon" >   <span style='font-size:30px;'>	&#11041;</span> </button>
      <button  class="grid-item" id="pentagon" title="pentagon" >   <span style='font-size:30px;'>	&#11040;</span> </button>
 
   <button  class="grid-item" id="format_color_fill" title="format_color_fill" >   <i class="material-icons">format_color_fill</i> </button>
      
   </div>

   
</template>



<script>
export default {
  name: 'toolsBar',

}
</script>


<style scoped>
.grid-container {
  width: 70px;
  display: grid;
  grid-template-columns: auto ;
}

.grid-item {
  background-color:#e6e6e6;
  border: 1px solid white;
  height: 40px;
  text-align: center;
  
  
}
#tools{
  background-color: #595959;
}

</style>
