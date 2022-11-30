import React, { useState } from 'react';
import List from "./List"

export default function Search(props) {
    const [entity, setEntity] = useState('');
    const [words, setWords] = useState([]);

    const handleEntitySubmit = async e => {
        e.preventDefault();
        if (entity != '') {
            addWord(entity);
            setEntity('');
        }
        
    }

    const addWord = (newWord) => {
        setWords(prevArray => [...prevArray, newWord]);
        console.log(words);
    }

    const deleteWord = (index) => {
        let copyActualWords = words;
        copyActualWords.pop(index);
        setWords(copyActualWords);
        console.log(words);
    }

    return (
        <div className='column is-one-quarter has-background-warning'>
            <form onSubmit={handleEntitySubmit}>
                <label className="label">Name</label>
                <div className="field is-grouped">
                    <div className="control">
                        <input className="input" type="text" placeholder="Text input" value={entity} onChange={e => setEntity(e.target.value)}/>
                    </div>
                    <div className="control">
                        <button className="button control"> AÑADIR </button>
                    </div>
                </div>
            </form>
            <List entities={words} deleteEntity={
                (indEnt) => {
                    const newWords = words.filter((_, index) => index !== indEnt);
                    setWords(newWords);}}
            />
        </div>
    );
}