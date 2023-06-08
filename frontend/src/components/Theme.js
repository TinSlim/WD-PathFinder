import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
        primary: {
            //light: '#757ce8',
            main: '#D084F5',
            //dark: '#002884',
            contrastText: '#fff',
        },
        secondary: {
            light: '#ff7961',
            main: '#B769C2',
            dark: '#ba000d',
            contrastText: '#000',
        },
        error: {
            main: '#F54C76',
        }
    },
});
