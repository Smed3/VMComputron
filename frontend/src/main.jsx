import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider } from "./contexts/ThemeProvider";
import './index.css'
import App from './App.jsx'
import {ServerContextProvider} from "./contexts/ServerContext.jsx";

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ThemeProvider>
      <ServerContextProvider>
        <App />
      </ServerContextProvider>
    </ThemeProvider>
  </StrictMode>,
)
