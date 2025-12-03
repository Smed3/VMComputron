import styles from './SearchInputSection.module.css';
import SearchIcon from './SearchIcon.jsx';
import ClearIcon from './ClearIcon.jsx';

function SearchInputSection({input, setInput}) {

    const clearInput = () => {
        setInput('');
    }

    return (
        <label className={styles.SearchInputSection}>
            <input maxLength={7} type="text" placeholder="Search..." value={input} onInput={(e) => setInput(e.target.value)} className={styles.SearchInput}>            
            </input>
            {input.length > 0 ? (
                <ClearIcon onClick={clearInput} />
            ) : (
                <SearchIcon />
            )}
        </label>
    );
}

export default SearchInputSection;