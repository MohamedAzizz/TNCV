import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

import authService from "../services/authService";
import api from "../services/api";
import { storage } from "../utils/storage";

import type {
  AuthUser,
  LoginRequest,
  RegisterRequest,
} from "../types/auth";

interface AuthContextType {
  user: AuthUser | null;
  loading: boolean;
  isAuthenticated: boolean;

  login: (
    data: LoginRequest
  ) => Promise<void>;

  register: (
    data: RegisterRequest
  ) => Promise<void>;

  logout: () => void;
}

export const AuthContext =
  createContext<AuthContextType | undefined>(
    undefined
  );

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({
  children,
}: AuthProviderProps) => {
  const [user, setUser] =
    useState<AuthUser | null>(
      storage.getUser<AuthUser>()
    );

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {
    const restoreSession = async () => {
      const token = storage.getToken();

      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const response =
          await api.get<AuthUser>(
            "/api/users/me"
          );

        setUser(response.data);
        storage.setUser(response.data);
      } catch {
        storage.clear();
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    restoreSession();
  }, []);

  const login = async (
    data: LoginRequest
  ) => {
    const response =
      await authService.login(data);

    storage.setToken(
      response.accessToken
    );

    const userResponse =
      await api.get<AuthUser>(
        "/api/users/me"
      );

    setUser(userResponse.data);
    storage.setUser(userResponse.data);
  };

  const register = async (
    data: RegisterRequest
  ) => {
    await authService.register(data);
  };

  const logout = () => {
    storage.clear();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        isAuthenticated: !!user,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuth doit être utilisé dans AuthProvider"
    );
  }

  return context;
};