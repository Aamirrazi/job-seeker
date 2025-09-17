import React, { useState } from "react";
import {
  Search,
  MapPin,
  DollarSign,
  Clock,
  Users,
  Eye,
  Filter,
  X,
  ChevronDown,
  ChevronUp,
} from "lucide-react";
import { Heart } from "lucide-react";

export const SidebarJobFilters = ({ onFiltersChange }) => {
  const [locationExpanded, setLocationExpanded] = useState(true);
  const [salaryExpanded, setSalaryExpanded] = useState(true);
  const [locationSearch, setLocationSearch] = useState("");
  const [selectedLocations, setSelectedLocations] = useState([]);
  const [selectedSalaryRanges, setSelectedSalaryRanges] = useState([]);

  // Dummy cities array
  const cities = [
    "San Francisco, CA",
    "New York, NY",
    "Los Angeles, CA",
    "Seattle, WA",
    "Austin, TX",
    "Boston, MA",
    "Chicago, IL",
    "Denver, CO",
    "Miami, FL",
    "Atlanta, GA",
    "Portland, OR",
    "San Diego, CA",
    "Nashville, TN",
    "Phoenix, AZ",
    "Dallas, TX",
  ];

  const salaryRanges = [
    "$40k - $60k",
    "$60k - $80k",
    "$80k - $100k",
    "$100k - $120k",
    "$120k+",
  ];

  const filteredCities = cities.filter((city) =>
    city.toLowerCase().includes(locationSearch.toLowerCase())
  );

  const handleLocationChange = (location) => {
    setSelectedLocations((prev) =>
      prev.includes(location)
        ? prev.filter((loc) => loc !== location)
        : [...prev, location]
    );
  };

  const handleSalaryChange = (salary) => {
    setSelectedSalaryRanges((prev) =>
      prev.includes(salary)
        ? prev.filter((sal) => sal !== salary)
        : [...prev, salary]
    );
  };

  const applyFilters = () => {
    onFiltersChange({
      locations: selectedLocations,
      salaryRanges: selectedSalaryRanges,
    });
  };

  const clearAllFilters = () => {
    setSelectedLocations([]);
    setSelectedSalaryRanges([]);
    setLocationSearch("");
    onFiltersChange({
      locations: [],
      salaryRanges: [],
    });
  };

  const hasActiveFilters =
    selectedLocations.length > 0 || selectedSalaryRanges.length > 0;

  return (
    <div className="border-t border-gray-200 pt-6 mt-6">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-gray-900 flex items-center">
          <Filter className="h-5 w-5 mr-2" />
          Filters
        </h3>
        {hasActiveFilters && (
          <button
            onClick={clearAllFilters}
            className="text-sm text-red-600 hover:text-red-800 flex items-center"
          >
            <X className="h-4 w-4 mr-1" />
            Clear
          </button>
        )}
      </div>

      {/* Location Filter */}
      <div className="mb-6">
        <button
          onClick={() => setLocationExpanded(!locationExpanded)}
          className="w-full flex items-center justify-between text-left text-sm font-medium text-gray-700 mb-3"
        >
          <span className="flex items-center">
            <MapPin className="h-4 w-4 mr-2" />
            Location{" "}
            {selectedLocations.length > 0 && `(${selectedLocations.length})`}
          </span>
          {locationExpanded ? (
            <ChevronUp className="h-4 w-4" />
          ) : (
            <ChevronDown className="h-4 w-4" />
          )}
        </button>

        {locationExpanded && (
          <div className="space-y-2">
            <div className="relative">
              <Search className="absolute left-3 top-2.5 h-4 w-4 text-gray-400" />
              <input
                type="text"
                placeholder="Search cities..."
                value={locationSearch}
                onChange={(e) => setLocationSearch(e.target.value)}
                className="w-full pl-9 pr-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>

            <div className="max-h-48 overflow-y-auto space-y-1">
              {filteredCities.map((city) => (
                <label
                  key={city}
                  className="flex items-center space-x-2 text-sm text-gray-600 hover:bg-gray-50 p-2 rounded cursor-pointer"
                >
                  <input
                    type="checkbox"
                    checked={selectedLocations.includes(city)}
                    onChange={() => handleLocationChange(city)}
                    className="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  />
                  <span>{city}</span>
                </label>
              ))}
              {filteredCities.length === 0 && (
                <div className="text-sm text-gray-500 p-2">No cities found</div>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Salary Range Filter */}
      <div className="mb-6">
        <button
          onClick={() => setSalaryExpanded(!salaryExpanded)}
          className="w-full flex items-center justify-between text-left text-sm font-medium text-gray-700 mb-3"
        >
          <span className="flex items-center">
            <DollarSign className="h-4 w-4 mr-2" />
            Salary Range{" "}
            {selectedSalaryRanges.length > 0 &&
              `(${selectedSalaryRanges.length})`}
          </span>
          {salaryExpanded ? (
            <ChevronUp className="h-4 w-4" />
          ) : (
            <ChevronDown className="h-4 w-4" />
          )}
        </button>

        {salaryExpanded && (
          <div className="space-y-1">
            {salaryRanges.map((salary) => (
              <label
                key={salary}
                className="flex items-center space-x-2 text-sm text-gray-600 hover:bg-gray-50 p-2 rounded cursor-pointer"
              >
                <input
                  type="checkbox"
                  checked={selectedSalaryRanges.includes(salary)}
                  onChange={() => handleSalaryChange(salary)}
                  className="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                />
                <span>{salary}</span>
              </label>
            ))}
          </div>
        )}
      </div>

      {/* Apply Filters Button */}
      {hasActiveFilters && (
        <button
          onClick={applyFilters}
          className="w-full px-4 py-2 bg-indigo-600 text-white font-medium rounded-lg hover:bg-indigo-700 transition-colors text-sm"
        >
          Apply Filters
        </button>
      )}
    </div>
  );
};
