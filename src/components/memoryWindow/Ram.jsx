import styles from "./Ram.module.css";

function Ram({ className }) {
  return (
    <div className={`${styles.Ram} ${className || ''}`}>
      {Array.from({ length: 256 }).map((_, i) => (
        <div className={styles.Cell} key={i}>
          <p className={styles.Cell__title}>00</p>
        </div>
      ))}
    </div>
  );
}

export default Ram;