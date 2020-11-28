<template>
  <div class="shape">
    <span class="tab"></span>
    <label class="label_bar">Color: </label>
    <span class="space"></span>
    <input type="color" class="color_picker" id="colorpickerLine" value="#DC143C">
    <span class="tab"></span>
    <label class="label_bar">Opacity: </label>
    <span class="space"></span>
    <get-input></get-input>
    <span class="tab"></span>
    <label class="label_bar">Diameter: </label>
    <span class="space"></span>
    <get-input></get-input>
  </div>    
</template>

<script>
import GetInput from './GetInput.vue';
export default {
  components: { GetInput },
  name: 'LineBar',
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>