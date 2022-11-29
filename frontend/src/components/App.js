import React from 'react';
import { render } from "react-dom"

export default function App() {
    return (
        <div>
            <h1> Resultado OK </h1>
        </div>
    );
}


const container = document.getElementById("root");
render(<App />, container);