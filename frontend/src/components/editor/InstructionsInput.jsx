import styles from './InstructionsInput.module.css';
import { useMemo, useEffect } from 'react';
import debounce from 'lodash.debounce';

const InstructionsInput = ({ activeTab, onChange, onType }) => {
    if (!activeTab) return null;

    const lines = activeTab.content ? activeTab.content.split('\n') : [''];

    // Debounce the onType callback to avoid performance issues
    const debouncedOnType = useMemo(
        () => debounce(onType, 300),
        [onType]
    );

    useEffect(() => {
        // Cleanup debounced function on unmount
        return () => {
            debouncedOnType.cancel();
        };
    }, [debouncedOnType]);

    const handleChange = (e) => {
        onChange(e.target.value);
        debouncedOnType(e.target.value);
    }

    return (
        <div className={styles.InstructionsContainer}>
            <div className={styles.LineNumbers}>
                {lines.map((_, idx) => (
                    <div key={idx} className={styles.LineNumber}>
                        {idx + 1}
                    </div>
                ))}
            </div>
            <textarea
                className={styles.InstructionsMainArea}
                value={activeTab.content}
                onChange={handleChange}
            />
        </div>
    );
}

export default InstructionsInput;