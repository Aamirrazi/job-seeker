import React from "react";
import { User, Mail, Link, X, Loader, Users } from "lucide-react";

// Smaller component for individual applicant details
const ApplicantCard = ({ applicant }) => (
  <div className="bg-gray-50 border border-gray-200 rounded-lg p-4 space-y-3">
    <div className="flex items-center space-x-3">
      <div className="bg-indigo-100 text-indigo-600 rounded-full p-2">
        <User size={20} />
      </div>
      <div>
        <h3 className="font-semibold text-gray-800">{applicant.name}</h3>
        <p className="text-sm text-gray-500 flex items-center">
          <Mail size={14} className="mr-1.5" />
          {applicant.userEmail}
        </p>
      </div>
    </div>
    <p className="text-sm text-gray-600 italic">"{applicant.bio}"</p>
    <div className="flex flex-wrap gap-2">
      {applicant.skills.map((skill) => (
        <span
          key={skill}
          className="px-2 py-1 bg-indigo-100 text-indigo-800 text-xs font-medium rounded-full"
        >
          {skill}
        </span>
      ))}
    </div>
    <a
      href={applicant.resumeUrl}
      target="_blank"
      rel="noopener noreferrer"
      className="inline-flex items-center text-sm font-medium text-indigo-600 hover:text-indigo-800"
    >
      <Link size={14} className="mr-1.5" />
      View Resume
    </a>
  </div>
);

const ApplicantsModal = ({ isOpen, onClose, job, applicants, isLoading }) => {
  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 bg-amber-100 bg-opacity-60 z-50 flex items-center justify-center p-4"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-xl shadow-2xl w-full max-w-2xl max-h-[90vh] flex flex-col transform transition-all"
        onClick={(e) => e.stopPropagation()} // Prevent closing modal when clicking inside
      >
        {/* Modal Header */}
        <div className="flex items-start justify-between p-5 border-b border-gray-200 rounded-t-xl">
          <div>
            <h2 className="text-xl font-bold text-gray-900">
              Applicants for "{job?.title}"
            </h2>
            <p className="text-sm text-gray-500">at {job?.companyName}</p>
          </div>
          <button
            onClick={onClose}
            className="p-2 text-gray-400 hover:bg-gray-100 rounded-full"
          >
            <X size={24} />
          </button>
        </div>

        {/* Modal Body */}
        <div className="p-6 overflow-y-auto">
          {isLoading ? (
            <div className="flex flex-col items-center justify-center py-10">
              <Loader className="animate-spin text-indigo-600" size={40} />
              <p className="mt-4 text-gray-600">Fetching Applicants...</p>
            </div>
          ) : applicants.length > 0 ? (
            <div className="space-y-4">
              {applicants.map((applicant) => (
                <ApplicantCard key={applicant.id} applicant={applicant} />
              ))}
            </div>
          ) : (
            <div className="text-center py-10">
              <Users className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-4 text-lg font-medium text-gray-900">
                No Applicants Yet
              </h3>
              <p className="mt-2 text-gray-500">
                Check back later to see who has applied for this job.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ApplicantsModal;
