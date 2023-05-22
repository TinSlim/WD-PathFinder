import React, { useEffect, useRef, useState } from "react";
import { Network } from "vis-network";
import { DataSet} from "vis-data";

const VisNetwork = () => {
	const container = useRef(null);

  
  const nodes = [
    { id: 1, label: 'Node 1' },
    { id: 2, label: 'Node 2' },
    { id: 3, label: 'Node 3' },
    { id: 4, label: 'Node 4' },
    { id: 5, label: 'Node 5' }
  ];

  var nodes1 = new DataSet(nodes);

  const edges = [
    { from: 1, to: 3 },
    { from: 1, to: 2 },
    { from: 2, to: 4 },
    { from: 2, to: 5 },
    { from: 3, to: 3 }
  ];

  var edges1 = new DataSet(edges);

  const options = {};
  
  const addNode3 = () => {
    nodes1.add({ id: 55, label: 'Node 55' });
  }

  useEffect(() => {
    const data = { nodes: nodes1, edges:edges1 };
    const network =
      container.current &&
      new Network(container.current, data , options);
  }, [container, nodes, edges]);

  return  <div>
            <div ref={container} style={{ height: '500px', width: '800px' }} />
            <button onClick={addNode3}> ADD </button>
          </div>;
};

export default VisNetwork;
