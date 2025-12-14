import styles from "./RegistersBoard.module.css";
import {useEffect, useState} from "react";
import {useServerContext} from "../../contexts/ServerContext.jsx";

function RegistersBoard({ className, register, type }) {
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

  // const values = [
  //   100000,40000,20000,10000,
  //   4000,2000,1000,
  //   400,200,100,
  //   40,20,10,
  //   4, 2, 1
  // ]

  const values = [
    100000,40000,20000,10000,
    4000,2000,1000,
    400,200,100,
    40,20,10,
    4, 2, 1
  ]

  const [value, setValue] = useState(0);
  const [bulbs, setBulbs] = useState([]);
  const {registers, updateRegister, updateMemory} = useServerContext();

  useEffect(() => {
    const currentRegister = registers[register];
    if (!!currentRegister) {
      setValue(currentRegister[0]);
      setBulbs(currentRegister[1]);
    }
  }, [registers, register]);

  const currentRegisters = registerPresets[type];

  function handleActivatingRegister(buttonIndex) {
    const newBulbs = [...bulbs];

    newBulbs[buttonIndex] = newBulbs[buttonIndex] === 1 ? 0 : 1;

    const newValue = newBulbs.reduce((sum, bit, i) => sum + (bit ? values[i] : 0), 0);

    if (register !== 'MEM') {
      updateRegister(register, newValue);
    } else {
      updateMemory(newValue);
    }
  }

  return (
      <div className={`${styles.RegistersBoard} ${className || ''}`}>
        <div className={styles.Registers__wrapper}>
          {currentRegisters.map((color, index) => (
              <button
                  key={index}
                  className={`
              ${styles.Register}
              ${colorToClass[color]}
              ${bulbs[index] === 1 ? styles.Active : ''}
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