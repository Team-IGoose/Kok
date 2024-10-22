import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@domain": path.resolve(__dirname, "src/_domain"), // path.resolve 사용
      "@assets": path.resolve(__dirname, "src/common/assets"), // path.resolve 사용
      "@constant": path.resolve(__dirname, "src/common/constant"), // path.resolve 사용
    },
  },
});
