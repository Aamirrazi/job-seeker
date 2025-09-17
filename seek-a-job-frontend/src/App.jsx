import React, { useState } from "react";
import Dashboard from "./components/Dashboard";
import AuthForm from "./components/AuthForm";

import { useSelector } from "react-redux";
// import { useAuth } from "./context/AuthContext";

const App = () => {
  // const { user, loading } = useAuth();
  const { user, status } = useSelector((state) => state.auth);
  const [authMode, setAuthMode] = useState("login");

  if (status === "loading") {
    return (
      <div className="min-h-screen min-w-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600"></div>
      </div>
    );
  }
  console.log("32", user);

  if (!user) {
    return <AuthForm mode={authMode} onSwitchMode={setAuthMode} />;
  }

  return <Dashboard />;
};

export default App;
