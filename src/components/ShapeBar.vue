<template>
  <div class="shape">
    <color-bar  msg="Line Color:" color="DE3163" id="LineColor" ></color-bar>

    <span class="tab"></span>
    <label class="label_bar">Line Opacity: </label>
    <span class="space"></span>
    <get-input numid="numLineOpacity" divid="divLineOpacity" rangeid="rangeLineOpacity"></get-input>

    <color-bar  msg= "Fill Color:" color="F08080" id="fillColor"></color-bar>
  </div>
</template>

<script>
import ColorBar from './ColorBar.vue'
import GetInput from './GetInput.vue'
export default {
  components: { GetInput, ColorBar },
  name: 'ShapesBar',
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  scoped>


</style>
