import React from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import Tooltip from '@mui/material/Tooltip';

const ListT = ({ entities, deleteEntity }) => (
    <div style={{overflowY: 'auto', height:"45vh"}} className='mb-3'>
        {entities.map((ent, index) => (
            <div style={{overflow: 'hidden'}} className="field is-grouped mb-4">
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
        ))}
    </div>
  );
  
  export default ListT;