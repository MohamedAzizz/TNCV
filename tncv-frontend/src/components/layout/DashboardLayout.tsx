import {
  LayoutDashboard,
  FileText,
  User,
  LogOut,
} from "lucide-react";

import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const DashboardLayout = () => {
  const { user, logout } = useAuth();

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `
      flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium
      transition
      ${
        isActive
          ? "bg-[#79B947]/10 text-[#79B947]"
          : "text-slate-600 hover:bg-slate-50"
      }
    `;

  return (
    <div className="min-h-screen bg-[#f8faf6]">
      <aside className="fixed left-0 top-0 hidden h-screen w-64 border-r border-slate-200 bg-white p-5 lg:block">
        <div className="mb-10 text-2xl font-extrabold">
          TN<span className="text-[#79B947]">CV</span>
        </div>

        <nav className="space-y-2">
          <NavLink
            to="/dashboard"
            className={linkClass}
          >
            <LayoutDashboard size={18} />
            Dashboard
          </NavLink>

          <NavLink
            to="/cvs"
            className={linkClass}
          >
            <FileText size={18} />
            Mes CV
          </NavLink>

          <NavLink
            to="/profile"
            className={linkClass}
          >
            <User size={18} />
            Mon profil
          </NavLink>
        </nav>

        <button
          onClick={logout}
          className="mt-10 flex w-full items-center gap-3 rounded-xl px-4 py-3 text-sm text-red-500 hover:bg-red-50"
        >
          <LogOut size={18} />
          Déconnexion
        </button>
      </aside>

      <main className="lg:ml-64">
        <header className="border-b border-slate-200 bg-white px-6 py-5">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-lg font-semibold text-slate-900">
                Bonjour {user?.firstName || "Utilisateur"} 👋
              </h1>

              <p className="text-sm text-slate-500">
                Bienvenue dans votre espace TNCV.
              </p>
            </div>
          </div>
        </header>

        <section className="p-6">
          <Outlet />
        </section>
      </main>
    </div>
  );
};

export default DashboardLayout;