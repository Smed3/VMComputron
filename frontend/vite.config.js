import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  proxy: {
    '/api': 'http://localhost:8080',
    '/ws': {
      target: 'http://localhost:8080',
      ws: true,
    },},

  build: {
    outDir: 'dist',
    emptyOutDir: true
  }
});