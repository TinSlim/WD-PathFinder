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
    setOptions(data.search.map((x) => ({label: x.label, id: x.id, url: x.concepturi, description: x.description})));
    setIsLoading(false);
  }

  const handleAuto = (word) => {
    if (word != "" && word != null) {
      let url = `${baseURL}/autocomplete?entity=${word}`
      console.log(url);
      // TODO DESCOMENTAR
      //fetch(url)
      //  .then(response => response.json())
      //  .then(data => handleNewData(data));
      // TODO BORRAR
      handleNewData({"search":[{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q298","description":"country in South America","label":"Chile","id":"Q298"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q165199","description":"fruit of plants from the genus Capsicum, members of the nightshade family, Solanaceae","label":"chili pepper","id":"Q165199"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q1045129","description":"asteroid","label":"4636 Chile","id":"Q1045129"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q5490088","description":"Q5490088","label":"Corporación de Promoción y Defensa de los Derechos del Pueblo","id":"Q5490088"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q18418541","description":"bus station in Buenos Aires, Argentina","label":"Chile","id":"Q18418541"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q37446942","description":"family name","label":"Chile","id":"Q37446942"},{"concepturi":"http:\/\/www.wikidata.org\/entity\/Q99299995","description":"country of Chile as depicted in Star Trek","label":"Chile","id":"Q99299995"}]});
    }
    else {
      setOptions([]);
    }
    
  }

  const handleOpt = async (word) => {
    clearTimeout(timeoutIdRef.current);
    setInputValue(word);
    setIsLoading(true);
    timeoutIdRef.current = setTimeout(() => {
      handleAuto(word);
    }, 500);
  }
    
  
  return (
    <div className='mt-3 mb-3'>
      <Autocomplete
        onClose={() => setIsLoading(false)} // al cerrar autocompletado
        filterOptions={(x) => x}            // quita filtro de opciones (no se necesita ya que obtengo de API)
        value={value}
        onChange={(event, newValue) => {
            if (newValue != null) {
              props.addEntity(newValue);
            }
            setInputValue("");
          }}
        inputValue={inputValue}
        onInputChange={(event, newInputValue) => {
          handleOpt(newInputValue);
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
                  {isLoading ? <CircularProgress color="inherit" size={20} /> : null}
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
        
        

            
        /*
        renderOption={(props, option) => { return <h2> ok2</h2>}}
          
          const matches = option.structured_formatting.main_text_matched_substrings;
          const parts = parse(
            option.structured_formatting.main_text,
            matches.map((match: any) => [match.offset, match.offset + match.length]),
          );
  
          return (
            <li {...props}>
              <Grid container alignItems="center">
                <Grid item>
                  <Box
                    component={LocationOnIcon}
                    sx={{ color: 'text.secondary', mr: 2 }}
                  />
                </Grid>
                <Grid item xs>
                  {parts.map((part, index) => (
                    <span
                      key={index}
                      style={{
                        fontWeight: part.highlight ? 700 : 400,
                      }}
                    >
                      {part.text}
                    </span>
                  ))}
                  <Typography variant="body2" color="text.secondary">
                    {option.structured_formatting.secondary_text}
                  </Typography>
                </Grid>
              </Grid>
            </li>
          );
        }}*/

      />
    </div>
    );
  }
  



  