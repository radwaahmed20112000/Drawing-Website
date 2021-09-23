<template>
    <div id="Inputs">
        <input type ="number" id = "num_input"  max = "100" min = "0" value="100" @change="changeRange()">
        <button @click="rangeDisplay()" ><i class="material-icons">keyboard_arrow_down</i></button>
        <div id="ranger" class="div_ranger">
            <input type="range" id ="range_input"  max = "100" min="0" value="100" @change="changeNumber();$emit('setwidth',lineValue)" >
        </div>
    </div>
</template>

<script>
export default {
  name:"GetInput",
    props:{
      numid:String,
      rangeid:String,
      divid:String
    },
    data() {
        return {
            num_id:this.numid,
            range_id:this.rangeid,
            div_id:this.divid,
            lineValue:'',
        }
    },
    methods:{ 
        rangeDisplay(){
        var range = document.getElementById(this.range_id),
            range_div = document.getElementById(this.div_id);
        console.log(range.id,range_div.id);
        if(range.style.opacity === "0")
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
        var num = document.getElementById(this.num_id),
            range = document.getElementById(this.range_id);
        range.value = num.value;
         this.lineValue=range.value
        console.log(this.lineValue)
        },
        changeNumber()
        {
        var num = document.getElementById(this.num_id),
            range = document.getElementById(this.range_id);
        num.value = range.value;
        this.lineValue=num.value
        console.log(this.lineValue)
        }
  },
  mounted() {
      var div =  document.getElementById("ranger"),
        range =  document.getElementById("range_input"),
        num = document.getElementById("num_input");
        num.id = this.num_id;
        range.id = this.range_id;
        div.id = this.div_id;
        console.log(num.id,range.id,div.id);
        console.log(this.num_id,this.range_id,this.div_id);
  },
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
.div_ranger{
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