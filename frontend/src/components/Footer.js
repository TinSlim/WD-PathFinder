import React from 'react';
import Stack from '@mui/material/Stack';

export default function Footer() {
    return (
        
        <footer id="footer" class="footer mt-auto has-background-primary has-text-white">
            <Stack direction="row" justifyContent="space-around">
                <Stack>
                    <p>
                    <strong class="has-text-white">© 2023 WoolNet</strong> by <a href="https://ctorresg.cl">Cristóbal Torres</a>.
                    </p>
                </Stack>
                <Stack>
                <img src={require('./../images/Wikidata_Stamp_Rec_Dark.svg')}
                                width="150px"/>
                </Stack>
            </Stack>
            
        </footer>
    );
}
