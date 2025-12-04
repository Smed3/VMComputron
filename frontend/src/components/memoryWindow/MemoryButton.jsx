import styles from './MemoryButton.module.css';

function ConsoleButton({ className, icon, enabled=true, onClick }) {
  return (
    <button
      className={`
        ${styles.MemoryButton} ${className || ''} ${!enabled && styles.Disabled}
      `}
      onClick={enabled && onClick}
      aria-label="Clear Console"
    >
      {icon}
    </button>
  );
}

export default ConsoleButton;