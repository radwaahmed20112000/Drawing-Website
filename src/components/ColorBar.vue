<template>
  <div>
    <span class="tab"></span>
    <label class="label_bar">{{msg}}</label>
    <span class="space"></span>
    <input type="color" class="color_picker" id="colorpickerLine" >
  </div>
</template>
<script>
export default {
  name:"ColorBar",
  props:{
    msg:String ,
    color:String,
    id:String
  },
  mounted() {
    var colo = document.getElementById("colorpickerLine");
    console.log(this.color);
    colo.id = this.id;
    colo.value = "#" + this.color;
  },
}
</script>