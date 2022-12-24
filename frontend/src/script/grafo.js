// create an array with nodes and edges
var nodes = new vis.DataSet([]);
var edges = new vis.DataSet([]);

// create a network
var container = null;
var data = {
    nodes: nodes,
    edges: edges,
};
var options = {

    height: '300%', //'400px',
};
var network = null;
window.onload = function() {
    container = document.getElementById("mynetwork");
    network = new vis.Network(container, data, options);
}


export function makeGraph() {
    // create an array with nodes and edges
    var nodes = new vis.DataSet([]);
    var edges = new vis.DataSet([]);

    // create a network
    var container = document.getElementById("mynetwork");
    var data = {
        nodes: nodes,
        edges: edges,
    };

    var options = {
        autoResize: true,
        height: '100%',
        width: '100%'
    };

    var network = new vis.Network(container, data, options);

    function resetGraph () {
        nodes.clear();
        edges.clear();
    }


    nodes.add({"id" : 0, "label" : "nodo0", "color" : "#cc76FC"});
    nodes.add({"id" : 1, "label" : "nodo1", "color" : "#cc76FC"});
    edges.add(
        {"from": 0,
        "label":"Relacion",
        "to":1 ,
        "font": {"align": "middle"},
        "color": {"color": '#848484'},
        "arrows" : {
            "to" : {
                "enabled" : true,
                "type" : "arrow",
            }
        }
    })
}


function resetGraph () {
    nodes.clear();
    edges.clear();
}


export function startGraph (values) {
    const socket = new WebSocket("ws://localhost:8080/query");
    console.log ("empezando");
    socket.onopen = function(e) {
      console.log("[open] Connection established");
      console.log("Sending to server");
      resetGraph();
      socket.send(values);

    };

    socket.onmessage = function(event) {
      //console.log(`[message] Data received from server: ${event.data}`);
      let newData = JSON.parse(event.data);
      for (let newVertex of newData.vertex) {
          let ans = null;
          let ent = "Q" + newVertex.label;
          let url = `http://localhost:8080/info?entity=${ent}`
          fetch(url)
              .then(response => response.json())
              .then(data => newVertex.label =  data.entities[ent].labels.en.value)
                //ans = {desc : data.entities[ent].descriptions.en.value, label : data.entities[ent].labels.en.value});
          //newVertex.label = ans.label;
          nodes.add(newVertex);
      }
      //console.log(newData.edge);
      edges.add(newData.edge);
    };

    socket.onclose = function(event) {
      if (event.wasClean) {
        console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
      } else {
        // e.g. server process killed or network down
        // event.code is usually 1006 in this case
        console.log('[close] Connection died');
      }
    };

    socket.onerror = function(error) {
        console.log(`[error]`);
    };
};
