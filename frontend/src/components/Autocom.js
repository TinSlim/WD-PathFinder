import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';

export default function Autocom(props) {
  const [options, setOptions] = React.useState([]);
  const [value, setValue] = React.useState("");
  const [inputValue, setInputValue] = React.useState('');

  const handleOpt = async (word) => {
    // TODO PROBLEMA DE COOKIE AQUI
    setInputValue(word);
    if (word != "" && word != null) {
      let url = `http://localhost:8080/autocomplete?entity=${word}`
      fetch(url)
        .then(response => response.json())
        .then(data => setOptions(data.search.map((x) => ({label: x.label,id: x.id,url: x.concepturi}))));
    }
    else {
      setOptions([]);
    }
  }
    
  
  return (
    <div className='mb-3'>
      <Autocomplete
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
        renderInput={(params) => <TextField {...params} label="Songs" />}
        

        renderOption={(props, option) => { return <h2> ok2</h2>}}
          /*
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
  



  