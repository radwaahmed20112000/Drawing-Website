<template>
  <div class="shape">
    <span class="tab"></span>
    <label class="label_bar">Line Color: </label>
    <span class="space"></span>
    <input type="color" class="color_picker" id="colorpickerLine" value="#DC143C">
    <span class="tab"></span>
    <label class="label_bar">Line Opacity: </label>
    <span class="space"></span>
    <get-input msg=""></get-input>
    <span class="tab"></span>
    <label class="label_bar">FillColor:</label>
    <span class="space"></span>
    <input type="color" id="colorpickerShape" class="color_picker" value="#F08080">
  </div>
</template>

<script>
import GetInput from './GetInput.vue'
export default {
  components: { GetInput },
  name: 'ShapesBar',
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  scoped>


</style>
