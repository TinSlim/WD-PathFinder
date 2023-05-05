import React, { useEffect, useRef, useState } from "react";
import { Network } from "vis-network";

const VisNetwork = () => {
	const container = useRef(null);

  const nodes = [
    { id: 1, label: 'Node 1' },
    { id: 2, label: 'Node 2' },
    { id: 3, label: 'Node 3' },
    { id: 4, label: 'Node 4' },
    { id: 5, label: 'Node 5' }
  ];

  const edges = [
    { from: 1, to: 3 },
    { from: 1, to: 2 },
    { from: 2, to: 4 },
    { from: 2, to: 5 },
    { from: 3, to: 3 }
  ];

  const options = {};

  useEffect(() => {
    const network =
      container.current &&
      new Network(container.current, { nodes, edges }, options);
  }, [container, nodes, edges]);

  return <div ref={container} style={{ height: '500px', width: '800px' }} />;
};

export default VisNetwork;
