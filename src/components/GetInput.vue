<template>
    <div id="Inputs">
        <input type ="number" id = "num_input" class = "LineOpacity" max = "100" min = "0" value="100" @change="changeRange()">
        <button @click="rangeDisplay()" ><i class="material-icons">keyboard_arrow_down</i></button>
        <div id="ranger">
            <input type="range" id ="range_input" class ="LineOpacity" max = "100" min="0" value="100" @change="changeNumber()">
        </div>
    </div>
</template>

<script>
export default {
  name:"GetInput",
    props:{
      msg:String 
    },
    methods:{ 
        rangeDisplay(){
        var range = document.getElementById("range_input"),
            range_div = document.getElementById("ranger");
        if(range.style.opacity == "0")
        {
            range.style.opacity = "1";
            range_div.style.opacity = "1";
            range.disabled = false;
        }else{
            range.style.opacity = "0";
            range_div.style.opacity = "0";
            range.disabled = true;
        }
        },
        changeRange()
        {
        var num = document.getElementById("num_input"),
            range = document.getElementById("range_input");      
        range.value = num.value;
        },
        changeNumber()
        {
        var num = document.getElementById("num_input"),
            range = document.getElementById("range_input");
        num.value = range.value;
        }
  }
}
</script>
<style scoped>
 button, input[type=number], #Inputs{
  display: block;
  float: left;
}
i{
  color:#DE3163;
  background-color: rgb(255, 255, 255);
}
button {
  border: none; 
  color: #DE3163;
  background-color: #ffffff; 
  font-size: 1rem; 
  margin: 0%;
  padding: 0%;
  width:25px;height: 25px;
  text-align: center;
  outline:none;
}
input[type=number]{
  width: 50px;
  height: 25px;
  border: none; 
  color: #DE3163;
  margin: 0;
  padding: 0;
  background-color: #ffffff; 
}
input[type="range"]{
  opacity: 0;
  top: 20%;
  position: absolute;
  display: block;
}
#ranger{
  border:0.2px solid #DE3163;
  background-color: #ffffff;
  top: 90%;
  height: 35px;
  width:140px;
  position: relative;
  display: block;
  opacity: 0;
}

#Inputs{
  padding: 2px;
  margin: 0%;
  height: 30px;
  width:80px;
}

</style>