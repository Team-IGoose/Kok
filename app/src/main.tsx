import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import "./main.css";
import "./common/translation/index.ts";

createRoot(document.getElementById("root")!).render(<App />);
