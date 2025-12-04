import styles from './ConsoleButton.module.css';

function ConsoleButton({icon, enabled=true, onClick}) {
    return (
        <button className={`${styles.ConsoleButton} ${!enabled && styles.Disabled}`} onClick={enabled && onClick} aria-label="Clear Console">
            {icon}
        </button>
    );
}

export default ConsoleButton;