import React, { useState } from 'react';
import Graph from './Graph';
import Search from './Search';

export default function Content() {
    const [actualWords, setActualWords] = useState([]);

    
    return (
        <div className='columns'> 
            <Search></Search>
            <Graph></Graph>
        </div>
    );
}