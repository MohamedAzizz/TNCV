import { useState } from "react";
import { useNavigate } from "react-router-dom";

import Input from "../../components/common/Input";
import Button from "../../components/common/Button";
import { cvService } from "../../services/cvService";

const CvCreate = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    fullName: "",
    email: "",
    phone: "",
    address: "",
    linkedin: "",
    github: "",
    summary: "",
  });

  const [loading, setLoading] = useState(false);

  const updateField = (
    field: keyof typeof form,
    value: string
  ) => {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));
  };

  const handleSubmit = async (
    event: React.FormEvent
  ) => {
    event.preventDefault();

    setLoading(true);

    try {
      const cv = await cvService.createCv(form);

      navigate(`/cvs/${cv.id}`);
    } catch (error) {
      console.error(error);
      alert("Erreur lors de la création du CV.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-4xl">
      <div className="mb-7">
        <h2 className="text-2xl font-bold text-slate-900">
          Créer un CV
        </h2>

        <p className="mt-1 text-sm text-slate-500">
          Commencez par vos informations principales.
        </p>
      </div>

      <form
        onSubmit={handleSubmit}
        className="space-y-6"
      >
        <div className="rounded-2xl border border-slate-200 bg-white p-6">
          <h3 className="mb-5 text-lg font-semibold">
            Informations générales
          </h3>

          <div className="grid gap-5 md:grid-cols-2">
            <Input
              label="Nom du CV"
              placeholder="CV Développeur Full Stack"
              value={form.title}
              onChange={(e) =>
                updateField("title", e.target.value)
              }
              required
            />

            <Input
              label="Nom complet"
              placeholder="Mohamed Aziz Cherni"
              value={form.fullName}
              onChange={(e) =>
                updateField(
                  "fullName",
                  e.target.value
                )
              }
              required
            />

            <Input
              label="Email"
              type="email"
              value={form.email}
              onChange={(e) =>
                updateField("email", e.target.value)
              }
              required
            />

            <Input
              label="Téléphone"
              value={form.phone}
              onChange={(e) =>
                updateField("phone", e.target.value)
              }
            />

            <Input
              label="Adresse"
              value={form.address}
              onChange={(e) =>
                updateField(
                  "address",
                  e.target.value
                )
              }
            />

            <Input
              label="LinkedIn"
              value={form.linkedin}
              onChange={(e) =>
                updateField(
                  "linkedin",
                  e.target.value
                )
              }
            />

            <Input
              label="GitHub"
              value={form.github}
              onChange={(e) =>
                updateField("github", e.target.value)
              }
            />
          </div>

          <div className="mt-5">
            <label className="mb-2 block text-sm font-medium text-slate-700">
              Résumé professionnel
            </label>

            <textarea
              value={form.summary}
              onChange={(e) =>
                updateField(
                  "summary",
                  e.target.value
                )
              }
              rows={6}
              placeholder="Présentez votre profil professionnel..."
              className="w-full resize-none rounded-xl border border-slate-200 px-4 py-3 text-sm outline-none focus:border-[#79B947] focus:ring-2 focus:ring-[#79B947]/20"
            />
          </div>
        </div>

        <div className="flex justify-end">
          <div className="w-full sm:w-56">
            <Button
              type="submit"
              loading={loading}
            >
              Créer le CV
            </Button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default CvCreate;