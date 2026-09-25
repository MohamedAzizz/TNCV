import {
  createContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

import { authService } from "../services/authService";
import { userService } from "../services/userService";
import { storage } from "../utils/storage";

import type {
  AuthUser,
  LoginRequest,
  RegisterRequest,
} from "../types/auth";

interface AuthContextType {
  user: AuthUser | null;
  token: string | null;
  loading: boolean;
  isAuthenticated: boolean;

  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
  user: null,
  token: null,
  loading: true,
  isAuthenticated: false,
  login: async () => {},
  register: async () => {},
  logout: () => {},
});

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({
  children,
}: AuthProviderProps) => {
  const [user, setUser] = useState<AuthUser | null>(
    storage.getUser()
  );

  const [token, setToken] = useState<string | null>(
    storage.getToken()
  );

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const restoreSession = async () => {
      const savedToken = storage.getToken();

      if (!savedToken) {
        setLoading(false);
        return;
      }

      try {
        const profile = await userService.getMe();

        const authUser: AuthUser = {
          id: profile.keycloakUserId,
          username: profile.username,
          email: profile.email,
          firstName: profile.firstName,
          lastName: profile.lastName,
        };

        setUser(authUser);
        storage.setUser(authUser);
      } catch {
        storage.clear();
        setUser(null);
        setToken(null);
      } finally {
        setLoading(false);
      }
    };

    restoreSession();
  }, []);

  const login = async (data: LoginRequest) => {
    const response = await authService.login(data);

    storage.setToken(response.accessToken);
    setToken(response.accessToken);

    const profile = await userService.getMe();

    const authUser: AuthUser = {
      id: profile.keycloakUserId,
      username: profile.username,
      email: profile.email,
      firstName: profile.firstName,
      lastName: profile.lastName,
    };

    storage.setUser(authUser);
    setUser(authUser);
  };

  const register = async (data: RegisterRequest) => {
    await authService.register(data);

    // Après inscription, l'utilisateur pourra se connecter.
  };

  const logout = () => {
    storage.clear();
    setUser(null);
    setToken(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        loading,
        isAuthenticated: !!token,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};