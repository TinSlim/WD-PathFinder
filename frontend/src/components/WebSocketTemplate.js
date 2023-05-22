import React from "react";
import useWebSocket from 'react-use-websocket';

export default function WebSocketTemplate () {
    

    useWebSocket("ws://localhost:8080/query", {
        onOpen: (e) => {
          console.log('WebSocket connection establishe3d.');
        },
        onMessage: (event) => {
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
        },
        onClose: (event) => {
            if (event.wasClean) {
                console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
            } else {
                console.log('[close] Connection died');
            }
        }
    });
    
    return <h1> testafaf</h1>
}