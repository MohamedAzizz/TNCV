import type { ReactNode } from "react";
import Logo from "../common/Logo";

interface AuthLayoutProps {
  children: ReactNode;
}

const AuthLayout = ({ children }: AuthLayoutProps) => {
  return (
    <div
      className="
        min-h-screen
        flex
        flex-col
        items-center
        justify-center
        bg-[#f8faf6]
        px-4
        py-8
      "
    >
      <div className="mb-6">
        <Logo size="lg" />
      </div>

      <main className="w-full max-w-md">
        {children}
      </main>

      <p className="mt-8 text-center text-xs text-slate-400">
        © {new Date().getFullYear()} TNCV — Tous droits réservés.
      </p>
    </div>
  );
};

export default AuthLayout;