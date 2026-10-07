import api from "./api";
import type {
  Cv,
  CvRequest,
  Experience,
  Education,
  Skill,
  Language,
  Project,
  Certification,
  Interest,
} from "../types/cv";

export const cvService = {
  async getMyCvs(): Promise<Cv[]> {
    const response = await api.get<Cv[]>("/api/cvs");
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
    const response = await api.put<Cv>(`/api/cvs/${id}`, data);
    return response.data;
  },

  async deleteCv(id: number): Promise<void> {
    await api.delete(`/api/cvs/${id}`);
  },

  // Sub-resources
  async addExperience(cvId: number, data: Omit<Experience, "id">): Promise<Experience> {
    const response = await api.post<Experience>(`/api/cvs/${cvId}/experiences`, data);
    return response.data;
  },

  async getExperiences(cvId: number): Promise<Experience[]> {
    const response = await api.get<Experience[]>(`/api/cvs/${cvId}/experiences`);
    return response.data;
  },

  async addEducation(cvId: number, data: Omit<Education, "id">): Promise<Education> {
    const response = await api.post<Education>(`/api/cvs/${cvId}/educations`, data);
    return response.data;
  },

  async getEducations(cvId: number): Promise<Education[]> {
    const response = await api.get<Education[]>(`/api/cvs/${cvId}/educations`);
    return response.data;
  },

  async addSkill(cvId: number, data: Omit<Skill, "id">): Promise<Skill> {
    const response = await api.post<Skill>(`/api/cvs/${cvId}/skills`, data);
    return response.data;
  },

  async getSkills(cvId: number): Promise<Skill[]> {
    const response = await api.get<Skill[]>(`/api/cvs/${cvId}/skills`);
    return response.data;
  },

  async addLanguage(cvId: number, data: Omit<Language, "id">): Promise<Language> {
    const response = await api.post<Language>(`/api/cvs/${cvId}/languages`, data);
    return response.data;
  },

  async getLanguages(cvId: number): Promise<Language[]> {
    const response = await api.get<Language[]>(`/api/cvs/${cvId}/languages`);
    return response.data;
  },

  async addProject(cvId: number, data: Omit<Project, "id">): Promise<Project> {
    const response = await api.post<Project>(`/api/cvs/${cvId}/projects`, data);
    return response.data;
  },

  async addCertification(cvId: number, data: Omit<Certification, "id">): Promise<Certification> {
    const response = await api.post<Certification>(`/api/cvs/${cvId}/certifications`, data);
    return response.data;
  },

  async addInterest(cvId: number, data: Omit<Interest, "id">): Promise<Interest> {
    const response = await api.post<Interest>(`/api/cvs/${cvId}/interests`, data);
    return response.data;
  },
};