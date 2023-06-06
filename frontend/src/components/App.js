import React, { useRef, useState, useEffect } from 'react';
import { render } from "react-dom"
import { Network } from "vis-network";
import { DataSet} from "vis-data";

import Navbar from "./Navbar"
import Content from "./Content"
import Example from "./Example"
import Footer from "./Footer"

import Slide from '@mui/material/Slide';

import Typography from '@mui/material/Typography';

import Slider from '@mui/material/Slider';
import Divider from '@mui/material/Divider';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import SquareIcon from '@mui/icons-material/Square';
import Select from '@mui/material/Select';
import MenuItem from "@mui/material/MenuItem";
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';

import TimerIcon from '@mui/icons-material/Timer';
import CompressIcon from '@mui/icons-material/Compress';

import IconButton from '@mui/material/IconButton';
import InfoIcon from '@mui/icons-material/Info';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';

import Fab from '@mui/material/Fab';

import Search from './Search';

import  './i18n';
import { useTranslation } from 'react-i18next';
import { Stack, Zoom } from '@mui/material';

const { socketUrl } = require('config');



  
export default function App() {
    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState("00:00");

    const [showingInfo, setShowingInfo] = useState(false);
    const [drawerState, setDrawerState] = useState(false);
    const [running, setRunning] = useState(false);
    
    
    const container = useRef(null);
    
    const [lang, setLang] = useState('es');
    const { t, i18n } = useTranslation();
   
    const [network, setNetwork] = useState(null);
    const [pares, setPares] = useState({});
    const [nodoPar, setNodoPar] = useState({});
    
    const nodes = useRef(new DataSet([]));
    const edges = useRef(new DataSet([]));

    const stopwatchInterval = useRef(null);

    const [restartSocket, setRestartSocket] = useState(true);
    const [roadSize, setRoadSize] = useState(3);
    const [gradeSize, setGradeSize] = useState(9);

    const ws = useRef(null);
    

    // Cuando inicia crea un socket
    useEffect(() => {
        ws.current = new WebSocket(`${socketUrl}/query`);
        ws.current.onopen = () => console.log("ws opened");
        ws.current.onclose = () => console.log("onclose::");

        const wsCurrent = ws.current;

        return () => {
            console.log("cierra ws");
            
            //setRestartSocket(!restartSocket);//wsCurrent.close();
        };
    }, [restartSocket]);


    useEffect(() => {
        if (!ws.current) return;

        ws.current.onmessage = e => {
            if (!running) return;

            const message = JSON.parse(e.data);
            if (message.type == 'vertex') {
                console.log(message);
                if (Math.log10(message.data.nodeGrade) <= gradeSize && message.data.roadSize <= roadSize) {
                    message.data.hidden = false;
                } else {
                    message.data.hidden = true;
                }
                nodes.current.add(message.data);
            }
            else if (message.type == 'edge') {
                edges.current.add(message.data);
            }
            else if (message.type == "edit") {
                if ('nodeGrade' in message.data) {
                    if (Math.log10(message.data.nodeGrade) <= gradeSize && message.data.roadSize <= roadSize) {
                        message.data.hidden = false;
                    } else {
                        message.data.hidden = true;
                    }
                }
                nodes.current.updateOnly(message.data);
            }
        };
    }, [running, roadSize,gradeSize]);

    const changeInfo = () => {
        if (showingInfo) {setShowingInfo(false)}
        else {setShowingInfo(true)};
    }

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

    const startSearch = (ids) => {
        if (running) {      //
            end2();         // TODO no reinicia al apretar buscar en medio de una búsqueda
        }                   //

        let pares = {};
        setPares(pares);

        let nodoPar = {};
        setNodoPar(nodoPar);

        nodes.current = new DataSet([]);
        edges.current = new DataSet([]);

        const options = {
            autoResize: true,
            height: (window.innerHeight - document.getElementById("footer").offsetHeight) + "px",
            width:  (window.innerWidth) + "px",
            nodes: {
                shape: "image",
                image: require('./../images/no-image-photography-icon.png'),
              },
            physics : {
                forceAtlas2Based: {
                    theta: 0.45,
                    gravitationalConstant: -310,
                    centralGravity: 0,
                    springLength: 500,
                    springConstant: 0.675,
                    damping: 0.1,
                    avoidOverlap: 1
                  },
                //barnesHut: {
                //  gravitationalConstant: -50,   // TODO numero chistoso = 10000
                //  centralGravity: 0,
                //  avoidOverlap: 0.5
                //},
              //minVelocity: 1
            },
        };

        const data = { nodes: nodes.current, edges:edges.current };
        let network = new Network(container.current, data , options);
        setNetwork(network);
        
        const networkCont =
            container.current &&
            network;

        ws.current.send(ids.concat(i18n.language));

    }

    const end2 = () => {
        ws.current.close();
        end();
    }
    const end = () => {
        clearInterval(stopwatchInterval.current);
        //end();//
        network.setOptions({
            physics: {enabled:false}
        });
        //setNetwork(network);
        setRunning(false);
        setRestartSocket(!restartSocket);
    }

    const start = () => {
        setRunning(true);
        clearInterval(stopwatchInterval.current);
        setTime("00:00");
        let startTime = Date.now() - 0;
        // animacion esfera
        const stopwatchIntervalC = setInterval ( () => {
            const runningTimeC = Date.now() - startTime;
            setTime(calculateTime(runningTimeC));
        }, 1000)
        stopwatchInterval.current = stopwatchIntervalC;

        
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
                    },
                  };
                network.cluster(clusterOptionsByData);
                setNetwork(network);
                clusterId += 1;
            }
        }
    }
    
/*
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
            height: (window.innerHeight - document.getElementById("footer").offsetHeight) + "px",
            width:  (window.innerWidth) + "px",
            nodes: {
                shape: "image",
                image: require('./../images/no-image-photography-icon.png'),
              },
            physics : {
                forceAtlas2Based: {
                    theta: 0.45,
                    gravitationalConstant: -310,
                    centralGravity: 0,
                    springLength: 500,
                    springConstant: 0.675,
                    damping: 0.1,
                    avoidOverlap: 1
                  },
                //barnesHut: {
                //  gravitationalConstant: -50,   // TODO numero chistoso = 10000
                //  centralGravity: 0,
                //  avoidOverlap: 0.5
                //},
              //minVelocity: 1
            },
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
            newSocket.send(ids.concat(i18n.language));
        };
      
        newSocket.onmessage = function(event) {
            
            let newData = JSON.parse(event.data);
            
            if (newData.type == "vertex") {
                newData.data.hidden = checkVals(newData.data.roadSize, newData.data.nodeGrade);
                nodes.add(newData.data);
                setNodes(nodes);
            }
        
            else if (newData.type == "edge") {
                edges.add(newData.data);
                setEdges(edges);
                */
/*
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
                */ 
/*
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
    }*/

    const handleSliderGrade = (e) => {
        setGradeSize(e.target.value)
    }

    const handleSliderRoad = (e) => {
        setRoadSize(e.target.value)
    }

    const handleSliderChange = () => {
        let updates = nodes.current.map(
            (x) => {
                let hidden = Math.log10(x.nodeGrade) <= gradeSize && x.roadSize <= roadSize ? false : true;
                return {id:x.id,hidden:hidden} 
        });
        nodes.current.updateOnly(updates);
    }
    
    useEffect( () => {
        handleSliderChange();
    }, [roadSize]);

    useEffect( () => {
        handleSliderChange();
    }, [gradeSize]);

    return (
        <div onLoad={openDrawer} className='hero is-fullheight has-background-white-ter'> 
            
            <div className='has-background-white-ter' ref={container}/>

            {/*<Example></Example>*/}

            <Stack
                sx={{position:"fixed",top: "0px",left: "0px", width: "100%"}}
                id="app-bar"
                direction="row"
                justifyContent="space-between" 
                className='mt-1 pr-5 pl-5'>
                    
                    <Stack direction="row">
                        <IconButton
                            size="small"
                            edge="start"
                            color="primary"
                            aria-label="menu"
                            sx={{ mr: 2 }}
                            onClick={openDrawer}
                        >
                            <MenuIcon />
                        </IconButton>

                        
                    </Stack>

                    <Stack 
                    direction="row" 
                    spacing={1} 
                    alignItems="center">
                        {/*
                        <Stack>
                            <Button variant="contained" onClick={groupClusters} >
                            &nbsp; 
                            <CompressIcon/> COMPRIMIR </Button> 
                        </Stack>
                        */}
                        <Stack direction="row">
                            <Button variant="contained" disabled={!running} onClick={end2}>
                                <TimerIcon />
                                &nbsp;
                                {time}
                                &nbsp;
                            
                                {t('Stop')}
                            </Button>
                        </Stack>
                    </Stack>
                        
                    
                    <Select
                        variant="standard"
                        className="selectBox"
                        onChange={changeLanguage}
                        name="lang"
                        value={lang}
                        >
                        
                        <MenuItem className="optionsMenu" value="es">
                            {t('Spanish')}
                        </MenuItem>
                        <MenuItem className="optionsMenu" value="en">
                            {t('English')}
                        </MenuItem>
                    </Select>
            </Stack>



            <SwipeableDrawer
                PaperProps={{
                    sx: {
                      backgroundColor: "#e3e6e8",
                      width: "324px"        // TODO ancho del drawer 324
                    }
                  }}
                open={drawerState}
                onClose={closeDrawer}>
                <Stack spacing={12} className="has-background-primary" direction="row" justifyContent="flex-end">
                    <Typography variant="h4" fullWidth={true} className='has-text-white' p={1}>
                        W<img src={require('./../images/wool2.svg')} width="25px"/>olNet
                    </Typography>
                    <IconButton onClick={closeDrawer}>
                        <ChevronLeftIcon />
                    </IconButton>
                </Stack>

                    <Typography variant="body" p={1}>
                        Woolnet, es el graficador de caminos entre entidades de Wikidata.
                    </Typography>
                <Divider />
                


                {!showingInfo &&
                <Zoom  in={!showingInfo} >
                    <Stack
                        >

                        <Search 
                            initGraph = {startSearch}
                            startCrono = {start}
                            words={words}
                            setWords={setWords}
                            values={values}
                            setValues={setValues}
                            closeDrawer={closeDrawer}>
                        </Search>

                    

                        <Stack className="ml-3 mr-3" spacing={1} direction="row" alignItems="center">
                            <Button fullWidth={true} onClick={changeInfo} >
                                <InfoIcon/> &nbsp;Ayuda
                            </Button>
                        </Stack>
                    </Stack>
                </Zoom>
                }


                {showingInfo &&
                <Zoom hidden={!showingInfo} in={showingInfo} >
                    <Stack>
                        <Stack>
                            <Button fullWidth={true} className='mt-3' onClick={changeInfo}>
                                <InfoIcon/> Volver
                            </Button>
                        </Stack>
                    
                        <Stack className='ml-3 mr-3'>
                            <Stack style={{display: 'flex',height: '500px', overflowY: 'auto'}}>
                                <Stack>
                                    <Typography variant='h6'>
                                        Uso:
                                    </Typography>
                                    
                                    <Typography variant="body2" component="div">
                                        1. Escriba en el buscador la entidad la entidad que desea buscar.
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        2. Cuando se desplieguen entidades existentes, seleccione una.
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        3. Repita el paso 1. y 2. con las entidades que desea.
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        4. Clickeando el botón BUSCAR iniciará las búsqueda de caminos.
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        • Puede detener la búsqueda clickeando en DETENER.
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        • Puede disminuir regular los resultados usando los SLIDERS inferiores.
                                    </Typography>
                                </Stack>

                                <Stack>
                                    <Typography variant='h6'>
                                        WoolNet:
                                    </Typography>
                                    <Typography variant="body2" component="div">
                                        WoolNet es la aplicación que permite obtener los caminos que unen entidades de Wikdata.
                                    </Typography>
                                </Stack>

                            
                            {/*
                            <Typography variant="body2" component="div">
                                • Si los resultados son demasiados, puede comprimirlos usando el botón COMPRIMIR.
                            </Typography>
                            */}
                            </Stack>
                        </Stack>
                        
                    </Stack>

                </Zoom>
                }

               

            </SwipeableDrawer>
            
            <Stack
                direction="row"
                spacing={5}
                sx={{position:"absolute", left:"15%", bottom: "90px", width: "70%"}}>
                <Stack
                sx={{width: "30%"}}>
                    <Typography>
                        Largo caminos
                    </Typography>
                    <Slider
                    defaultValue={3}
                    min={1}
                    max={3}
                    onChange={(e) => setRoadSize(e.target.value)}
                    marks={[{value:1, label:"1"},{value:2, label:"2"},{value:3, label:"3"}]}
                    />
                </Stack>
                <Stack
                sx={{width: "70%"}}>
                    <Typography>
                        Capa
                    </Typography>
                    <Slider
                    defaultValue={9}
                    min={1}
                    max={9}
                    onChange={(e) => setGradeSize(e.target.value)}
                    marks={[
                            {value:1, label:"1"},{value:2, label:"2"},{value:3, label:"3"},
                            {value:4, label:"4"},{value:5, label:"5"},{value:6, label:"6"},
                            {value:7, label:"7"},{value:8, label:"8"},{value:9, label:"9"}]}
                    />
                </Stack>
            </Stack>

            <Footer></Footer>
        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);
