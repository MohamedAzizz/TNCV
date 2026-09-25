export interface UserProfile {
  id?: string;
  keycloakUserId?: string;
  username?: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  profileImage?: string;
  profession?: string;
  createdAt?: string;
  updatedAt?: string;
}