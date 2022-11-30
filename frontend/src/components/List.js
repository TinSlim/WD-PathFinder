import React from 'react';
const List = ({ entities, deleteEntity }) => (
    <ul>
        {entities.map((ent, index) => (

                <li> {ent} 
                <button className="delete" onClick={() => {deleteEntity(index);}}></button></li>

        ))}
    </ul>
  );
  
  export default List;