import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Source - https://stackoverflow.com/a/78033472
  // Posted by Kavishwa Wendakoon, modified by community. See post 'Timeline' for change history
  // Retrieved 2026-04-13, License - CC BY-SA 4.0

  server: {
    // host: "0.0.0.0",
    proxy: {
      "/api" : "http://172.26.144.1:8081"
      // "/api" : "http://localhost:8081"
    },
  },
})
