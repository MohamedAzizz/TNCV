import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { FileText, Plus, Trash2 } from "lucide-react";

import { cvService } from "../../services/cvService";
import type { Cv } from "../../types/cv";

const CvList = () => {
  const [cvs, setCvs] = useState<Cv[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadCvs = async () => {
      try {
        const data = await cvService.getMyCvs();
        setCvs(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    loadCvs();
  }, []);

  const handleDelete = async (id?: number) => {
    if (!id) return;

    const confirmed = window.confirm(
      "Voulez-vous vraiment supprimer ce CV ?"
    );

    if (!confirmed) return;

    await cvService.deleteCv(id);

    setCvs((current) =>
      current.filter((cv) => cv.id !== id)
    );
  };

  return (
    <div className="min-h-screen bg-[#f8faf6] p-6 lg:p-10">
      <div className="mx-auto max-w-6xl space-y-7">
        <div className="flex items-center gap-2 text-sm text-[#4d8225] font-semibold">
          <Link
            to="/dashboard"
            className="hover:underline flex items-center gap-1"
          >
            ← Retour à l'accueil
          </Link>
        </div>

        <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">
              Mes CV
            </h2>

            <p className="mt-1 text-sm text-slate-500">
              Gérez tous vos CV professionnels.
            </p>
          </div>

          <Link
            to="/cvs/create"
            className="inline-flex items-center justify-center gap-2 rounded-xl bg-[#79B947] px-5 py-3 text-sm font-semibold text-white shadow-xs hover:bg-[#6ba83d] transition-colors"
          >
            <Plus size={18} />
            Nouveau CV
          </Link>
        </div>

      {loading ? (
        <div className="rounded-2xl bg-white p-10 text-center text-sm text-slate-500">
          Chargement des CV...
        </div>
      ) : cvs.length === 0 ? (
        <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-12 text-center">
          <FileText
            size={40}
            className="mx-auto text-slate-300"
          />

          <h3 className="mt-4 font-semibold text-slate-900">
            Aucun CV
          </h3>

          <p className="mt-2 text-sm text-slate-500">
            Créez votre premier CV professionnel.
          </p>

          <Link
            to="/cvs/create"
            className="mt-5 inline-block rounded-xl bg-[#79B947] px-5 py-3 text-sm font-semibold text-white"
          >
            Créer un CV
          </Link>
        </div>
      ) : (
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          {cvs.map((cv) => (
            <div
              key={cv.id}
              className="rounded-2xl border border-slate-200 bg-white p-6"
            >
              <FileText
                size={28}
                className="text-[#79B947]"
              />

              <h3 className="mt-5 font-semibold text-slate-900">
                {cv.title}
              </h3>

              <p className="mt-1 text-sm text-slate-500">
                {cv.fullName}
              </p>

              <div className="mt-6 flex gap-2">
                <Link
                  to={`/cvs/${cv.id}`}
                  className="flex-1 rounded-lg bg-[#79B947]/10 px-3 py-2 text-center text-sm font-medium text-[#79B947]"
                >
                  Voir
                </Link>

                <Link
                  to={`/cvs/${cv.id}/edit`}
                  className="flex-1 rounded-lg bg-slate-100 px-3 py-2 text-center text-sm font-medium text-slate-700"
                >
                  Modifier
                </Link>

                <button
                  onClick={() => handleDelete(cv.id)}
                  className="rounded-lg bg-red-50 px-3 py-2 text-red-500"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      </div>
    </div>
  );
};

export default CvList;