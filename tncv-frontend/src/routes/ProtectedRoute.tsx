import {
  Navigate,
  Outlet,
} from "react-router-dom";

import { useAuth } from "../context/AuthContext";

const ProtectedRoute = () => {
  const {
    isAuthenticated,
    loading,
  } = useAuth();

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-[#f8faf6]">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-[#79B947]" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <Navigate
        to="/signin"
        replace
      />
    );
  }

  return <Outlet />;
};

export default ProtectedRoute;