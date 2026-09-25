import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import PublicRoute from "./PublicRoute";
import ProtectedRoute from "./ProtectedRoute";

import SignIn from "../pages/auth/SignIn";
import SignUp from "../pages/auth/SignUp";
import ForgotPassword from "../pages/auth/ForgotPassword";
import ResetPassword from "../pages/auth/ResetPassword";
import VerifyCode from "../pages/auth/VerifyCode";

import DashboardLayout from "../components/layout/DashboardLayout";

import Dashboard from "../pages/dashboard/Dashboard";

import CvList from "../pages/cv/CvList";
import CvCreate from "../pages/cv/CvCreate";
import CvEdit from "../pages/cv/CvEdit";
import CvDetails from "../pages/cv/CvDetails";

const AppRoutes = () => {
  return (
    <Routes>
      {/* ============================= */}
      {/* PUBLIC ROUTES */}
      {/* ============================= */}

      <Route element={<PublicRoute />}>
        <Route
          path="/signin"
          element={<SignIn />}
        />

        <Route
          path="/signup"
          element={<SignUp />}
        />

        <Route
          path="/forgot-password"
          element={<ForgotPassword />}
        />

        <Route
          path="/reset-password"
          element={<ResetPassword />}
        />

        <Route
          path="/verify-code"
          element={<VerifyCode />}
        />
      </Route>

      {/* ============================= */}
      {/* PROTECTED ROUTES */}
      {/* ============================= */}

      <Route element={<ProtectedRoute />}>
        <Route
          element={<DashboardLayout />}
        >
          <Route
            path="/dashboard"
            element={<Dashboard />}
          />

          <Route
            path="/cvs"
            element={<CvList />}
          />

          <Route
            path="/cvs/create"
            element={<CvCreate />}
          />

          <Route
            path="/cvs/:id"
            element={<CvDetails />}
          />

          <Route
            path="/cvs/:id/edit"
            element={<CvEdit />}
          />
        </Route>
      </Route>

      {/* ============================= */}
      {/* DEFAULT */}
      {/* ============================= */}

      <Route
        path="/"
        element={
          <Navigate
            to="/signin"
            replace
          />
        }
      />

      <Route
        path="*"
        element={
          <Navigate
            to="/signin"
            replace
          />
        }
      />
    </Routes>
  );
};

export default AppRoutes;