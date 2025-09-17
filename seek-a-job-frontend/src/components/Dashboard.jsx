import React, { useState, useMemo, useEffect } from "react";
import { JobCard, PostJobForm } from "./JobComponents";
import ApplicantsModal from "./ApplicantsModal"; // Import the new modal
import { Send, Heart, Search } from "lucide-react";
import Header from "./Header";
import { useDispatch, useSelector } from "react-redux";
import {
  applyJobs,
  getAppliedJobs,
  getJobs,
  getApplicantsForJob, // Import the new thunk
} from "../features/jobs/jobsSlice";

const Dashboard = () => {
  const dispatch = useDispatch();
  const { role } = useSelector((state) => state.auth);
  const {
    jobs: Jobs,
    appliedJob,
    applicants, // Get new state from Redux
    applicantsLoading, // Get new loading state
  } = useSelector((state) => state.jobs);

  const [currentView, setCurrentView] = useState("jobs");
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [filters, setFilters] = useState({
    locations: [],
    salaryRanges: [],
  });

  // --- NEW MODAL STATE ---
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedJob, setSelectedJob] = useState(null);

  useEffect(() => {
    // Fetch initial data only after the role has been determined from the auth state
    if (role) {
      dispatch(getJobs());
      if (role !== "RECRUITER") {
        dispatch(getAppliedJobs());
      }
    }
  }, [dispatch, role]);

  const filteredJobs = useMemo(() => {
    let filtered = Jobs;

    // Filter by search query
    if (searchQuery.trim()) {
      const lowercasedQuery = searchQuery.toLowerCase();
      filtered = filtered.filter(
        (job) =>
          job.title.toLowerCase().includes(lowercasedQuery) ||
          job.companyName.toLowerCase().includes(lowercasedQuery) ||
          job.description.toLowerCase().includes(lowercasedQuery)
      );
    }

    // Filter by location
    if (filters.locations.length > 0) {
      filtered = filtered.filter((job) =>
        filters.locations.includes(job.location)
      );
    }

    // Filter by salary range
    if (filters.salaryRanges.length > 0) {
      filtered = filtered.filter((job) =>
        filters.salaryRanges.includes(job.salary)
      );
    }

    return filtered;
  }, [searchQuery, filters, Jobs]);

  // --- UPDATED HANDLERS ---
  const handleApply = async (jobId) => {
    try {
      await dispatch(applyJobs(jobId)).unwrap();
      // Refetch applied jobs to update the UI correctly after a successful application
      dispatch(getAppliedJobs());
    } catch (error) {
      // Toast notifications are handled in the slice for better separation of concerns
      console.error("Failed to apply for job:", error);
    }
  };

  const handleViewApplicants = (job) => {
    setSelectedJob(job);
    setIsModalOpen(true);
    dispatch(getApplicantsForJob(job.id));
  };
  // --- END OF UPDATED HANDLERS ---

  const renderContent = () => {
    const appliedJobs = appliedJob || [];
    const isRecruiter = role === "RECRUITER";

    switch (currentView) {
      case "post-job":
        return <PostJobForm />;
      case "applications":
        return (
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <h1 className="text-2xl font-bold text-gray-900 mb-6">
              My Applications
            </h1>
            {appliedJobs.length > 0 ? (
              <div className="grid gap-6">
                {appliedJobs.map((job) => (
                  <JobCard
                    key={job.id}
                    job={job}
                    isApplied={true}
                    handleApply={handleApply}
                    isRecruiter={isRecruiter}
                  />
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <Send className="mx-auto h-12 w-12 text-gray-400" />
                <h3 className="mt-4 text-lg font-medium text-gray-900">
                  No applications yet
                </h3>
                <p className="mt-2 text-gray-500">
                  Start applying to jobs to see your applications here.
                </p>
              </div>
            )}
          </div>
        );
      case "saved":
        return (
          <div className="text-center py-12">
            <Heart className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-4 text-lg font-medium text-gray-900">
              No saved jobs
            </h3>
            <p className="mt-2 text-gray-500">
              Save jobs you're interested in to view them here later.
            </p>
          </div>
        );
      default:
        return (
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-6 flex items-center justify-between">
              <h1 className="text-2xl font-bold text-gray-900">
                {isRecruiter ? "Posted Jobs" : "Available Jobs"}
              </h1>
              <div className="text-sm text-gray-500">
                {filteredJobs.length} jobs found
              </div>
            </div>
            <div className="grid gap-6">
              {filteredJobs.length > 0 ? (
                filteredJobs.map((job) => (
                  <JobCard
                    key={job.id}
                    job={job}
                    isRecruiter={isRecruiter}
                    handleApply={handleApply}
                    handleViewApplicants={handleViewApplicants} // Pass the handler
                  />
                ))
              ) : (
                <div className="text-center py-12">
                  <Search className="mx-auto h-12 w-12 text-gray-400" />
                  <h3 className="mt-4 text-lg font-medium text-gray-900">
                    No jobs found
                  </h3>
                  <p className="mt-2 text-gray-500">
                    Try adjusting your search criteria or filters.
                  </p>
                </div>
              )}
            </div>
          </div>
        );
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header
        onMenuToggle={() => setShowMobileMenu(!showMobileMenu)}
        showMobileMenu={showMobileMenu}
      />

      {/* --- NEW MODAL RENDER --- */}
      <ApplicantsModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        job={selectedJob}
        applicants={applicants}
        isLoading={applicantsLoading}
      />

      <div className="flex">
        {/* Sidebar */}
        <>
          {showMobileMenu && (
            <div
              className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
              onClick={() => setShowMobileMenu(false)}
            />
          )}
          <aside
            className={`
              fixed md:static inset-y-0 left-0 z-30 w-64 bg-white border-r border-gray-200 transform transition-transform duration-300 ease-in-out
              ${
                showMobileMenu
                  ? "translate-x-0"
                  : "-translate-x-full md:translate-x-0"
              }
              pt-0
            `}
          >
            <nav className="h-full px-4 py-6 overflow-y-auto">
              {/* Navigation items based on role */}
              <div className="space-y-2">
                {(role === "RECRUITER"
                  ? [
                      { id: "jobs", label: "My Jobs", icon: "💼" },
                      { id: "post-job", label: "Post New Job", icon: "➕" },
                    ]
                  : [
                      { id: "jobs", label: "Browse Jobs", icon: "🔍" },
                      {
                        id: "applications",
                        label: "My Applications",
                        icon: "📤",
                      },
                      { id: "saved", label: "Saved Jobs", icon: "❤️" },
                    ]
                ).map((item) => (
                  <button
                    key={item.id}
                    onClick={() => {
                      setCurrentView(item.id);
                      setShowMobileMenu(false);
                    }}
                    className={`
                      w-full flex items-center px-3 py-2 text-sm font-medium rounded-md transition-colors
                      ${
                        currentView === item.id
                          ? "text-white bg-gradient-to-r from-indigo-600 to-purple-600 shadow-md"
                          : "text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                      }
                    `}
                  >
                    <span className="mr-3 text-base">{item.icon}</span>
                    {item.label}
                  </button>
                ))}
              </div>

              {/* Filters for Job Seeker */}
              {currentView === "jobs" && role !== "RECRUITER" && (
                <div className="border-t border-gray-200 pt-6 mt-6">
                  {/* Filter implementation remains the same */}
                </div>
              )}
            </nav>
          </aside>
        </>
        <main className="flex-1">{renderContent()}</main>
      </div>
    </div>
  );
};

export default Dashboard;
