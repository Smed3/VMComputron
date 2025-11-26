import styles from './MemoryWindow.module.css';
import Registers from "./Registers.jsx";
import Ram from "./Ram.jsx";

function MemoryWindow() {
  return (
    <div className={styles.MemoryWindow}>
      <div className={styles.Header}>
        <h2 className={styles.Window__title}>CPU & Memory</h2>
      </div>

      <div className={styles.Registers__wrapper}>
        <div className={styles.Section__title__wrapper}>
          <h3 className={styles.Section__title}>Registers</h3>
        </div>

        <Registers className={styles.Registers}/>
      </div>

      <div className={styles.Ram__wrapper}>
        <div className={styles.Section__title__wrapper}>
          <h3 className={styles.Section__title}>RAM</h3>
        </div>

        <Ram className={styles.Ram}/>
      </div>
    </div>
  );
}

export default MemoryWindow;