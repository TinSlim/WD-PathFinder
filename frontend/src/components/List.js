import React from 'react';
const List = ({ entities, deleteEntity }) => (
    <div style={{overflowY: 'auto', height:"45vh"}} className='mb-3'>
        {entities.map((ent, index) => (
            <div style={{overflow: 'hidden'}} className="field is-grouped mb-4">
                <label className="label">{ent}</label>
                <button style={{marginLeft:'auto',marginRight: 0}} 
                className="button mr-1" onClick={() => {deleteEntity(index);}}>
                <i className="fa fa-futbol-o"></i>
                </button>
            </div>
        ))}
    </div>
  );
  
  export default List;