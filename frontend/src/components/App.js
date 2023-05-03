import React, { useState, useEffect } from 'react';
import { render } from "react-dom"
//import useWebSocket from 'react-use-websocket';

import {pauseGraph} from './../script/grafo.js'

import Navbar from "./Navbar"
import Content from "./Content"
import Footer from "./Footer"
import Typography from '@mui/material/Typography';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Drawer from '@mui/material/Drawer';
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';

import Graph from './Graph';
import Search from './Search';

export default function App() {
    const [drawerState, setDrawerState] = useState(false);
    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState("00:00");
    const [running, setRunning] = useState(false);
    const [stopwatchInterval, setStopWatchInterval] = useState(null);
    /*
    const [websocket, setWebSocket] = useState(0);
    React.useEffect(() => {
        const websocket = new WebSocket('ws://localhost:8080/query');
    
        websocket.onopen = () => {
          console.log('connected TO REACT WS');
        }
    
        websocket.onmessage = (event) => {
          const data = JSON.parse(event.data);
        }

        setWebSocket(websocket);

        return () => {
            websocket.close()
        }
        }, [])
    */

    const openDrawer = () => {
        //websocket.send("261, 298");
        //websocket.send("1, 2");
        setDrawerState(true);
    }

    const closeDrawer = () => {
        setDrawerState(false);
        console.log("CloseDrawer");
    }

    //<div style={{height:'80vh', display: 'inline-flex'}} className='ml-3 mr-3 columns'> 
    
    let runningTime = 0;
    
    const playPause = () => {
        if (!running) {
            setRunning(true);
            start();
        } else {
            setRunning(false);
            pause();
        }
    }

    const calculateTime = runningTime => {
        const total_seconds = Math.floor(runningTime / 1000);
        const total_minutes = Math.floor(total_seconds / 60);
    
        const display_seconds = (total_seconds % 60).toString().padStart(2, "0");
        const display_minutes = total_minutes.toString().padStart(2, "0");
    
        return `${display_minutes}:${display_seconds}`
    }

    const start = () => {
        console.log("start");
        let startTime = Date.now() - runningTime;
        // animacion esfera
        stopwatchInterval = setInterval ( () => {
            runningTime = Date.now() - startTime;
            setTime(calculateTime(runningTime));
        }, 1000)
        setStopWatchInterval(stopwatchInterval);
    }
    
    const pause = () => {
        clearInterval(stopwatchInterval);
    }

    const stop = () => {
        setRunning(false);
        runningTime = 0;
        clearInterval(stopwatchInterval);
        setTime("00:00");
    }

    return (
        <div>           
            <AppBar position="fixed">
                <Toolbar>
                    <Button color="inherit"
                    onClick={openDrawer}>
                        Menu</Button>

                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Cosas
                    </Typography>

                    <Typography variant="h6" component="div" >
                        Time: <div id="stopwatch" class="stopwatch">{time}</div>
                    </Typography>
                    <Button color="inherit" onClick={playPause}>PlayPause</Button>
                    <Button color="inherit" onClick={stop}>Stoptops</Button>
                    <Button color="inherit" onClick={pauseGraph}>Stop</Button>
                    </Toolbar>
            </AppBar>

            <Graph words={words} values={values} ></Graph>
            
            <SwipeableDrawer
                open={drawerState}
                onClose={closeDrawer}>
                <div>
                    <Search 
                        words={words}
                        setWords={setWords}
                        values={values}
                        setValues={setValues}
                        closeDrawer={closeDrawer}>
                    </Search>
                </div>
                 
            </SwipeableDrawer>

        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);
