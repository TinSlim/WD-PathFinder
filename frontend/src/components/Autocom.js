import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import InputAdornment from '@mui/material/InputAdornment';
import CircularProgress from '@mui/material/CircularProgress';

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';

import  './i18n';
import { useTranslation } from 'react-i18next';

const { baseURL } = require('config');

export default function Autocom(props) {
  const [options, setOptions] = React.useState([]);
  const [value, setValue] = React.useState("");
  const [inputValue, setInputValue] = React.useState('');
  const [isLoading, setIsLoading] = React.useState(false);

  const { t, i18n } = useTranslation();

  const timeoutIdRef = React.useRef();
  const queryParameters = new URLSearchParams(window.location.search);
  const entityQuery = queryParameters.getAll("ent");

  React.useEffect (() => {
    if (entityQuery) {
      dataFromGet(entityQuery);
    }
  },[])

  const handleNewData = (data) => {
    setOptions([]);
    setOptions(data.search.map((x, index) => ({index: index,id: x.id, label: x.label,  url: x.concepturi, description: x.description})));
    setIsLoading(false);
  }

  const dataFromGet = (params) => {
    for (let param of params) {
      let url = `${baseURL}/autocomplete?entity=${param}&language=${i18n.language}`
      fetch(url)
        .then(response => response.json())
        .then(data =>
            data.search.length > 0  ?
            props.addEntity({index: 0,id: data.search[0].id, label: data.search[0].label,  url: data.search[0].concepturi, description: data.search[0].description}) : null             
            //data.search.map((x, index) => ({index: index,id: x.id, label: x.label,  url: x.concepturi, description: x.description}))  
          );
    } //data.search.map((x, index) => ({index: index,id: x.id, label: x.label,  url: x.concepturi, description: x.description}
  }

  const handleAuto = (word) => {
    if (word != "" && word != null) {
      let url = `${baseURL}/autocomplete?entity=${word}&language=${i18n.language}`
      fetch(url)
        .then(response => response.json())
        .then(data => handleNewData(data));
    }
    else {
      setOptions([]);
    }
    //setIsLoading(false);
  }

  const handleOpt = async (word) => {
    clearTimeout(timeoutIdRef.current);
    setInputValue(word);
    setIsLoading(true);
    timeoutIdRef.current = setTimeout(() => {
      handleAuto(word);
    }, 500);
  }
  
  const getDefaultText = () => {
    if (isLoading && inputValue != "") {
      return t('LoadingSearch');
    }
    else if (inputValue == "") {
      return t('WaitingSearch');
    }
    else {
      return t('NoResultSearch');
    }
  }
  
  // TODO AGREGAR TRADUCCION
  return (
    <div>
      <Autocomplete
        forcePopupIcon={false}                // Elimina flechita autocompletado
        noOptionsText={getDefaultText()}
        onClose={() => setIsLoading(false)}   // al cerrar autocompletado
        filterOptions={(x) => x}              // quita filtro de opciones (no se necesita ya que obtengo de API)
        value={value}
        onChange={(event, newValue) => {
            if (newValue != null) {
              props.addEntity(newValue);
            }
          }}
        inputValue={inputValue}
        onInputChange={(event, newInputValue, reason) => {
          if (reason === 'input') {
            handleOpt(newInputValue);
          }
          if (reason === 'reset') {
            setInputValue("");
            setOptions([]);
            setValue("");
          }
          if (reason === 'clear') {
            setInputValue("");
            setOptions([]);
            setValue("");
          }
        }}

        disablePortal
        id="combo-box-demo"
        options={options}
        sx={{ width: 300 }}
        renderInput={(params) => 
          <TextField {...params}
            InputLabelProps={{ shrink: true }}
            // hiddenLabel
            label={t("Entity")} //TODO Texto IDIOMA
            variant="filled"

            //helperText="Escriba una entidad"  // TODO IDIOMA
            placeholder='Escriba una entidad'
            InputProps={{
              ...params.InputProps,
              endAdornment: (
                <React.Fragment>
                  {isLoading && inputValue!= "" ? <CircularProgress color="inherit" size={20} /> : null}
                  {params.InputProps.endAdornment}
                </React.Fragment>
              ),
            }}
          />
        }
        renderOption={(props, option) => {
            return  <li {...props}>
                      <Grid container alignItems="center">
                        <Grid item>
                          <Box
                            //component={LocationOnIcon}
                            //sx={{ color: 'text.secondary', mr: 2 }}
                          />
                        </Grid>
                        <Grid item xs>
                            {option.label}&nbsp;
                            <Typography variant="caption" color="text.secondary" display="inline">
                              {option.id}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {option.description}
                            </Typography>
                        </Grid>
                      </Grid>
                    </li>
          }
        }
      />

      
    </div>
    );
  }
  



  