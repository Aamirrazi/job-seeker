import { configureStore } from "@reduxjs/toolkit";
import jobsReducer from "../features/jobs/jobsSlice";
import authReducer from "../features/auth/authSlice"; // Import the new reducer

export const store = configureStore({
  reducer: {
    jobs: jobsReducer,
    auth: authReducer, // Add the auth slice here
  },
});
