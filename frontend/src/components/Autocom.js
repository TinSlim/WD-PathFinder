import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import InputAdornment from '@mui/material/InputAdornment';
import CircularProgress from '@mui/material/CircularProgress';


import Box from '@mui/material/Box';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';

const { baseURL } = require('config');

export default function Autocom(props) {
  const [options, setOptions] = React.useState([]);
  const [value, setValue] = React.useState("");
  const [inputValue, setInputValue] = React.useState('');
  const [isLoading, setIsLoading] = React.useState(false);

  const timeoutIdRef = React.useRef();
  
  const handleNewData = (data) => {
    setOptions([]);
    setOptions(data.search.map((x, index) => ({index: index,id: x.id, label: x.label,  url: x.concepturi, description: x.description})));
    setIsLoading(false);
  }

  const handleAuto = (word) => {
    if (word != "" && word != null) {
      let url = `${baseURL}/autocomplete?entity=${word}`
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
      return "loading";
    }
    else if (inputValue == "") {
      return "Esperando";
    }
    else {
      return "nada";
    }
  }
  
  return (
    <div className='mt-3 mb-3'>
      <Autocomplete
        noOptionsText={getDefaultText()}
        onClose={() => setIsLoading(false)}   // al cerrar autocompletado
        filterOptions={(x) => x}              // quita filtro de opciones (no se necesita ya que obtengo de API)
        value={value}
        onChange={(event, newValue) => {
            if (newValue != null) {
              props.addEntity(newValue);
            }
            //setInputValue("");
            //setOptions([]); //
            //setValue(null); //
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
            label="Entidad"
            variant="filled"
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
                            {option.label}
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
  



  