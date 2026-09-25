import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { cvService } from "../../services/cvService";
import type { Cv } from "../../types/cv";

const CvDetails = () => {
  const { id } = useParams();

  const [cv, setCv] = useState<Cv | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadCv = async () => {
      if (!id) return;

      try {
        const data = await cvService.getCv(
          Number(id)
        );

        setCv(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    loadCv();
  }, [id]);

  if (loading) {
    return <p>Chargement...</p>;
  }

  if (!cv) {
    return (
      <div className="rounded-2xl bg-white p-8">
        CV introuvable.
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl">
      <div className="mb-6 flex justify-between">
        <div>
          <h2 className="text-2xl font-bold">
            {cv.title}
          </h2>

          <p className="mt-1 text-slate-500">
            {cv.fullName}
          </p>
        </div>

        <Link
          to={`/cvs/${cv.id}/edit`}
          className="rounded-xl bg-[#79B947] px-5 py-3 text-sm font-semibold text-white"
        >
          Modifier
        </Link>
      </div>

      <div className="space-y-5">
        <section className="rounded-2xl bg-white p-6">
          <h3 className="font-semibold">
            Informations
          </h3>

          <div className="mt-4 space-y-2 text-sm text-slate-600">
            <p>Email : {cv.email}</p>
            <p>Téléphone : {cv.phone || "-"}</p>
            <p>Adresse : {cv.address || "-"}</p>
            <p>LinkedIn : {cv.linkedin || "-"}</p>
            <p>GitHub : {cv.github || "-"}</p>
          </div>
        </section>

        <section className="rounded-2xl bg-white p-6">
          <h3 className="font-semibold">
            Résumé professionnel
          </h3>

          <p className="mt-4 whitespace-pre-line text-sm leading-7 text-slate-600">
            {cv.summary || "Aucun résumé."}
          </p>
        </section>
      </div>
    </div>
  );
};

export default CvDetails;