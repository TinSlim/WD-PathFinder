import React, { useState } from 'react';
import { render } from "react-dom"
import Navbar from "./Navbar"
import Content from "./Content"
import Footer from "./Footer"
import Typography from '@mui/material/Typography';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Drawer from '@mui/material/Drawer';
import Button from '@mui/material/Button';

import Graph from './Graph';
import Search from './Search';

export default function App() {
    const [drawerState, setDrawerState] = useState(false);
    const [words, setWords] = useState([]);
    const [values, setValues] = useState([]);
    const [time, setTime] = useState(0);

    const openDrawer = () => {
        setDrawerState(true);
    }

    const closeDrawer = () => {
        setDrawerState(false);
    }

    //<div style={{height:'80vh', display: 'inline-flex'}} className='ml-3 mr-3 columns'> 
    
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
                        Time: 00:00
                    </Typography>
                    <Button color="inherit">Stop</Button>
                    </Toolbar>
            </AppBar>

            <Graph words={words} values={values} ></Graph>
            
            <Drawer
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
                 
            </Drawer>

        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);
