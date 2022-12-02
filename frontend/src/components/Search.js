import React, { useState } from 'react';
import List from "./List"
import {connect, makeGraph, startGraph} from './../script/grafo.js'

export default function Search(props) {
    const [entity, setEntity] = useState('');

    const handleEntitySubmit = async e => {
        e.preventDefault();
        if (entity != '') {
            addWord(entity);
            setEntity('');
        }
        
    }

    const addWord = (newWord) => {
        props.setWords(prevArray => [...prevArray, newWord]);
        console.log(props.words);
    }

    const deleteWord = (index) => {
        let copyActualWords = props.words;
        copyActualWords.pop(index);
        props.setWords(copyActualWords);
        console.log(props.words);
    }

    const clearWords = () => {
        props.setWords([]);
    }

    const launchGraph = () => {
        startGraph(props.words);
        console.log(props.words);
    }

    return (
        <div className='column is-3 mt-5 mr-3 has-background-secondary'>
            <div className='mb-4'>
                <button onClick={launchGraph} className='button is-fullwidth is-info'> BUSCAR </button>
            </div>
            <div className='mb-3'>
                <form onSubmit={handleEntitySubmit}>
                    <div className="field is-grouped">
                        <div className="control has-icons-left has-icons-right is-expanded">
                            <input type="text" className="input fullwidth" placeholder="Text input" value={entity} onChange={e => setEntity(e.target.value)}/>
                            <span className="icon is-medium is-left">
                            <i className="fa fa-futbol-o"></i>
                            </span>
                        </div>
                        <p className="control">
                            <button className="button is-info"><i className="fa fa-futbol-o"></i></button>
                        </p>
                    </div>
                </form>
            </div>
            <List entities={props.words} deleteEntity={
                (indEnt) => {
                    const newWords = props.words.filter((_, index) => index !== indEnt);
                    props.setWords(newWords);}}
            />
            <div>
                <button onClick={clearWords} className='button is-fullwidth is-danger'> CLEAR </button>
            </div>
        </div>
    );
}