export function connect() {
    console.log('connected');
  }

export function makeGraph() {
    alert("ok");
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
