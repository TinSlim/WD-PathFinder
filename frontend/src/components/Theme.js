import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
        primary: {
            //light: '#757ce8',
            main: '#8fbbaf',
            //dark: '#002884',
            contrastText: '#fff',
        },
        secondary: {
            main: '#9ab5c1',
            contrastText: '#fff',
        },
        error: {
            main: '#cd3661',//'#c06c84',           cf003b       ca3521
        },
        success: {
            main: '#49bf9f',
            contrastText: '#fff',
        }

    },
});
