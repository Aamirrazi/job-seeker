// setupInterceptors.js
import { logout } from "../features/auth/authSlice";
import axiosInstance from "./axiosInstance";

import { toast } from "react-toastify";

export const setupInterceptors = (store) => {
  // Request Interceptor
  axiosInstance.interceptors.request.use(
    (config) => {
      const state = store.getState();
      const token = state.auth.token;
      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Response Interceptor
  axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;
      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        store.dispatch(logout());
        toast.error("Session Expired. Please log in again.");
        return Promise.reject(error);
      }
      return Promise.reject(error);
    }
  );
};
