import React, { useRef, useState, useEffect } from 'react';
import { render } from "react-dom"
import { Network } from "vis-network";
import { DataSet} from "vis-data";

import Navbar from "./Navbar"
import Content from "./Content"
import Example from "./Example"
import Footer from "./Footer"

import Typography from '@mui/material/Typography';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Select from '@mui/material/Select';
import MenuItem from "@mui/material/MenuItem";
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';

import Search from './Search';

import  './i18n';
import { useTranslation } from 'react-i18next';

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
    const [lang, setLang] = useState('en');

    const [network, setNetwork] = useState(null);

    const { t, i18n } = useTranslation();
   

    const [pares, setPares] = useState({});
    const [nodoPar, setNodoPar] = useState({});


    const [nodes, setNodes] = useState(new DataSet([]));
    const [edges, setEdges] = useState(new DataSet([]));

    const openDrawer = () => {
        setDrawerState(true);
    }

    const closeDrawer = () => {
        setDrawerState(false);
    }

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
            console.log("socket close");
            socket.close();
            console.log("socket clos3de");
            setSocket(socket);
        }
    }
    
    const changeLanguage = (e) => {
        setLang(e.target.value);
        i18n.changeLanguage(e.target.value);
    }

    const groupClusters = () => {
        // Decluster
        for (let index of network.body.nodeIndices) {
            if (network.isCluster(index) == true) {
                network.openCluster(index);
            }
        }
        
        console.log("pares");
        console.log(pares);
        let clusterId = 0;
        for (let parKey in pares) {

            if (pares[parKey].length >= 5) {
                let clusterOptionsByData = {
                    joinCondition: function (childOptions) {
                        return pares[parKey].includes(childOptions.id);
                    },
                    processProperties: function (clusterOptions, childNodes, childEdges) {
                        let text = childNodes.map(x => x.label).join(",")
                        clusterOptions.title = text;
                        clusterOptions.label = text.substring(0, 23);
                        return clusterOptions;
                    },
                    clusterNodeProperties: {
                      id: `${clusterId}cluster:`,
                      //borderWidth: 3,
                      //shape: "database",
                      //label: childNodes.map(x => x.label).join(","),
                    },
                  };
                network.cluster(clusterOptionsByData);
                setNetwork(network);
                clusterId += 1;
            }
        }
    }

    const initGraph = (ids) => {
        let pares = {};
        setPares(pares);

        let nodoPar = {};
        setNodoPar(nodoPar);

        let nodes = new DataSet([]);
        setNodes(nodes);

        let edges = new DataSet([]);
        setEdges(edges);

        if (socket != null) {
            socket.close();
        }
        const options = {
            autoResize: true,
            height: (window.innerHeight - document.getElementById("app-bar").offsetHeight - document.getElementById("footer").offsetHeight) + "px",
            width:  (window.innerWidth) + "px",
            nodes: {
                shape: "box",
              },
            //repulsion: {
            //    centralGravity: 0.2,
            //    springLength: 200,
            //    springConstant: 0.05,
            //    nodeDistance: 500,
            //    damping: 0.09
            //  },
        };

        const data = { nodes: nodes, edges:edges };
        
        let network = new Network(container.current, data , options);
        setNetwork(network);
        
        const networkCont =
            container.current &&
            network;

        const newSocket = new WebSocket(`${socketUrl}/query`);
        newSocket.onopen = function(e) {
            console.log("[open] Connection established");
            //resetGraph();
            newSocket.send(ids.concat(i18n.language));
      
        };
      
        newSocket.onmessage = function(event) {
            
        let newData = JSON.parse(event.data);
        
        if (newData.type == "vertex") {
            nodes.add(newData.data);
            setNodes(nodes);
        }
    
        else if (newData.type == "edge") {
            edges.add(newData.data);
            setEdges(edges);

            let item1 = nodes.get(newData.data.from, { fields: ['id'] } );
            let actEdge = newData.data.labelWiki + "_" + newData.data.to;
            if (! (item1.id in nodoPar) ) {
                if (actEdge in pares) {
                    pares[actEdge].push(item1.id);
                }
                else {
                    pares[actEdge] = [item1.id];
                }
                nodoPar[item1.id] = [actEdge];
            }
            else {
                pares[nodoPar[item1.id]] = pares[nodoPar[item1.id]].filter((ele) => {return ele != item1.id})
                
                nodoPar[item1.id].push(actEdge);
                nodoPar[item1.id].sort();

                if (nodoPar[item1.id] in pares) {
                    pares[nodoPar[item1.id]].push(item1.id);
                }
                else {
                    pares[nodoPar[item1.id]] = [item1.id];
                }
            }


            let item2 = nodes.get(newData.data.to, { fields: ['id'] } );
            actEdge = "-" + newData.data.labelWiki + "_" + newData.data.from;
            if (! (item2.id in nodoPar) ) {
                if (actEdge in pares) {
                    pares[actEdge].push(item2.id);
                }
                else {
                    pares[actEdge] = [item2.id];
                }
                nodoPar[item2.id] = [actEdge];
            }
            else {
                pares[nodoPar[item2.id]] = pares[nodoPar[item2.id]].filter((ele) => {return ele != item2.id})
                
                nodoPar[item2.id].push(actEdge);
                nodoPar[item2.id].sort();

                if (nodoPar[item2.id] in pares) {
                    pares[nodoPar[item2.id]].push(item2.id);
                }
                else {
                    pares[nodoPar[item2.id]] = [item2.id];
                }
            }
           
            setPares({... pares});
            setNodoPar({... nodoPar});
        }
        

        else if (newData.type == "edit") {
            nodes.updateOnly(newData.data);
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
        <div onLoad={openDrawer} className='hero is-fullheight has-background-white-ter'> 
            
            <AppBar id="app-bar" position="static">
                <Toolbar >
                    <Button color="inherit"
                        onClick={openDrawer}>
                        {t('Menu')}
                    </Button>

                    <Typography variant="h3" component="div" sx={{ flexGrow: 1}}>
                        W<img src={require('./../images/wool2.svg')}
                            width="50px"/>olNet
                    </Typography>

                    <Select
                    variant="standard"
                    className="selectBox"
                    onChange={changeLanguage}
                    name="lang"
                    value={lang}
                    >
                         <MenuItem className="optionsMenu" value="en">
                            {t('English')}
                        </MenuItem>
                        <MenuItem className="optionsMenu" value="es">
                            {t('Spanish')}
                        </MenuItem>
                    </Select>
                    
                    <Typography variant="h6" component="div" >
                    {t('Time')}: {time}
                    </Typography>
                    <Button color="inherit" onClick={stop}>{t('Stop')}</Button>
                    </Toolbar>
            </AppBar>

            
            {/*<Graph words={words} values={values} ></Graph>*/}
            {/*<VisNetwork></VisNetwork>*/}
           
            <div className='has-background-white-ter' ref={container}/>

            <Example></Example>
            <Button onClick={groupClusters}> PRINT INFO </Button>

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
