import React, { useState } from 'react';
import ListT from "./ListT"
import Autocom from "./Autocom"
const { baseURL } = require('config');
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';

import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

import  './i18n';
import { useTranslation } from 'react-i18next';

export default function Search(props) {
    const [entity, setEntity] = useState('');
    const [ansEntity, setAnsEntity] = useState([]);

    const { t, i18n } = useTranslation();

    const handleEntitySubmit = async e => {
        e.preventDefault();
        if (entity != '') {
            addWord(entity);
            setEntity('');
        }   
    }

    const handleEntityChange = async (word) => {
        setEntity(word);
        let url = `${baseURL}/autocomplete?entity=${word}`
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
        props.closeDrawer();
        let ids = props.values.map((entity) => (entity.id).replace("Q", ""));//})
        //startGraph(ids);
        props.initGraph(ids);
        props.startCrono();
        console.log(ids);
    }

    return (
        <div className='column'>

            <Autocom addEntity={(newWord) => props.setValues(prevArray => [...prevArray, newWord])}></Autocom>

            <ListT entities={props.values} deleteEntity={
                (indEnt) => {
                    const newWords = props.values.filter((_, index) => index !== indEnt);
                    props.setValues(newWords);}}
            />
            
            <Stack spacing={2} direction="row" alignItems="center">
                <Button onClick={clearWords} variant="contained" color="error" >{t('Clear')}</Button>
                <Button onClick={launchGraph} variant="contained">{t('Search')}</Button>
            </Stack>


        </div>
    );
}