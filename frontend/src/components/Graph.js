import React, { useState } from 'react';
import {connect, makeGraph} from './grafo.js'

export default function Graph() {
    return (
        <div className='column has-background-primary'>
            <button onClick={makeGraph}>makeGraph</button>
            <h1> TEST </h1>
            <div id="mynetwork"></div>
        </div>
    );
}