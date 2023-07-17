import React from 'react';
import Stack from '@mui/material/Stack';

export default function Footer() {
    return (
        
        <footer id="footer" className="footer mt-auto has-background-primary has-text-white">
            <Stack direction="row" justifyContent="space-around">
                <Stack>
                    <p>
                    <strong className="has-text-white">© 2023 WoolNet</strong> by <a className='link' href="https://ctorresg.cl">Cristóbal Torres</a>.
                    </p>
                </Stack>
                <Stack>
                <img src={require('./../images/Wikidata_Stamp_Rec_Dark.svg')}
                                width="130px"/>
                </Stack>
            </Stack>
            
        </footer>
    );
}
