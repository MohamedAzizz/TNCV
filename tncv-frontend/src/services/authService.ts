import api from "./api";
import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from "../types/auth";

const authService = {
  async login(
    data: LoginRequest
  ): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>(
      "/api/auth/login",
      data
    );

    return response.data;
  },

  async register(
    data: RegisterRequest
  ): Promise<AuthResponse | unknown> {
    const response = await api.post(
      "/api/auth/register",
      data
    );

    return response.data;
  },
};

export default authService;