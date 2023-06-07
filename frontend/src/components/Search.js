import React, { useState } from 'react';
import ListT from "./ListT"
import Autocom from "./Autocom"
const { baseURL } = require('config');

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
    }

    const deleteWord = (index) => {
        let copyActualWords = props.words;
        copyActualWords.pop(index);
        props.setWords(copyActualWords);
    }

    const clearWords = () => {
        props.setValues([]);
    }

    const launchGraph = () => {
        props.closeDrawer();
        let ids = props.values.map((entity) => (entity.id).replace("Q", ""));//})
        //startGraph(ids);
        props.startCrono();
        props.initGraph(ids);
    }

    return (
        <div className='column'>

            <Stack className='has-background-grey-lighter mb-3' sx={{ borderRadius: '5px' }}>
                <Autocom addEntity={(newWord) => props.setValues(prevArray => [...prevArray, newWord])}></Autocom>
                <ListT entities={props.values} deleteEntity={
                    (indEnt) => {
                        const newWords = props.values.filter((_, index) => index !== indEnt);
                        props.setValues(newWords);}}
                />
            </Stack>
            
            <Stack spacing={1} direction="row" alignItems="center">
                <Button fullWidth={true} onClick={clearWords} variant="contained" color="error" >{t('Clear')}</Button>
                <Button fullWidth={true} onClick={launchGraph} variant="contained">{t('Search')}</Button>
            </Stack>
            

        </div>
    );
}