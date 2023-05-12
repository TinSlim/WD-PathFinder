import React from 'react';

export default function Footer() {
    return (
        <footer id="footer" class="footer mt-auto has-background-primary has-text-white">
            <div class="columns">
                <div class="content has-text-centered column">
                    <p>
                    <strong class="has-text-white">© 2023 WoolNet</strong> by <a href="https://ctorresg.cl">Cristóbal Torres</a>.
                    </p>
                </div>
                <div class="content has-text-centered column">
                <img src={require('./../images/Wikidata_Stamp_Rec_Dark.svg')}
                                width="150px"/>
                </div>
            </div>
        </footer>
    );
}
