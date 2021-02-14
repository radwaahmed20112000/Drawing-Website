<template>
    <div>
        <div id="modalJSON">
            <form id="form1">
                <h2>Download as JSON file</h2> <br>
                <label for="label" >Enter the file path : </label>
                <input type="text" class="text" id= "textJSON" value="x:/location/name.json"><br><br>
                <button @click="downloadJSON" class="download" id="downloadJSON">Download</button>
                <p id="validJSON">You should enter a valid file name and location.</p>
                <button class = "cancel" id="cancelJSON">Cancel</button>
            </form>
        </div>
         <div id="modalXML">
            <form id="form2">
                <h2>Download as XML file</h2> <br>
                <label for="label" >Enter the file path : </label>
                <input type="text" class="text" id= "textXML" value="x:/location/name.xml"><br><br>
                <button @click="downloadXML" class="download" id="downloadXML">Download</button>
                <p id="validXML">You should enter a valid file name and location.</p>
                <button class = "cancel" id="cancelXML">Cancel</button>
            </form>
        </div>
        <div id="modalOpen">
            <form id="form3">
            <h2>Upload JSON or XML File</h2> <br>
            <label for="label" >Choose file : </label>
            <input type="file" accept=".json, .xml" id="inputFile">
            <button class = "cancel" id="cancelOpen">Cancel</button>
            </form>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
const apiUrl = "http://localhost:8089"
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
let modalJSON = null;
let modalXML = null;
export default {
  name: 'Modal',
  mounted() {
    var saveJSON = document.getElementById("saveJSON");
    modalJSON = document.getElementById("modalJSON");
    var cancelJSON = document.getElementById("cancelJSON");
    var saveXML = document.getElementById("saveXML");
    modalXML = document.getElementById("modalXML");
    var cancelXML = document.getElementById("cancelXML");
    var open = document.getElementById("open");
    var modalOpen = document.getElementById("modalOpen");
    var cancelOpen = document.getElementById("cancelOpen");
    saveJSON.addEventListener("click", ()=> {
        modalJSON.style.display = "block";
    });
    cancelJSON.addEventListener("click", ()=> {
        modalJSON.style.display = "none";
    });
    saveXML.addEventListener("click", ()=> {
        modalXML.style.display = "block";
    });
    cancelXML.addEventListener("click", ()=> {
        modalXML.style.display = "none";
    });
    open.addEventListener("click", ()=> {
        modalOpen.style.display = "block";
    });
    cancelOpen.addEventListener("click", ()=> {
        modalOpen.style.display = "none";
    });
  },
  methods : {
    async downloadJSON()
    {
        var regex = /^([A-Za-z]:)(\/[A-Za-z_\\-\\s0-9\\.]+)+\.json$/
        var input = document.getElementById("textJSON");
        var path  = input.value;
        var warning = document.getElementById("validJSON");
        if(!regex.test(path))
        {
            warning.style.display = "block";
            return;
        }
        
        await axios.post(apiUrl + "/downloadJSON", path, {headers: {"Content-Type": "text/plain"}})
        .then(function (response) {
            console.log(response.data);
        })
        .catch(function (error) {
            console.log(error);
        });
    },
    downloadXML()
    {
        
    }
}
}
</script>

<style scoped>
#modalJSON , #modalXML{
    position:absolute;
    width: 500px;
    height: 250px;
    z-index: 9999999;
    background-color: #cccc;
    padding: 20px;
    padding-top: 5px;
    transform: translate(-50%, -50%);
    top : 50%;
    left: 50%;
    font-family: 'Open sans' , sans-serif;
    display:none ;
}
#modalOpen {
    position:absolute;
    width: 500px;
    height: 150px;
    z-index: 9999999000;
    background-color: #cccc;
    padding: 20px;
    padding-top: 5px;
    transform: translate(-50%, -50%);
    top : 50%;
    left: 50%;
    font-family: 'Open sans' , sans-serif;
    display:none ;
}
.download {
    width: 100px;
    height: 30px;
    margin-left: 180px;
    margin-bottom: 0;
    border : none;
    background-color:  #DE3163 ;
    color: white;
    font-weight: 600;
    cursor: pointer;
    outline: none;
}
.cancel {
    margin-left: 400px;
    width : 70px;
    height : 30px;
    background-color:  #DE3163 ;
    color: white;
    font-weight: 600;
    cursor: pointer;
    outline: none;
    border: none;
}
#validJSON, #validXML {
    font-size: 15px;
    margin-left: 60px;
    display: none;
}
.text {
    width: 300px;
    height: 25px;
    color: rgb(90, 86, 86);
    font-weight: 600;
    font-size: 15px;
    outline: none;
}
</style>