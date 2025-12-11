import styles from './MemoryWindow.module.css';
import Registers from "./Registers.jsx";
import Ram from "./Ram.jsx";
import {useState} from "react";
import Chevron from "../consoleWindow/icons/Chevron.jsx";
import MemoryButton from "./MemoryButton.jsx";
import SimpleBar from "simplebar-react";

function MemoryWindow() {
  const [collapsed, setCollapsed] = useState(false);

  const toggleCollapsed = () => {
    setCollapsed(prev => !prev);
  };

  return (
    <div className={styles.MemoryWindow}>
      <div
        className={`
          ${styles.Header} ${collapsed ? styles.Header__collapsed : ''
        }`}
      >
        <MemoryButton
          className={styles.Hide__button}
          icon={<Chevron direction={collapsed ? 'up' : 'down'} />}
          onClick={toggleCollapsed}
        />

        <h2 className={styles.Window__title}>CPU & Memory</h2>
      </div>

      <div
        className={`
          ${styles.Content} ${collapsed ? styles.Content__collapsed : ''}
        `}
      >
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
    </div>
  );
}

export default MemoryWindow;