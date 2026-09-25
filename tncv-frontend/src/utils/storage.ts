const TOKEN_KEY = "tncv_access_token";
const USER_KEY = "tncv_user";

export const storage = {
  setToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
  },

  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  removeToken() {
    localStorage.removeItem(TOKEN_KEY);
  },

  setUser(user: unknown) {
    localStorage.setItem(
      USER_KEY,
      JSON.stringify(user)
    );
  },

  getUser<T>() {
    const user = localStorage.getItem(USER_KEY);

    if (!user) {
      return null;
    }

    try {
      return JSON.parse(user) as T;
    } catch {
      return null;
    }
  },

  removeUser() {
    localStorage.removeItem(USER_KEY);
  },

  clear() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },
};