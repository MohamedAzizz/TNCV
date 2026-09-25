import type { ReactNode } from "react";

interface AuthLayoutProps {
  children: ReactNode;
}

const AuthLayout = ({
  children,
}: AuthLayoutProps) => {
  return (
    <main
      className="
        min-h-screen
        bg-[#f8faf6]
        px-4 py-8
        sm:px-6
      "
    >
      <div className="flex min-h-[calc(100vh-4rem)] items-center justify-center">
        {children}
      </div>
    </main>
  );
};

export default AuthLayout;