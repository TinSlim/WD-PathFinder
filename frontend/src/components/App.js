import React, { useRef, useState, useEffect } from 'react';
import { render } from "react-dom"
import { Network } from "vis-network";
import { DataSet} from "vis-data";

import Navbar from "./Navbar"
import Content from "./Content"
import Footer from "./Footer"

import Typography from '@mui/material/Typography';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';

import Search from './Search';

const { socketUrl } = require('config');

export default function App() {
    const [drawerState, setDrawerState] = useState(false);
    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState("00:00");
    const [running, setRunning] = useState(false);
    const [stopwatchInterval, setStopWatchInterval] = useState(null);
    const [runningTime, setRunningTime] = useState(0);
    const container = useRef(null);
    const [socket,setSocket] = useState(null);
    
    
   

    const openDrawer = () => {
        setDrawerState(true);
    }

    const closeDrawer = () => {
        setDrawerState(false);
    }

    //<div style={{height:'80vh', display: 'inline-flex'}} className='ml-3 mr-3 columns'> 
    
    const calculateTime = runningTime => {
        const total_seconds = Math.floor(runningTime / 1000);
        const total_minutes = Math.floor(total_seconds / 60);
    
        const display_seconds = (total_seconds % 60).toString().padStart(2, "0");
        const display_minutes = total_minutes.toString().padStart(2, "0");
    
        return `${display_minutes}:${display_seconds}`
    }

    const start = () => {
        setRunning(true);
        clearInterval(stopwatchInterval);
        setTime("00:00");
        let startTime = Date.now() - 0;
        // animacion esfera
        const stopwatchIntervalC = setInterval ( () => {
            const runningTimeC = Date.now() - startTime;
            setRunningTime(runningTimeC);
            setTime(calculateTime(runningTimeC));
        }, 1000)
        setStopWatchInterval(stopwatchIntervalC);
    }
    
    const stop = () => {
        setRunning(false);
        clearInterval(stopwatchInterval);
        if (socket != null) {
            socket.close();
        }
    }
    

    

    var nodes = new DataSet([]);
    var edges = new DataSet([]);

    const options = {
        autoResize: true,
        height: (window.innerHeight - 48) + "px",
        width:  (window.innerWidth - 25) + "px",
        nodes: {
            shape: "box",
          },
    };

    const initGraph = (ids) => {
        if (socket != null) {
            socket.close();
        }
        const data = { nodes: nodes, edges:edges };
        const network =
            container.current &&
            new Network(container.current, data , options);
        
        const newSocket = new WebSocket(`${socketUrl}/query`);
        newSocket.onopen = function(e) {
            console.log("[open] Connection established");
            //resetGraph();
            newSocket.send(ids);
      
        };
      
        newSocket.onmessage = function(event) {
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
        };
      
        newSocket.onclose = function(event) {
        if (event.wasClean) {
            console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
        } else {
            console.log('[close] Connection died');
        }
        };
      
        newSocket.onerror = function(error) {
            console.log(`[error]`);
        };
        setSocket(newSocket);
    }


    return (
        <div onLoad={openDrawer} className='hero is-fullheight'> 
            
            <AppBar position="fixed">
                <Toolbar >
                    <Button color="inherit"
                        onClick={openDrawer}>
                        Menu</Button>
                    <Typography variant="h3" component="div" sx={{ flexGrow: 1}}>
                        W<img src={require('./../images/wool2.svg')}
                            width="50px"/>olNet
                    </Typography>

                    <Typography variant="h6" component="div" >
                        Time: {time}
                    </Typography>
                    <Button color="inherit" onClick={stop}>Stop</Button>
                    </Toolbar>
            </AppBar>

            
            {/*<Graph words={words} values={values} ></Graph>*/}
            {/*<VisNetwork></VisNetwork>*/}
            <div className='column mt-5 has-background-grey-lighter'>
                <div ref={container}/>
            </div>
            {/*<WebSocketTemplate></WebSocketTemplate>*/}
            
            <SwipeableDrawer
                PaperProps={{
                    sx: {
                      backgroundColor: "#e3e6e8"
                    }
                  }}
                open={drawerState}
                onClose={closeDrawer}>
                <div style={{display:"flex", alignItems:"center", justifyContent:"flex-end"}}>
                    <IconButton onClick={closeDrawer}>
                        <ChevronLeftIcon />
                    </IconButton>
                </div>

                <div>
                    <Search 
                        initGraph = {initGraph}
                        startCrono = {start}
                        words={words}
                        setWords={setWords}
                        values={values}
                        setValues={setValues}
                        closeDrawer={closeDrawer}>
                    </Search>
                </div>
                 
            </SwipeableDrawer>

            <Footer></Footer>
        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);
