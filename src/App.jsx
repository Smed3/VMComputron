import styles from './App.module.css';
import Header from "./components/Header.jsx";
import MemoryWindow from "./components/memoryWindow/MemoryWindow.jsx";
import ConsoleWindow from "./components/consoleWindow/ConsoleWindow.jsx";
import InstructionsWindow from './components/instructionsWindow/InstructionsWindow.jsx';
import "simplebar-react/dist/simplebar.min.css";

function App() {

  const messages = [
    "Program started.",
    "Loading modules...",
    "Modules loaded successfully.",
    "Executing main function...",
    "Error: Unable to fetch data from server.",
    "Retrying connection...",
    "Connection established.",
    "Program terminated."
  ];

  return (
    <div className={styles.App__wrapper}>
      <Header />

      <div className={styles.Main__content__wrapper}>
        <div className={styles.Left__content__wrapper}>
          <div className={styles.Code__wrapper}>
            <div className={styles.Code__display__wrapper}>
              Code Display
            </div>
            <InstructionsWindow />
          </div>
          <div className={styles.Console__wrapper}>
            <ConsoleWindow msgs={messages}/>
          </div>
        </div>
        <div className={styles.Memory__wrapper}>
          <MemoryWindow />
        </div>
      </div>
    </div>
  )
}

export default App
