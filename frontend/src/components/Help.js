import React, { useRef, useState, useEffect } from 'react';
import { Stack, Zoom, Button } from '@mui/material';
import Typography from '@mui/material/Typography';
import InfoIcon from '@mui/icons-material/Info';
import  './i18n';
import { useTranslation } from 'react-i18next';

export default function Hekp(props) {
    const { t, i18n } = useTranslation();

return(
    <Zoom hidden={!props.showingInfo} in={props.showingInfo} >
        <Stack>
            <Stack className="ml-3 mr-3" spacing={1} direction="row" alignItems="center">
                <Button fullWidth={true} className='mt-3' onClick={props.changeInfo} color="secondary">
                    <InfoIcon/>&nbsp; {t("Return")}
                </Button>
            </Stack>
        
            <Stack className='ml-3 mr-3'>
                <Stack style={{display: 'flex',height: '500px', overflowY: 'auto'}}>
                    <Stack>
                        <Typography variant='h6'>
                            Uso:
                        </Typography>
                        
                        <Typography variant="body2" component="div">
                            1. {t('Ayuda1')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso1_edit.png')}/>
                        <Typography variant="body2" component="div">
                            2. {t('Ayuda2')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso2_edit.png')}/>
                        <Typography variant="body2" component="div">
                            3. {t('Ayuda3')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso3_edit.png')}/>
                        <Typography variant="body2" component="div">
                            4. {t('Ayuda4')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso4_edit.png')}/>
                        <Typography variant="body2" component="div">
                            • {t('Ayuda5')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso5_edit.png')}/>
                        <Typography variant="body2" component="div">
                            • {t('Ayuda6')}.
                        </Typography>
                        <img className="mt-1 mb-2" src={require('./../images/help/Paso6_edit.png')}/>
                    </Stack>

                    <Stack>
                        <Typography variant='h6'>
                            WoolNet:
                        </Typography>
                        <Typography variant="body2" component="div">
                            WoolNet es la aplicación que permite obtener los caminos que unen entidades de Wikdata.
                        </Typography>
                    </Stack>

                
                {/*
                <Typography variant="body2" component="div">
                    • Si los resultados son demasiados, puede comprimirlos usando el botón COMPRIMIR.
                </Typography>
                */}
                </Stack>
            </Stack>
            
        </Stack>

    </Zoom>)}