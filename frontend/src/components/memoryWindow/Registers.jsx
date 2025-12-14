import styles from './Registers.module.css';
import {useState} from "react";
import RegistersBoard from "./RegistersBoard.jsx";
import {useServerContext} from "../../contexts/ServerContext.jsx";

function Registers({ className }) {
  const registers = ['PC', 'SP', 'A', 'X', 'RH', 'RL']
  const [selectedRegister, setSelectedRegister] = useState('PC');

  const {storeToMemory, loadFromMemory} = useServerContext();

  return (
    <div className={`${styles.Registers} ${className || ''}`}>
      <div className={styles.Registers__option__wrapper}>
        <div className={styles.Registers__switch}>
          {registers.map((register) => (
            <button
              key={register}
              className={`
                ${styles.Register__option} 
                ${register === selectedRegister ? styles.Active__option : styles.Inactive__option}
              `}
              onClick={() => setSelectedRegister(register)}
            >
              <p className={styles.Register__option__title}>
                {register}
              </p>
            </button>
          ))}
        </div>

        <div className={styles.Buttons__wrapper}>
          <button
            className={styles.File__button}
            onClick={() => loadFromMemory(selectedRegister)}
          >
            <svg
              // className={styles.File__button__icon}
              width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M14 10V12.6667C14 13.0203 13.8595 13.3594 13.6095 13.6095C13.3594 13.8595 13.0203 14 12.6667 14H3.33333C2.97971 14 2.64057 13.8595 2.39052 13.6095C2.14048 13.3594 2 13.0203 2 12.6667V10M11.3333 5.33333L8 2M8 2L4.66667 5.33333M8 2V10"
                strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
              />
            </svg>
            <p className={styles.File__button__title}>
              Load
            </p>
          </button>
          <button
            className={styles.File__button}
            onClick={() => storeToMemory(selectedRegister)}
          >
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M14 10V12.6667C14 13.0203 13.8595 13.3594 13.6095 13.6095C13.3594 13.8595 13.0203 14 12.6667 14H3.33333C2.97971 14 2.64057 13.8595 2.39052 13.6095C2.14048 13.3594 2 13.0203 2 12.6667V10M4.66667 6.66667L8 10M8 10L11.3333 6.66667M8 10V2"
                strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"
              />
            </svg>
            <p className={styles.File__button__title}>
              Store
            </p>
          </button>
        </div>
      </div>

      <RegistersBoard className={styles.Registers__board} register={selectedRegister} type={'cpu'} />
      <RegistersBoard className={styles.Registers__board} register={'MEM'} type={'memory'} />
    </div>
  );
}

export default Registers;