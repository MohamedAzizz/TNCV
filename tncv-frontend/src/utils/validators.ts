export const isValidEmail = (
  email: string
): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

export const isValidPassword = (
  password: string
): boolean => {
  return password.length >= 8;
};

export const isRequired = (
  value: string
): boolean => {
  return value.trim().length > 0;
};