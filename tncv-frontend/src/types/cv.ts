export interface Experience {
  id?: number;
  company: string;
  position: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  current: boolean;
  description?: string;
}

export interface Education {
  id?: number;
  institution: string;
  degree: string;
  fieldOfStudy?: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  current: boolean;
  description?: string;
}

export interface Skill {
  id?: number;
  name: string;
  category?: string;
  level?: string;
  description?: string;
}

export interface Language {
  id?: number;
  name: string;
  level?: string;
  certification?: string;
  description?: string;
}

export interface Project {
  id?: number;
  name: string;
  description?: string;
  technologies?: string;
  startDate?: string;
  endDate?: string;
  current: boolean;
  githubUrl?: string;
  projectUrl?: string;
}

export interface Certification {
  id?: number;
  name: string;
  issuingOrganization?: string;
  issueDate?: string;
  expirationDate?: string;
  noExpiration: boolean;
  credentialId?: string;
  credentialUrl?: string;
  description?: string;
}

export interface Interest {
  id?: number;
  name: string;
  category?: string;
  description?: string;
}

export interface Cv {
  id?: number;
  userId?: string;

  title: string;
  fullName: string;
  email: string;
  phone?: string;
  address?: string;
  linkedin?: string;
  github?: string;
  summary?: string;

  experiences?: Experience[];
  educations?: Education[];
  skills?: Skill[];
  languages?: Language[];
  projects?: Project[];
  certifications?: Certification[];
  interests?: Interest[];
}

export interface CvRequest {
  title: string;
  fullName: string;
  email: string;
  phone?: string;
  address?: string;
  linkedin?: string;
  github?: string;
  summary?: string;
}