import styles from "./Ram.module.css";
import {useServerContext} from "../../contexts/ServerContext.jsx";

function Ram({ className }) {
  const numbers = [0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 15, 16, 17,
    20, 21, 22, 23, 24, 25, 26, 27, 30, 31, 32, 33, 34, 35, 36, 37,
    40, 41, 42, 43, 44, 45, 46, 47, 50, 51, 52, 53, 54, 55, 56, 57,
    60, 61, 62, 63, 64, 65, 66, 67, 70, 71, 72, 73, 74, 75, 76, 77]


  const {ram} = useServerContext();

  const parts = ["part1", "part2", "part3", "part4", "part5"];

  return (
    <div className={`${styles.Ram} ${className || ''}`}>
      {parts.map((partName, partIndex) => (
        <div key={partIndex} className={styles.Column}>

          {/* Number column */}
          <div className={styles.Number__column}>
            <div className={styles.Number__column__header}>
              <p>PC</p>
            </div>
            <div className={styles.Number__column__content}>
              {Array.from({ length: 16 }).map((_, i) => (
                <div key={i} className={styles.Number__cell}>
                  <p className={styles.Cell__title}>{i + 16 * partIndex}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Memory column */}
          <div className={styles.Mem__column}>
            <div className={styles.Mem__column__header}></div>
            <div className={styles.Mem__column__content}>
              {ram?.[partName]?.flat().map((cell, i) => (
                <div
                  key={i}
                  className={`
                    ${styles.Cell} 
                    ${cell !== '00' ? styles.Cell__light__green : ''}
                  `}
                >
                  <p className={styles.Cell__title}>{cell}</p>
                </div>
              ))}
            </div>
          </div>

        </div>
      ))}
    </div>
  );
}

export default Ram;