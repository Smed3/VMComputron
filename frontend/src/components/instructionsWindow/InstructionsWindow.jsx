
import { useState } from 'react';
import InstructionsTable from './InstructionsTable.jsx';
import styles from './InstructionsWindow.module.css';
import SearchInputSection from './SearchInputSection.jsx';

function InstructionsWindow() {
    const [searchInput, setSearchInput] = useState('');

    return (
        <div className={styles.InstructionsWindow}>
            <SearchInputSection input={searchInput} setInput={setSearchInput} />
            <InstructionsTable search={searchInput}/>
        </div>
    )
}

export default InstructionsWindow;
