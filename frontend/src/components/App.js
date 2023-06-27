import React, { useRef, useState, useEffect } from 'react';
import { render } from "react-dom"
import { Network } from "vis-network";
import { DataSet} from "vis-data";

import { createTheme, ThemeProvider, styled } from '@mui/material/styles';
import { theme } from './Theme';

import Navbar from "./Navbar";
import Content from "./Content";
import Help from "./Help";
import Footer from "./Footer";

import Tooltip from '@mui/material/Tooltip';
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

import Search from './Search';

import  './i18n';
import { useTranslation } from 'react-i18next';
import { Stack, Zoom } from '@mui/material';

const { socketUrl } = require('config');

  
export default function App() {
    const queryParameters = new URLSearchParams(window.location.search);
    const langParam = queryParameters.get("lang") ? queryParameters.get("lang") : 'es';
    const name = queryParameters.get("name");

    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState("00:00");

    const [showingInfo, setShowingInfo] = useState(false);
    const [drawerState, setDrawerState] = useState(false);
    const [running, setRunning] = useState(false);

    const container = useRef(null);
    
    const [lang, setLang] = useState(langParam);
    
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

    const [idsSearch, setIdsSearch] = useState([]);
    const ws = useRef(null);
    
    // Ajusta idioma
    useEffect(() => {
        i18n.changeLanguage(langParam);
        setLang(langParam);
    },[])
    
    // Set network
    useEffect(() => {
        const options = {
            autoResize: true,
            //height: (665 + document.getElementById("footer").offsetHeight) + "px",
            width:  (window.innerWidth) + "px",
            nodes: {
                shape: "image",
                image: require('./../images/no-image-photography-icon.png'),
                widthConstraint: {
                    //minimum : 200,
                    maximum : 200,
                },
                //font : {
                //    multi: 'html',
                //}
            },
            edges: {
                widthConstraint: {// 200,   // Cantidad de letras X 10
                    //minimum : 200,
                    maximum : 200,
                },
                //smooth : {
                //    type :"curvedCCW",
                //}
                
                //font : {
                //    multi: 'html',
                //},
            },
            physics : {
                maxVelocity: 8,
                minVelocity: 0.29,
                timestep: 0.4,
                barnesHut: {
                    springConstant: 0,
                    avoidOverlap: 0.6,
                    springConstant: 0.05,
                    gravitationalConstant: -20850,
                    centralGravity: 0.8, 
                }
            },
        };

        const data = { nodes: nodes.current, edges:edges.current };
        let network = new Network(container.current, data , options);
        setNetwork(network);

        const networkCont =
            container.current &&
            network;
    },[])

    function htmlTitle(html) {
        let ans = html.split('\n');
        const id = document.createElement("b");
        id.innerHTML = ans[0];
        const label = document.createElement("p");
        label.innerHTML = ans[1];

        const container = document.createElement("div");
        container.appendChild(id);
        container.appendChild(label);
        return container;
    }

    // Cuando running, crea un socket
    useEffect(() => {
        if (! running) {
            return;
        }
        if (ws.current) {
            ws.current.close();
        }

        // Limpia y reinicia el Grafo
        nodes.current.clear();
        edges.current.clear();
        network.moveTo(
            {
                position: {x:0, y:0},
                scale: 0.5,
                offset: {x:0, y:0},
                animation: false
            }
        );
        network.setOptions({
            physics: {enabled:true}
        });
        let actWS = new WebSocket(`${socketUrl}/query`);
        actWS.onopen = () => {actWS.send(idsSearch.concat(i18n.language));actWS.send(idsSearch.concat(i18n.language));actWS.send(idsSearch.concat(i18n.language));};
        actWS.onclose = () => checkClose(actWS);
        actWS.onmessage = e => {
            if (!running) return;

            const message = JSON.parse(e.data);
            if (message.type == 'vertex') {
                if (Math.log10(message.data.nodeGrade) <= gradeSize && message.data.roadSize <= roadSize) {
                    message.data.hidden = false;
                } else {
                    message.data.hidden = true;
                }
                message.data.title = message.data.title;
                //message.data.title = htmlTitle(message.data.title);
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
                //message.data.title = htmlTitle(message.data.title);
                nodes.current.updateOnly(message.data);
            }
        };
        ws.current = actWS;

        //ws.current.send(idsSearch.concat(i18n.language));
        //const wsCurrent = ws.current;

        return () => {
            //actWS.close();
            
            //setRestartSocket(!restartSocket);//wsCurrent.close();
        };
    }, [idsSearch]);

    function checkClose (actWS) {
        if (actWS == ws.current) {
            end();
        }
    }
    
    // Cambios en Slider gatillan cambio de Onmessage
    useEffect(() => {
        if (!ws.current) return;

        ws.current.onmessage = e => {
            if (!running) return;

            const message = JSON.parse(e.data);
            if (message.type == 'vertex') {
                if (Math.log10(message.data.nodeGrade) <= gradeSize && message.data.roadSize <= roadSize) {
                    message.data.hidden = false;
                } else {
                    message.data.hidden = true;
                }
                message.data.title = message.data.title; //htmlTitle();
                //message.data.title = htmlTitle(message.data.title);
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
    }, [roadSize,gradeSize]);

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
        setIdsSearch(ids);
        setRunning(true);
        network.setOptions({
            height: (window.innerHeight - document.getElementById("footer").offsetHeight) + "px"
        });
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
        //window.location.reload(false);
        //console.log("reload");
        const url = new URL(window.location.href);
        url.searchParams.set('lang', e.target.value);
        window.location.href = url.href;
        //setLang(e.target.value);
        //i18n.changeLanguage(e.target.value);
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
        <ThemeProvider theme={theme}>
        <div onLoad={openDrawer} className='hero is-fullheight has-background-white-ter'> 
            
            <div className='has-background-white-ter' ref={container}/>

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
               
                <Stack className="has-background-primary mb-3" p={0.5} direction="row" alignItems="center" justifyContent="space-between">
                    <img className="ml-1" src={require('./../images/wool2.svg')}/>
                    <IconButton onClick={closeDrawer}>
                        <ChevronLeftIcon />
                    </IconButton>
                </Stack>
                {/*
                <Typography variant="body" p={1}>
                    {t("Description")}.                   
                </Typography>
                <Divider />*/
                }

                {!showingInfo &&
                <Zoom  in={!showingInfo} >
                    <Stack
                        >
                        <Stack className="ml-3 mr-3" spacing={1} direction="row" alignItems="center">
                            <Button fullWidth={true} onClick={changeInfo} color="secondary">
                                <InfoIcon/> &nbsp;{t('Help')}
                            </Button>
                        </Stack>

                        <Search 
                            initGraph = {startSearch}
                            startCrono = {start}
                            words={words}
                            setWords={setWords}
                            values={values}
                            setValues={setValues}
                            closeDrawer={closeDrawer}>
                        </Search>
                    </Stack>
                </Zoom>
                }


                {showingInfo &&
                <Help showingInfo={showingInfo} changeInfo={changeInfo}></Help>
                }

               

            </SwipeableDrawer>
            
            <Stack
                direction="row"
                spacing={5}
                sx={{position:"absolute", left:"15%", bottom: "90px", width: "70%"}}>
                <Stack
                sx={{width: "30%"}}>
                    
                    <Stack direction="row" alignItems="center">
                        <Typography>
                            {t('Slider1')}&nbsp;
                        </Typography>
                        <Tooltip placement="top" title={t('Slider1Info')}>
                            <InfoIcon color="secondary"/>
                        </Tooltip>
                    </Stack>
                    
                    
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

                    <Stack direction="row" alignItems="center">
                        <Typography>
                            {t('Slider2')}&nbsp;
                        </Typography>
                        <Tooltip placement="top" title={t('Slider2Info')}>
                            <InfoIcon color="secondary"/>
                        </Tooltip>
                    </Stack>

                    <Slider
                    defaultValue={8}
                    min={1}
                    max={8}
                    onChange={(e) => setGradeSize(e.target.value)}
                    marks={[
                            {value:1, label:"1"},{value:2, label:"2"},{value:3, label:"3"},
                            {value:4, label:"4"},{value:5, label:"5"},{value:6, label:"6"},
                            {value:7, label:"7"},{value:8, label:"8"}]}
                    />
                </Stack>
            </Stack>

            <Footer></Footer>
        </div>
        </ThemeProvider>
    );
}


const container = document.getElementById("root");
render(<App />, container);
