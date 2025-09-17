/* eslint-disable no-unused-vars */
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./index.css";
import { Provider } from "react-redux";
import { store } from "./store/store.js";
import { setupInterceptors } from "./api/setupInterceptor.js";
import { ToastContainer } from "react-toastify";
// import { setupInterceptors } from "./api/setupInterceptor";

// import { AuthProvider } from "./contexts/AuthContext.jsx";
setupInterceptors(store);
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ToastContainer position="top-center" />
    <Provider store={store}>
      <App />
    </Provider>
    {/* <AuthProvider>
      <App />
    </AuthProvider> */}
  </React.StrictMode>
);
