import React from 'react';
import Stack from '@mui/material/Stack';
import GitHubIcon from '@mui/icons-material/GitHub';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';

export default function Footer() {
    return (
        
        <footer id="footer" className="footer mt-auto has-background-primary has-text-white">
            <Stack direction="row" alignItems="center">
                <Stack sx={{ width: '33%' }} alignItems="center" justifyContent="center">
                    <IconButton onClick={() => window.open('https://github.com/TinSlim/WD-PathFinder')}>    
                        <GitHubIcon/>
                    </IconButton>
                </Stack>
                <Stack sx={{ width: '33%' }} alignItems="center" justifyContent="center">
                    <Typography variant="body1">
                        <strong className="has-text-white">© 2023 WoolNet</strong> by <a className='link' href="https://ctorresg.cl">Cristóbal Torres</a>.
                    </Typography>
                </Stack>
                <Stack sx={{ width: '33%' }} alignItems="center" justifyContent="center">
                    <img src={require('./../images/Wikidata_Stamp_Rec_Dark.svg')}
                                width="130px"/>
                </Stack>

            </Stack>
        </footer>
    );
}
