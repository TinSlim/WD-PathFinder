import React from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import Tooltip from '@mui/material/Tooltip';
import Divider from '@mui/material/Divider';
import Stack from '@mui/material/Stack';

const ListT = ({ entities, deleteEntity }) => (
    <div style={{overflowY: 'auto', height:"45vh"}}>
        {entities.map((ent, index) => (
            <Stack>
                <div style={{overflow: 'hidden'}} className="field is-grouped mt-2 mb-2 ml-2 mr-1">
                    <label className="label">
                        <Grid item xs>
                            <a href={ent.url}>{ent.id}</a>
                            <Tooltip title={ent.description}>
                                <Typography variant="body2" color="text.secondary">
                                {ent.label}
                                </Typography>
                            </Tooltip>
                        </Grid>
                    </label>
                    <button style={{marginLeft:'auto',marginRight: 0}} 
                    className="button mr-1" onClick={() => {deleteEntity(index);}}>
                    <DeleteIcon/>
                    </button>
                    
                </div>
                <Divider />
            </Stack>
            
        ))}
    </div>
  );
  
  export default ListT;