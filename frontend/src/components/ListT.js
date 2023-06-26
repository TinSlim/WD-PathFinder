import React from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import Tooltip from '@mui/material/Tooltip';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

const ListT = ({ entities, deleteEntity }) => (
    <div style={{overflowY: 'auto', height:"50vh"}}> {/*antes 45vh*/}
        {entities.map((ent, index) => (
            <Stack>
                <Stack 
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                spacing={0}
                style={{overflow: 'hidden'}} className="field is-grouped mt-2 mb-2 ml-2 mr-1">
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
                    <Button style={{marginLeft:'auto',marginRight: 0}}
                            variant="contained"
                            color="secondary"
                            className="button mr-1" onClick={() => {deleteEntity(index);}}>
                        <DeleteIcon/>
                    </Button>
                    
                </Stack>
                <Divider />
            </Stack>
            
        ))}
    </div>
  );
  
  export default ListT;