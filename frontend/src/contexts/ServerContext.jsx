import {createContext, useContext, useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ServerContext = createContext();

export function ServerContextProvider({ children }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const client = useRef(null);

  const [PC, setPC] = useState([0, []]);
  const [SP, setSP] = useState([0, []]);
  const [A, setA] = useState([0, []]);
  const [X, setX] = useState([0, []]);
  const [RH, setRH] = useState([0, []]);
  const [RL, setRL] = useState([0, []]);

  const [memory, setMemory] = useState([0, []]);
  const [ram, setRam] = useState([]);


  useEffect(() => {
    client.current = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      onConnect: () => {
        fetchMemory();

        console.log('Connected!');
        client.current.subscribe('/topic/greetings', (msg) => {
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body.content]);
        });

        client.current.subscribe('/topic/register/PC', (msg) => {
          const data = JSON.parse(msg.body);
          setPC([data.newValue, data.cpu]);

          fetchMemory();
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

          fetchMemory();
        });
      },
      onStompError: (frame) => {
        console.log("dwdw");
        console.error('Error', frame);
      }
    });

    client.current.activate();

    return () => {
      client.current.deactivate();
    };
  }, []);

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
    }
  }

  async function fetchMemory() {
    const response = await fetch("http://localhost:8080/api/memory");
    if (!response.ok) {
      throw new Error("Failed to fetch memory");
    }
    const data = await response.json();
    setRam(data);
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

      ram,
    }}>
      {children}
    </ServerContext.Provider>
  );
}

export const useServerContext = () => useContext(ServerContext);