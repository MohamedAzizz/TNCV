import api from "./api";
import type { Cv, CvRequest } from "../types/cv";

export const cvService = {
  async getMyCvs(): Promise<Cv[]> {
    const response = await api.get<Cv[]>("/api/cvs/me");

    return response.data;
  },

  async getCvs(): Promise<Cv[]> {
    const response = await api.get<Cv[]>("/api/cvs");

    return response.data;
  },

  async getCv(id: number): Promise<Cv> {
    const response = await api.get<Cv>(`/api/cvs/${id}`);

    return response.data;
  },

  async createCv(data: CvRequest): Promise<Cv> {
    const response = await api.post<Cv>("/api/cvs", data);

    return response.data;
  },

  async updateCv(id: number, data: CvRequest): Promise<Cv> {
    const response = await api.put<Cv>(
      `/api/cvs/${id}`,
      data
    );

    return response.data;
  },

  async deleteCv(id: number): Promise<void> {
    await api.delete(`/api/cvs/${id}`);
  },
};