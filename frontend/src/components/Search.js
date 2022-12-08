import React, { useState } from 'react';
import List from "./List"
import ListT from "./ListT"
import Autocom from "./Autocom"
//import Prediction from "./Prediction"
import {connect, makeGraph, startGraph} from './../script/grafo.js'

export default function Search(props) {
    const [entity, setEntity] = useState('');
    const [ansEntity, setAnsEntity] = useState([]);
    
    const handleEntitySubmit = async e => {
        e.preventDefault();
        if (entity != '') {
            addWord(entity);
            setEntity('');
        }
        
    }

    const handleEntityChange = async (word) => {
        setEntity(word);
        // TODO PROBLEMA DE COOKIE AQUI
        let url = `http://localhost:8080/autocomplete?entity=${word}`
        fetch(url)
            .then(response => response.json())
            .then(data => setAnsEntity(data.search.map((x) => ({id: x.id, label: x.label, url: x.concepturi}))));
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
                            <input type="text" className="input fullwidth" placeholder="Text input" value={entity} onChange={e => handleEntityChange(e.target.value)}/>
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
            <Autocom addEntity={(newWord) => props.setValues(prevArray => [...prevArray, newWord])}></Autocom>
            
            <List entities={props.words} deleteEntity={
                (indEnt) => {
                    const newWords = props.words.filter((_, index) => index !== indEnt);
                    props.setWords(newWords);}}
            />
            
            <ListT entities={props.values} deleteEntity={
                (indEnt) => {
                    const newWords = props.values.filter((_, index) => index !== indEnt);
                    props.setValues(newWords);}}
            />
            <div>
                <button onClick={clearWords} className='button is-fullwidth is-danger'> CLEAR </button>
            </div>

        </div>
    );
}