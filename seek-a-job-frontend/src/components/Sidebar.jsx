// import React, { useState } from "react";
// import { Search, Plus, Send, Heart, Building2, Briefcase } from "lucide-react";
// import { useAuth } from "../contexts/AuthContext.jsx";

// const Sidebar = ({ isOpen, onClose, onViewChange }) => {
//   const { user } = useAuth();
//   const [activeView, setActiveView] = useState("jobs");

//   const menuItems =
//     user?.role === "RECRUITER"
//       ? [
//           { id: "dashboard", label: "Dashboard", icon: Building2 },
//           { id: "jobs", label: "My Jobs", icon: Briefcase },
//           { id: "post-job", label: "Post New Job", icon: Plus },
//         ]
//       : [
//           { id: "jobs", label: "Browse Jobs", icon: Search },
//           { id: "applications", label: "My Applications", icon: Send },
//           { id: "saved", label: "Saved Jobs", icon: Heart },
//         ];

//   return (
//     <>
//       {isOpen && (
//         <div
//           className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
//           onClick={onClose}
//         />
//       )}
//       <aside
//         className={`
//         fixed md:static inset-y-0 left-0 z-30 w-64 bg-white border-r border-gray-200 transform transition-transform duration-300 ease-in-out
//         ${isOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0"}
//         pt-16 md:pt-0
//       `}
//       >
//         <nav className="h-full px-4 py-6 overflow-y-auto">
//           <div className="space-y-2">
//             {menuItems.map((item) => {
//               const Icon = item.icon;
//               return (
//                 <button
//                   key={item.id}
//                   onClick={() => {
//                     setActiveView(item.id);
//                     onViewChange(item.id);
//                     onClose();
//                   }}
//                   className={`
//                     w-full flex items-center px-3 py-2 text-sm font-medium rounded-md transition-colors
//                     ${
//                       activeView === item.id
//                         ? "bg-indigo-50 text-indigo-700 border-r-2 border-indigo-700"
//                         : "text-gray-700 hover:bg-gray-50"
//                     }
//                   `}
//                 >
//                   <Icon className="mr-3 h-5 w-5" />
//                   {item.label}
//                 </button>
//               );
//             })}
//           </div>
//         </nav>
//       </aside>
//     </>
//   );
// };

// export default Sidebar;
import React, { useState } from "react";
import { Search, Plus, Send, Heart, Building2, Briefcase } from "lucide-react";
import { SidebarJobFilters } from "./SidebarJobFilter";

const Sidebar = ({
  isOpen,
  onClose,
  onViewChange,
  onFiltersChange,
  userRole,
}) => {
  const [activeView, setActiveView] = useState("jobs");

  const menuItems =
    userRole === "RECRUITER"
      ? [
          { id: "dashboard", label: "Dashboard", icon: Building2 },
          { id: "jobs", label: "My Jobs", icon: Briefcase },
          { id: "post-job", label: "Post New Job", icon: Plus },
        ]
      : [
          { id: "jobs", label: "Browse Jobs", icon: Search },
          { id: "applications", label: "My Applications", icon: Send },
          { id: "saved", label: "Saved Jobs", icon: Heart },
        ];

  const showFilters = activeView === "jobs" && userRole !== "RECRUITER";

  return (
    <>
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
          onClick={onClose}
        />
      )}
      <aside
        className={`
        fixed md:static inset-y-0 left-0 z-30 w-64 bg-white border-r border-gray-200 transform transition-transform duration-300 ease-in-out
        ${isOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0"}
        pt-16 md:pt-0
      `}
      >
        <nav className="h-full px-4 py-6 overflow-y-auto">
          <div className="space-y-2">
            {menuItems.map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.id}
                  onClick={() => {
                    setActiveView(item.id);
                    onViewChange(item.id);
                    onClose();
                  }}
                  className={`
                    w-full flex items-center px-3 py-2 text-sm font-medium rounded-md transition-colors
                    ${
                      activeView === item.id
                        ? "bg-indigo-50 text-indigo-700 border-r-2 border-indigo-700"
                        : "text-gray-700 hover:bg-gray-50"
                    }
                  `}
                >
                  <Icon className="mr-3 h-5 w-5" />
                  {item.label}
                </button>
              );
            })}
          </div>

          {/* Show filters only for job seekers on the jobs page */}
          {showFilters && (
            <SidebarJobFilters onFiltersChange={onFiltersChange} />
          )}
        </nav>
      </aside>
    </>
  );
};

export default Sidebar;
