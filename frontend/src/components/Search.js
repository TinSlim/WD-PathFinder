import React, { useState } from 'react';
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
        props.setValues([]);
    }

    const launchGraph = () => {
        let ids = props.values.map((entity) => (entity.id).replace("Q", ""));//})
        startGraph(ids);
        console.log(ids);
    }

    return (
        <div className='column is-3 mt-5 mr-3 has-background-secondary'>
            <div className='mb-4'>
                <button onClick={launchGraph} className='button is-fullwidth is-info'> BUSCAR </button>
            </div>
            <Autocom addEntity={(newWord) => props.setValues(prevArray => [...prevArray, newWord])}></Autocom>

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