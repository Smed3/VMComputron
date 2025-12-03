import styles from './InstructionsInput.module.css';

const InstructionsInput = ({ activeTab, onChange, onType }) => {
    if (!activeTab) return null;

    const lines = activeTab.content ? activeTab.content.split('\n') : [''];

    const handleChange = (e) => {
        onChange(e.target.value);
        onType(e.target.value);
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