import React, { useRef, useState, useEffect } from 'react';
import { Network } from "vis-network";
import { DataSet} from "vis-data";

export default function Example(props) {
    var nodes = new DataSet([]);
    var edges = new DataSet([]);
    
    var pares = {"-321.2_41.1" : 5}
    
    var data = {
        nodes: nodes,
        edges: edges,
      };
    var network = null;
    const container = useRef(null);

    const init = () => {
        const options = {
            autoResize: true,
            height: (window.innerHeight - document.getElementById("app-bar").offsetHeight - document.getElementById("footer").offsetHeight) + "px",
            width:  (window.innerWidth) + "px",
            nodes: {
                shape: "box",
              },
        };

        nodes.add({id:0, label:"0"});
        nodes.add({id:1, label:"1"});
        nodes.add({id:2, label:"2"});
        nodes.add({id:3, label:"3"});
        
        nodes.add({id:4, label:"4"});
        nodes.add({id:5, label:"5"});
        nodes.add({id:6, label:"6"});
        nodes.add({id:7, label:"7"});
        nodes.add({id:8, label:"8"});

        edges.add({to:0, from:1})
        edges.add({to:2, from:3})


        edges.add({to:1, from:4, label: "P41"})
        edges.add({to:1, from:5, label: "P41"})
        edges.add({to:1, from:6, label: "P41"})
        edges.add({to:1, from:7, label: "P41"})
        edges.add({to:1, from:8, label: "P41"})

        edges.add({from:2, to:4, label: "P321"})
        edges.add({from:2, to:5, label: "P321"})
        edges.add({from:2, to:6, label: "P321"})
        edges.add({from:2, to:7, label: "P321"})
        edges.add({from:2, to:8, label: "P321"})

        nodes.updateOnly({id: 4, a: "41.1", b : "-321.2", size : 2});
        nodes.updateOnly({id: 5, a: "41.1", b : "-321.2", size : 2});
        nodes.updateOnly({id: 6, a: "41.1", b : "-321.2", size : 2});
        nodes.updateOnly({id: 7, a: "41.1", b : "-321.2", size : 2});
        nodes.updateOnly({id: 8, a: "41.1", b : "-321.2", size : 2});

        

        console.log(edges);
        console.log(nodes);

        data = { nodes: nodes, edges:edges };
        network =
            container.current &&
            new Network(container.current, data , options);
        
        network.on("selectNode", function (params) {
            if (params.nodes.length == 1) {
                if (network.isCluster(params.nodes[0]) == true) {
                network.openCluster(params.nodes[0]);
                }
            }
        });
    }
    
    function clusterByCid() {
        network.setData(data);
        var clusterOptionsByData = {
          joinCondition: function (childOptions) {
            console.log("==");
            console.log(childOptions);
            return !(childOptions.id in [0,1,2,3]); //!= 1 && childOptions.id != 2 && childOptions.id != 0 && childOptions.id != 3;
          },
          clusterNodeProperties: {
            id: "cidCluster",
            borderWidth: 3,
            shape: "database",
          },
        };
        network.cluster(clusterOptionsByData);
      }
    
    
      function clusterByColor() {
        network.setData(data);
        var pares2 = Object.keys(pares);
        var clusterOptionsByData;
        
        for (var i = 0; i < pares2.length; i++) {
          
          var par = pares2[i];
          
          clusterOptionsByData = {
            joinCondition: function (childOptions) {
              return childOptions.b + "_" + childOptions.a == par; // the color is fully defined in the node.
            },
            processProperties: function (clusterOptions, childNodes, childEdges) {
              var totalMass = 0;
              for (var i = 0; i < childNodes.length; i++) {
                totalMass += childNodes[i].mass;
              }
              clusterOptions.mass = totalMass;
              return clusterOptions;
            },
            clusterNodeProperties: {
              id: "cluster:" + par,
              borderWidth: 3,
              shape: "database",
              label: "color:" + par,
            },
          };
          network.cluster(clusterOptionsByData);
        }
      }


    const addNodes = () => {
        nodes.add({id:9, label:"9"});
        nodes.add({id:10, label:"10"});

        edges.add({to:1, from:9, label: "P41"})

        edges.add({from:2, to:10, label: "P321"})

        edges.add({to:1, from:10, label: "P41"})

        edges.add({from:2, to:9, label: "P321"})
    }
    
    return (
        <div>
            <button onClick={init}> LAUNCH </button>
            <input type="button" onClick={clusterByCid} value="Cluster by hubsize" />
            <input type="button" onClick={clusterByColor} value="Cluster by Color" />
            
            <input type="button" onClick={addNodes} value="Add nodes" />
            <div className='has-background-white-ter' ref={container}/>
        </div>
        
    );
}