import {
  FileText,
  Plus,
  BarChart3,
  Briefcase,
} from "lucide-react";

import { Link } from "react-router-dom";

const Dashboard = () => {
  const stats = [
    {
      title: "Mes CV",
      value: "0",
      icon: FileText,
    },
    {
      title: "Analyses ATS",
      value: "0",
      icon: BarChart3,
    },
    {
      title: "Candidatures",
      value: "0",
      icon: Briefcase,
    },
  ];

  return (
    <div className="space-y-8">
      <div className="flex flex-col justify-between gap-4 md:flex-row md:items-center">
        <div>
          <h2 className="text-2xl font-bold text-slate-900">
            Dashboard
          </h2>

          <p className="mt-1 text-sm text-slate-500">
            Gérez votre parcours professionnel depuis TNCV.
          </p>
        </div>

        <Link
          to="/cvs/create"
          className="inline-flex items-center justify-center gap-2 rounded-xl bg-[#79B947] px-5 py-3 text-sm font-semibold text-white hover:opacity-90"
        >
          <Plus size={18} />
          Créer un CV
        </Link>
      </div>

      <div className="grid gap-5 md:grid-cols-3">
        {stats.map((stat) => {
          const Icon = stat.icon;

          return (
            <div
              key={stat.title}
              className="rounded-2xl border border-slate-200 bg-white p-6"
            >
              <Icon
                size={24}
                className="text-[#79B947]"
              />

              <p className="mt-5 text-sm text-slate-500">
                {stat.title}
              </p>

              <p className="mt-1 text-3xl font-bold text-slate-900">
                {stat.value}
              </p>
            </div>
          );
        })}
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-8">
        <h3 className="text-lg font-semibold text-slate-900">
          Commencez votre premier CV
        </h3>

        <p className="mt-2 max-w-xl text-sm text-slate-500">
          Créez votre CV professionnel puis utilisez les
          prochaines fonctionnalités TNCV pour l'analyser
          et l'améliorer.
        </p>

        <Link
          to="/cvs/create"
          className="mt-5 inline-flex rounded-xl border border-[#79B947] px-5 py-3 text-sm font-semibold text-[#79B947] hover:bg-[#79B947]/5"
        >
          Créer mon premier CV
        </Link>
      </div>
    </div>
  );
};

export default Dashboard;