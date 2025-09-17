import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axiosInstance";
import { toast } from "react-toastify";

const initialState = {
  jobs: [],
  appliedJob: [],
  applicants: [], // New state to hold applicants for a job
  loading: false,
  applicantsLoading: false, // Separate loading state for applicants
  error: null,
};

export const getJobs = createAsyncThunk(
  "/jobs",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get("/jobs");
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);

export const postJobs = createAsyncThunk(
  "/post/jobs",
  async (jobData, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post("/jobs", jobData);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);

export const applyJobs = createAsyncThunk(
  "/jobs/apply",
  async (jobId, { rejectWithValue }) => {
    try {
      await axiosInstance.post(`/jobs/apply/${jobId}`);
      toast.success("Applied successfully!");
      return true;
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to apply");
      return rejectWithValue(err.response.data);
    }
  }
);

export const getAppliedJobs = createAsyncThunk(
  "/users/me/applications",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/users/me/applications`);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);

// --- NEW THUNK ---
export const getApplicantsForJob = createAsyncThunk(
  "/jobs/applicants",
  async (jobId, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/jobs/applicants/${jobId}`);
      return response.data;
    } catch (err) {
      toast.error("Failed to fetch applicants.");
      return rejectWithValue(err.response.data);
    }
  }
);
// --- END OF NEW THUNK ---

export const jobsSlice = createSlice({
  name: "jobs",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      // getJobs cases
      .addCase(getJobs.pending, (state) => {
        state.loading = true;
      })
      .addCase(getJobs.fulfilled, (state, action) => {
        state.loading = false;
        state.jobs = action.payload.content;
      })
      .addCase(getJobs.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // postJobs cases
      .addCase(postJobs.pending, (state) => {
        state.loading = true;
      })
      .addCase(postJobs.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(postJobs.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // getAppliedJobs cases
      .addCase(getAppliedJobs.pending, (state) => {
        state.loading = true;
      })
      .addCase(getAppliedJobs.fulfilled, (state, action) => {
        state.loading = false;
        state.appliedJob = action.payload;
      })
      .addCase(getAppliedJobs.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // applyJobs cases
      .addCase(applyJobs.pending, (state) => {
        state.loading = true;
      })
      .addCase(applyJobs.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(applyJobs.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // --- NEW REDUCERS for getApplicantsForJob ---
      .addCase(getApplicantsForJob.pending, (state) => {
        state.applicantsLoading = true;
        state.applicants = [];
      })
      .addCase(getApplicantsForJob.fulfilled, (state, action) => {
        state.applicantsLoading = false;
        state.applicants = action.payload;
      })
      .addCase(getApplicantsForJob.rejected, (state) => {
        state.applicantsLoading = false;
        state.applicants = [];
      });
  },
});

export default jobsSlice.reducer;
