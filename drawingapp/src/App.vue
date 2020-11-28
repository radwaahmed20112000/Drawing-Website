<template>

  <toolsBar />
</template>

<script>
import toolsBar from './components/toolsBar.vue'

export default {
  name: 'App',
  components: {
   toolsBar
  }
 
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
