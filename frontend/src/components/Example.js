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

        nodes.add({id:0, shape: 'image', image: 'https://upload.wikimedia.org/wikipedia/commons/2/21/Junior-Jaguar-Belize-Zoo.jpg'});
        nodes.add({id:1, label:"1"});
        nodes.add({id:2, label:"2"});
        nodes.add({id:3, label:"3"});
        
        nodes.add({id:4, label:"4", cid:1});
        nodes.add({id:5, label:"5", cid:1});
        nodes.add({id:6, label:"6", cid:1});
        nodes.add({id:7, label:"7", cid:1});
        nodes.add({id:8, label:"8", cid:1});

        edges.add({to:0, from:1})
        edges.add({to:2, from:3})


        edges.add({to:1, from:4, label: "P41"})
        edges.add({to:1, from:5, label: "P41"})
        edges.add({to:1, from:6, label: "P41"})
        edges.add({to:1, from:7, label: "P41"})
        edges.add({to:1, from:8, label: "P41"})

        edges.add({from:1, to:4, label: "P771"})
        edges.add({from:1, to:5, label: "P771"})
        edges.add({from:1, to:6, label: "P771"})
        edges.add({from:1, to:7, label: "P771"})
        edges.add({from:1, to:8, label: "P771"})

        edges.add({to:2, from:4, label: "P88"})
        edges.add({to:2, from:5, label: "P88"})
        edges.add({to:2, from:6, label: "P88"})
        edges.add({to:2, from:7, label: "P88"})
        edges.add({to:2, from:8, label: "P88"})

        edges.add({from:2, to:4, label: "P321"})
        edges.add({from:2, to:5, label: "P321"})
        edges.add({from:2, to:6, label: "P321"})
        edges.add({from:2, to:7, label: "P321"})
        edges.add({from:2, to:8, label: "P321"})

        nodes.updateOnly({id: 4});
        nodes.updateOnly({id: 5});
        nodes.updateOnly({id: 6});
        nodes.updateOnly({id: 7});
        nodes.updateOnly({id: 8});

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
        //network.setData(data);
        var clusterOptionsByData = {
          joinCondition: function (nodeOptions) {

            return [4,5,6,7,8].includes(nodeOptions.id); //!= 1 && childOptions.id != 2 && childOptions.id != 0 && childOptions.id != 3;
          },
          processProperties: function (clusterOptions, childNodes, childEdges) {
            return clusterOptions;
          },
          clusterNodeProperties: {
            id: "cidCluster",
            borderWidth: 3,
            shape: "database",
          },
        };
        network.cluster(clusterOptionsByData);
      }


      function clusterByConn() {
        var clusterOptionsByData = {
          joinCondition: function (childOptions) {
            console.log(childOptions);
            return [4,5,6,7,8].includes(childOptions.id); //!= 1 && childOptions.id != 2 && childOptions.id != 0 && childOptions.id != 3;
          },
          processProperties: function (clusterOptions, childNodes, childEdges) {
            return clusterOptions;
          },
          clusterNodeProperties: {
            id: "cidCluster",
            //borderWidth: 3,
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
              return childOptions.cid == 1; // the color is fully defined in the node.
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
            <input type="button" onClick={clusterByCid} value="Cluster by cid" />
            <input type="button" onClick={clusterByColor} value="Cluster by Color" />
            <input type="button" onClick={clusterByConn} value="Cluster by Conn" />
            
            <input type="button" onClick={addNodes} value="Add nodes" />
            <div className='has-background-white-ter' ref={container}/>
        </div>
        
    );
}