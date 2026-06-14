import { defineConfig } from 'vite';

export default defineConfig({
  root: 'web',
  build: {
    outDir: '../dist',
  },
  server: {
    port: 3000,
  }
});
