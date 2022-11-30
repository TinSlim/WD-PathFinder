import React from 'react';
import { render } from "react-dom"
import Navbar from "./Navbar"
import Content from "./Content"
import Footer from "./Footer"

export default function App() {
    return (
        <div>
            <Navbar></Navbar>
            <Content></Content>
            <Footer></Footer>
        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);