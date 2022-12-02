import React, { useState } from 'react';
import Graph from './Graph';
import Search from './Search';

export default function Content() {
    const [words, setWords] = useState([]);

    
    return (
        <div style={{height:'80vh', display: 'inline-flex'}} className='ml-3 mr-3 columns'> 
            <Search words={words} setWords={setWords}></Search>
            <Graph words={words}></Graph>
        </div>
    );
}