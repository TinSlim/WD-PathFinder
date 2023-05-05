import React, { useState, useEffect } from 'react';
import { render } from "react-dom"
//import useWebSocket from 'react-use-websocket';

import {stopGraph} from './../script/grafo.js'

import Navbar from "./Navbar"
import Content from "./Content"
import Footer from "./Footer"
import VisNetwork from './VisNetwork';
import WebSocketTemplate from './WebSocketTemplate';

import Typography from '@mui/material/Typography';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Drawer from '@mui/material/Drawer';
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';

import Graph from './Graph';
import Search from './Search';

export default function App() {
    const [drawerState, setDrawerState] = useState(false);
    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState("00:00");
    const [running, setRunning] = useState(false);
    const [stopwatchInterval, setStopWatchInterval] = useState(null);
    const [runningTime, setRunningTime] = useState(0);
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
    }
    
    const initGraph = (ids) => {
        
        console.log(ids);
    }

    return (
        <div> 
            
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
                    <Button color="inherit" onClick={() => {stopGraph();stop();}}>Stop</Button>
                    </Toolbar>
            </AppBar>

            
            <Graph words={words} values={values} ></Graph>
            
             {/* TODO <VisNetwork></VisNetwork>*/ }
             <WebSocketTemplate></WebSocketTemplate>
            
            <SwipeableDrawer
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

        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);
