import {createContext, useContext, useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ServerContext = createContext();

export function ServerContextProvider({ children }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const client = useRef(null);

  const [consoleLines, setConsoleLines] = useState([]);

  const [PC, setPC] = useState([0, []]);
  const [SP, setSP] = useState([0, []]);
  const [A, setA] = useState([0, []]);
  const [X, setX] = useState([0, []]);
  const [RH, setRH] = useState([0, []]);
  const [RL, setRL] = useState([0, []]);

  const [memory, setMemory] = useState([0, []]);
  const [ram, setRam] = useState([]);

  const [activeCode, setActiveCode] = useState("");

  useEffect(() => {
    client.current = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected!');
        client.current.subscribe('/topic/greetings', (msg) => {
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body.content]);
        });

        client.current.subscribe('/topic/ram', (msg) => {
          const data = JSON.parse(msg.body);
          setRam(data);
        });

        client.current.subscribe('/topic/register/PC', (msg) => {
          const data = JSON.parse(msg.body);
          setPC([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/SP', (msg) => {
          const data = JSON.parse(msg.body);
          setSP([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/A', (msg) => {
          const data = JSON.parse(msg.body);
          setA([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/X', (msg) => {
          const data = JSON.parse(msg.body);
          setX([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/RH', (msg) => {
          const data = JSON.parse(msg.body);
          setRH([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/RL', (msg) => {
          const data = JSON.parse(msg.body);
          setRL([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/memory', (msg) => {
          const data = JSON.parse(msg.body);
          setMemory([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/console', (msg) => {
          const data = JSON.parse(msg.body);
          setConsoleLines(prev => [...prev, data.text]);
        });        
        // testConsole();

      },
      onStompError: (frame) => {
        console.error('Error', frame);
      }
    });

    client.current.activate();

    return () => {
      client.current.deactivate();
    };
  }, []);


  function testConsole() {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/console/test',
      });
    }
  }

  function clearConsole() {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/console/clear',
      });
      setConsoleLines([]);
    }
  }

  function storeToMemory(register) {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/store',
        body: JSON.stringify({
          selectedRegister: register,
        }),
      });
    }
  }

  function loadFromMemory(register) {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/load',
        body: JSON.stringify({
          selectedRegister: register,
        }),
      });
    }
  }

  function sendMessage() {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/hello',
        body: JSON.stringify(input)
      });
      setInput('');
    }
  }

  function updateRegister(register, value) {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/registerUpdated',
        body: JSON.stringify({
          register: register,
          newValue: value
        }),
      });
    }
  }

  function updateMemory(value) {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/memoryUpdated',
        body: JSON.stringify({
          newValue: value
        }),
      });

      client.current.publish({
        destination: '/app/memory',
        body: JSON.stringify({

        }),
      });
    }
  }

  // async function fetchMemory() {
  //   const response = await fetch("http://localhost:8080/api/memory");
  //   if (!response.ok) {
  //     throw new Error("Failed to fetch memory");
  //   }
  //   const data = await response.json();
  //   setRam(data);
  // }

  async function runProgram({
                                code,
                                inputInt,
                                runAfterLoad = true,
                                stepLimit = 1000,
                                filename = "file.txt" }) {
      try {
          const body = { filename, code, runAfterLoad };
          if (inputInt !== null) body.inputInt = inputInt;

          const res = await fetch(`http://localhost:8080/api/program/text/run?stepLimit=${stepLimit}`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(body),
          });

          if (!res.ok) {
              const text = await res.text();
              throw new Error(text || "Program run failed");
          }

          const result = await res.json();

          if (result.lastConsoleLine !== null) {
              setMessages(prev => [...prev, result.lastConsoleLine]);
          }

          return result;
      } catch (e) {
          console.error("runProgram failed with error:", e);
          throw e;
      }
  }

  async function vmRequest(url, body = null) {
      const res = await fetch(`http://localhost:8080${url}`, {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: body ? JSON.stringify(body) : null,
      });

      if (!res.ok) {
          let message = "VM error";
          try {
              const data = await res.json();
              message = data.error ?? message;
          } catch {
              message = await res.text();
          }
          throw new Error(message);
      }

      return res.json().catch(() => null);
  }


    async function vmReset() {
      await vmRequest("/api/vm/reset");
  }

  async function vmStep(inputInt = null) {
      await vmRequest("/api/vm/step", inputInt !== null ? { inputInt } : null);
  }

  async function vmBack() {
      await vmRequest("/api/vm/back");
  }

  async function vmForward() {
      await vmRequest("/api/vm/forward");
  }

    async function uploadProgramFile(file, { runAfterLoad = true, stepLimit = 1000 } = {}) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('runAfterLoad', String(runAfterLoad));
      formData.append('stepLimit', String(stepLimit));

      const res = await fetch('http://localhost:8080/api/program/file', {
        method: 'POST',
        body: formData,
      });

      if (!res.ok) {
        let message = 'Upload failed';
        try {
          const data = await res.json();
          message = data.error ?? message;
        } catch {
          message = await res.text();
        }
        throw new Error(message);
      }

      // Response could be JSON with run result or status; try to parse but tolerate empty
      try {
        const data = await res.json();
        // If backend returns code or metadata, optionally update state
        if (data?.code) setActiveCode(data.code);
        if (data?.lastConsoleLine) setMessages(prev => [...prev, data.lastConsoleLine]);
        return data;
      } catch {
        return null;
      }
    }

  return (
      <ServerContext.Provider value={{
          messages,
          input,
          setInput,
          sendMessage,

          registers: {PC: PC, SP: SP, A: A, X: X, RH: RH, RL: RL, MEM: memory},
          updateRegister,
          updateMemory,

          storeToMemory,
          loadFromMemory,

          activeCode,
          setActiveCode,

          vmReset,
          vmStep,
          vmBack,
          vmForward,
          runProgram,

          uploadProgramFile,

          consoleLines,
          clearConsole,

          ram,
      }}>
          {children}
      </ServerContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export const useServerContext = () => useContext(ServerContext);