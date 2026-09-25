import api from "./api";
import type { UserProfile } from "../types/user";

export const userService = {
  async getMe(): Promise<UserProfile> {
    const response = await api.get<UserProfile>("/api/users/me");

    return response.data;
  },
};