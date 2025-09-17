import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axiosInstance";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";

let logoutTimer;

// --- UPDATED LOGIC ---
// Restore saved token and user
const savedToken = localStorage.getItem("token");
let savedUser = JSON.parse(localStorage.getItem("user")) || null;
let validToken = savedToken;
let savedRole = null; // Initialize role as null

if (savedToken) {
  try {
    const { exp } = jwtDecode(savedToken);
    if (Date.now() >= exp * 1000) {
      // Token is expired
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      validToken = null;
      savedUser = null;
    } else if (savedUser && savedUser.roles && savedUser.roles.length > 0) {
      // Token is valid, derive role from saved user
      savedRole = savedUser.roles[0].substring(5); // e.g., "ROLE_RECRUITER" -> "RECRUITER"
    }
  } catch (e) {
    console.error("Invalid token found:", e);
    validToken = null;
    savedUser = null;
  }
}

const initialState = {
  user: savedUser || null,
  role: savedRole, // Correctly set the role from localStorage on initial load
  token: validToken || null,
  isLoggedIn: !!validToken,
  status: "idle",
  error: null,
};
// --- END OF UPDATE ---

export const loginUser = createAsyncThunk(
  "/auth/login",
  async ({ email, password }, thunkAPI) => {
    try {
      const response = await axiosInstance.post("/auth/login", {
        email,
        password,
      });

      const { token } = response.data;

      try {
        const { exp } = jwtDecode(token);
        const expiryTime = exp * 1000 - Date.now();

        if (expiryTime > 0) {
          if (logoutTimer) clearTimeout(logoutTimer);
          logoutTimer = setTimeout(() => {
            thunkAPI.dispatch(logout());
          }, expiryTime);
        } else {
          thunkAPI.dispatch(logout());
        }
      } catch (err) {
        console.error("Failed to decode token:", err);
        thunkAPI.dispatch(logout());
      }
      toast.success("Login successful");
      return response.data;
    } catch (err) {
      return thunkAPI.rejectWithValue(err.response?.data || err.message);
    }
  }
);

export const aboutUser = createAsyncThunk(
  "/users/me/profile",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get("/users/me/profile");
      return response.data;
    } catch (err) {
      toast.error(err.message || "Failed to get user profile");
      return rejectWithValue(err.response?.data || err.message);
    }
  }
);

export const registerUser = createAsyncThunk(
  "/auth/registerUser",
  async (userData, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post("/auth/register", userData);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response?.data || err.message);
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.role = null;
      state.isLoggedIn = false;
      state.status = "idle";
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      if (logoutTimer) clearTimeout(logoutTimer);
      toast.info("You have been logged out.");
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.user = action.payload.user;
        state.role = action.payload.user.roles[0].substring(5);
        state.token = action.payload.token;
        state.isLoggedIn = true;
        localStorage.setItem("token", action.payload.token);
        localStorage.setItem("user", JSON.stringify(action.payload.user));
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(aboutUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(aboutUser.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.user = action.payload;
      })
      .addCase(aboutUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(registerUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(registerUser.fulfilled, (state) => {
        state.status = "succeeded";
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
