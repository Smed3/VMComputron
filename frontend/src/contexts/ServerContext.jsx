import {createContext, useContext, useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ServerContext = createContext();

export function ServerContextProvider({ children }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const client = useRef(null);

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

  function sendMessage() {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/hello',
        body: JSON.stringify(input)
      });
      setInput('');
    }
  }

  return (
    <ServerContext.Provider value={{
      messages,
      input,
      setInput,
      sendMessage,
    }}>
      {children}
    </ServerContext.Provider>
  );
}

export const useServerContext = () => useContext(ServerContext);