import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';


const top5Songs = [
  { label: 'Organise'},
  { label: 'Joha'},
  { label: 'Terminator'},
  { label: 'Dull'},
  { label: 'Nzaza'},
];


export default function Autocom(props) {
  const [options, setOptions] = React.useState([]);
  const [value, setValue] = React.useState("");
  const [inputValue, setInputValue] = React.useState('');

  const handleOpt = async (word) => {
    // TODO PROBLEMA DE COOKIE AQUI
    setInputValue(word);
    let url = `http://localhost:8080/autocomplete?entity=${word}`
    fetch(url)
        .then(response => response.json())
        .then(data => setOptions(data.search.map((x) => ({label: x.label,id: x.id,url: x.concepturi})))); // 
    }
  
  return (
      <Autocomplete
        value={value}
        onChange={(event, newValue) => {
            console.log(newValue);
            props.addEntity(newValue);
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
      />
    );
  }
  