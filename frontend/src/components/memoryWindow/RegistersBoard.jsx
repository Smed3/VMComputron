import styles from "./RegistersBoard.module.css";
import {useMemo, useState} from "react";

function RegistersBoard({ className, setRegisters, type }) {
  const registerPresets = {
    cpu: [
      'green','orange','orange','orange',
      'green','green','green',
      'orange','orange','orange',
      'green','green','green',
      'orange','orange','orange'
    ],
    memory: [
      'red','blue','blue','blue',
      'red','red','red',
      'blue','blue','blue',
      'red','red','red',
      'blue','blue','blue'
    ]
  };

  const colorToClass = {
    green: styles.Green,
    orange: styles.Orange,
    red: styles.Red,
    blue: styles.Blue
  };

  const values = [
    100_000,40_000,20_000,10_000,
    4000,2000,1000,
    400,200,100,
    40,20,10,
    4, 2, 1
  ]

  const registers = registerPresets[type];

  const [activeRegisters, setActiveRegisters] = useState([0, 1, 2, 3, 10, 11, 12]);
  const value = useMemo(() => {
    return activeRegisters.reduce(
      (sum, idx) => sum + values[idx],
      0
    );
  }, [activeRegisters]);

  function handleActivatingRegister(number) {
    setActiveRegisters(prev =>
      prev.includes(number)
        ? prev.filter(i => i !== number)
        : [...prev, number]
    );
  }

  return (
    <div className={`${styles.RegistersBoard} ${className || ''}`}>
      <div className={styles.Registers__wrapper}>
        {registers.map((color, index) => (
          <button
            key={index}
            className={`
              ${styles.Register}
              ${colorToClass[color]}
              ${activeRegisters.includes(index) ? styles.Active : ''}
            `}
            onClick={() => handleActivatingRegister(index)}
          >

          </button>
        ))}
      </div>

      <div className={styles.Counter}>
        <div className={styles.Counter__title__wrapper}>
          <p className={styles.Counter__title}>{type === 'cpu' ? 'CPU' : 'Memory'}</p>
        </div>
        <div className={styles.Counter__window}>
          <p className={styles.Counter__number}>{value}</p>
        </div>
      </div>
    </div>
  );
}

export default RegistersBoard;