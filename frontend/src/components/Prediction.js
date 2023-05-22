import React from 'react';

const Prediction = ({ entities}) => (
    <div>
        {entities.map((ent, index) => (
            <div>
                <ul>
                    <li>{ent.id}</li>
                    <li>{ent.label}</li>
                    <li>{ent.url}</li>
                </ul>
            </div>
        ))}
    </div>
  );
  
export default Prediction;