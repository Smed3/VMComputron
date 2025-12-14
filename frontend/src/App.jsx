import styles from './App.module.css';
import Header from "./components/header/Header.jsx";
import MemoryWindow from "./components/memoryWindow/MemoryWindow.jsx";
import ConsoleWindow from "./components/consoleWindow/ConsoleWindow.jsx";
import InstructionsWindow from './components/instructionsWindow/InstructionsWindow.jsx';
import OnboardingHint from "./components/onboarding/OnboardingHint.jsx";

import "simplebar-react/dist/simplebar.min.css";
import Editor from "./components/editor/Editor.jsx";
import {useState} from "react";
import { useEffect } from "react";
import { useRef } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {useServerContext} from "./contexts/ServerContext.jsx";

function App() {
  const [showHint, setShowHint] = useState(true);
  const {messages, input, setInput, sendMessage} = useServerContext();

  return (
    <div className={styles.App__wrapper}>
      <Header/>
      {/*<div>*/}
      {/*  <h2>WebSocket чат</h2>*/}
      {/*  <ul>*/}
      {/*    {messages.map((msg, i) => <li key={i}>{msg}</li>)}*/}
      {/*  </ul>*/}
      {/*  <input value={input} onChange={(e) => setInput(e.target.value)}/>*/}
      {/*  <button onClick={sendMessage}>Отправить</button>*/}
      {/*</div>*/}
      {showHint && (
        <OnboardingHint onClose={() => setShowHint(false)} />
      )}
      <div className={styles.Main__content__wrapper}>
        <div className={styles.Left__content__wrapper}>
          <div className={styles.Code__wrapper}>
            <div className={styles.Code__display__wrapper}>
              <Editor setEditorFilter={setEditorFilter}/>
            </div>
            <InstructionsWindow />
          </div>
          <div className={styles.Console__wrapper}>
            <ConsoleWindow/>
          </div>
        </div>
        <div className={styles.Memory__wrapper}>
          <MemoryWindow/>
        </div>
      </div>
    </div>
  )
}

export default App