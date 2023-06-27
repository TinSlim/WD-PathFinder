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
                <Button fullWidth={true} onClick={props.changeInfo} color="secondary">
                    <InfoIcon/>&nbsp; {t("Return")}
                </Button>
            </Stack>
        
            <Stack className='ml-3 mr-3 mt-1'>
                <Stack style={{display: 'flex',height: '450px', overflowY: 'auto'}}>
                    
                    <Stack className='mb-2'>
                        <Typography variant='h6'>
                            WoolNet:
                        </Typography>
                        <Typography variant="body2" component="div">
                            {t('WoolNet')}
                        </Typography>
                    </Stack>
                    
                    <Stack>
                        <Typography variant='h6'>
                            {t('Instructions')}:
                        </Typography>
                        
                        <Typography variant="body2" component="div">
                            1. {t('Ayuda1')}.
                        </Typography>
                        
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso1_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso1_edit.png')}/>
                        }
                        <Typography variant="body2" component="div">
                            2. {t('Ayuda2')}.
                        </Typography>
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso2_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso2_edit.png')}/>
                        }
                        <Typography variant="body2" component="div">
                            3. {t('Ayuda3')}.
                        </Typography>
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso3_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso3_edit.png')}/>
                        }
                        <Typography variant="body2" component="div">
                            4. {t('Ayuda4')}.
                        </Typography>
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso4_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso4_edit.png')}/>
                        }
                        <Typography variant="body2" component="div">
                            • {t('Ayuda5')}.
                        </Typography>
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso5_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso5_edit.png')}/>
                        }
                        <Typography variant="body2" component="div">
                            • {t('Ayuda6')}.
                        </Typography>
                        {i18n.language == 'en' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso6_en.png')}/>
                        }
                        {i18n.language == 'es' &&
                            <img className="mt-1 mb-2" src={require('./../images/help/Paso6_edit.png')}/>
                        }
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