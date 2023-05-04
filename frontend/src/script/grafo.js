const { socketUrl,  baseURL} = require('config');

// create an array with nodes and edges
var nodes = new vis.DataSet([]);
var edges = new vis.DataSet([]);

/* 
TODO example BORRAR:
var nodes = new vis.DataSet([
  {"id" : 0, "label" : "nodo0", "color" : "#cc76FC"},
  {"id" : 1, "label" : "nodo1", "color" : "#cc76FC"}
])
var edges = new vis.DataSet([
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
    }
]);

nodes.update({id: 1, label: "changed label"});
*/
// create a network

var container = null;
var data = {
    nodes: nodes,
    edges: edges,
};
var options = {
    autoResize: true,
    height: (window.innerHeight - 48) + "px",
    width: (window.innerWidth - 25) + "px",
};

var network = null;
window.onload = function() {
    container = document.getElementById("mynetwork");
    network = new vis.Network(container, data, options);
}

// TODO BORRAR
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
        width: (window.innerWidth - 25) + "px",
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

let socket = null;

export function startGraph (values) {
    if (socket != null) {
        socket.close();
    }
    socket = new WebSocket(`${socketUrl}/query`);

    socket.onopen = function(e) {
      console.log("[open] Connection established");
      console.log("Sending to server");
      resetGraph();
      socket.send(values);

    };

    socket.onmessage = function(event) {
      //console.log(`[message] Data received from server: ${event.data}`);
      let newData = JSON.parse(event.data);
      
      if (newData.type == "vertex") {
        nodes.add(newData.data);
      }

      else if (newData.type == "edge") {
        edges.add(newData.data);
        console.log(newData.data);
      }
      else if (newData.type == "edit") {
        nodes.update(newData.data);
      }
      
      //for (let newVertex of newData.vertex) {
      //    nodes.add(newVertex);
      //}
      //edges.add(newData.edge);
    };

    socket.onclose = function(event) {
      if (event.wasClean) {
        console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
      } else {
        console.log('[close] Connection died');
      }
    };

    socket.onerror = function(error) {
        console.log(`[error]`);
    };
};

export function stopGraph () {
  if (socket != null) {
    socket.close();
  }
}
