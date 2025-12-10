import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  proxy: {
    '/api': 'http://localhost:8080',
    '/ws': {
      target: 'http://localhost:8080',
      ws: true,  // важно для WebSocket
    },},

  build: {
    outDir: 'dist',  // Убедитесь, что выходная папка - dist
    emptyOutDir: true
  }
});