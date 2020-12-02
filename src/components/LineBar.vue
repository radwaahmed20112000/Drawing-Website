<template>
  <div class="shape">

    <color-bar msg="Color:" color="DE3163" key="color"></color-bar>

    <span class="tab"></span>
    <label class="label_bar">Opacity: </label>
    <span class="space"></span>

    <get-input numid="numOpacity" divid="divOpacity" rangeid="rangeOpacity"></get-input>

    <span class="tab"></span>
    <label class="label_bar">Diameter: </label>
    <span class="space"></span>

    <get-input numid="numDiameter" divid="divDiameter" rangeid="rangeDiameter"></get-input>
  </div>    
</template>

<script>
import ColorBar from './ColorBar.vue';
import GetInput from './GetInput.vue';
export default {
  components: {
       GetInput,
       ColorBar
 },
  name: 'LineBar',
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>