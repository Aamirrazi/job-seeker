import React, { useEffect, useState } from "react";
import { Search, MapPin, Clock, Eye, Heart, User, Users } from "lucide-react";
import { timeAgo } from "../features/utils/time";
import { useDispatch, useSelector } from "react-redux";
import { getJobs, postJobs } from "../features/jobs/jobsSlice";
import { toast } from "react-toastify";

export const JobCard = ({
  job,
  isApplied = false,
  handleApply,
  isRecruiter = false,
  handleViewApplicants, // New prop
}) => {
  const [applied, setApplied] = useState(isApplied);
  const [saved, setSaved] = useState(false);
  const { appliedJob } = useSelector((state) => state.jobs);

  useEffect(() => {
    setApplied(appliedJob.some((j) => j.id === job.id));
  }, [appliedJob, job.id]);

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-lg transition-shadow duration-300">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900">{job.title}</h3>
          <p className="text-gray-600">{job.companyName}</p>
          <div className="flex flex-wrap items-center gap-x-4 gap-y-2 mt-2 text-sm text-gray-500">
            <div className="flex items-center">
              <MapPin className="h-4 w-4 mr-1" />
              {job?.location}
            </div>
            <div className="flex items-center">
              <Clock className="h-4 w-4 mr-1" />
              {job?.jobType}
            </div>
            <div className="flex items-center font-medium">₹ {job?.salary}</div>
          </div>
        </div>
        {!isRecruiter && (
          <button
            onClick={() => setSaved(!saved)}
            className={`p-2 rounded-full ${
              saved
                ? "text-red-500 bg-red-50"
                : "text-gray-400 hover:text-red-500 hover:bg-red-50"
            }`}
          >
            <Heart className={`h-5 w-5 ${saved ? "fill-current" : ""}`} />
          </button>
        )}
      </div>
      <p className="mt-4 text-gray-600 text-sm line-clamp-2">
        {job?.description}
      </p>
      <div className="mt-6 flex items-center justify-between">
        <div className="flex items-center text-sm text-gray-500">
          Posted {timeAgo(job?.postedDate)}
        </div>
        {isRecruiter ? (
          <div className="flex space-x-2">
            <button
              onClick={() => handleViewApplicants(job)} // Attach new handler
              className="px-4 py-2 text-sm font-medium text-indigo-700 bg-indigo-100 rounded-md hover:bg-indigo-200 transition-colors"
            >
              <Users className="h-4 w-4 mr-1.5 inline" />
              View Applicants
            </button>
          </div>
        ) : (
          <div className="flex space-x-2">
            <button
              onClick={() => handleApply(job.id)}
              disabled={applied}
              className={`px-4 py-2 text-sm font-medium rounded-md transition-all duration-300 ${
                applied
                  ? "bg-green-100 text-green-800 cursor-not-allowed"
                  : "text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transform hover:-translate-y-0.5"
              }`}
            >
              {applied ? "Applied" : "Apply Now"}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export const PostJobForm = () => {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    location: "",
    salary: "",
    jobType: "",
  });
  const dispatch = useDispatch();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Use .unwrap() to await the thunk completion and handle errors
      await dispatch(postJobs(formData)).unwrap();
      toast.success("Job posted successfully");

      // Refetch the jobs list only after the post is successful
      dispatch(getJobs());

      // Reset form
      setFormData({
        title: "",
        description: "",
        location: "",
        salary: "",
        jobType: "",
      });
    } catch (err) {
      toast.error(err.message || "Failed to post job. Please try again.");
    }
  };

  return (
    <div className="max-w-3xl mx-auto p-4 sm:p-6 lg:p-8">
      <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
        <h2 className="text-2xl font-bold text-gray-900 mb-6">
          Post a New Job
        </h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* ... form fields remain the same ... */}
        </form>
      </div>
    </div>
  );
};
