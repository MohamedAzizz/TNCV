import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import Input from "../../components/common/Input";
import Button from "../../components/common/Button";
import { cvService } from "../../services/cvService";
import type { CvRequest } from "../../types/cv";

const CvEdit = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState<CvRequest>({
    title: "",
    fullName: "",
    email: "",
    phone: "",
    address: "",
    linkedin: "",
    github: "",
    summary: "",
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const loadCv = async () => {
      if (!id) return;

      try {
        const cv = await cvService.getCv(Number(id));

        setForm({
          title: cv.title,
          fullName: cv.fullName,
          email: cv.email,
          phone: cv.phone || "",
          address: cv.address || "",
          linkedin: cv.linkedin || "",
          github: cv.github || "",
          summary: cv.summary || "",
        });
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    loadCv();
  }, [id]);

  const updateField = (
    field: keyof CvRequest,
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

    if (!id) return;

    setSaving(true);

    try {
      await cvService.updateCv(
        Number(id),
        form
      );

      navigate(`/cvs/${id}`);
    } catch (error) {
      console.error(error);
      alert("Erreur lors de la modification.");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <p>Chargement...</p>;
  }

  return (
    <div className="mx-auto max-w-4xl">
      <h2 className="mb-7 text-2xl font-bold">
        Modifier le CV
      </h2>

      <form
        onSubmit={handleSubmit}
        className="space-y-6"
      >
        <div className="rounded-2xl bg-white p-6 shadow-sm">
          <div className="grid gap-5 md:grid-cols-2">
            <Input
              label="Nom du CV"
              value={form.title}
              onChange={(e) =>
                updateField("title", e.target.value)
              }
              required
            />

            <Input
              label="Nom complet"
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
                updateField(
                  "email",
                  e.target.value
                )
              }
              required
            />

            <Input
              label="Téléphone"
              value={form.phone}
              onChange={(e) =>
                updateField(
                  "phone",
                  e.target.value
                )
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
                updateField(
                  "github",
                  e.target.value
                )
              }
            />
          </div>

          <div className="mt-5">
            <label className="mb-2 block text-sm font-medium">
              Résumé
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
              className="w-full rounded-xl border border-slate-200 p-4 outline-none focus:border-[#79B947]"
            />
          </div>
        </div>

        <div className="flex justify-end">
          <div className="w-full sm:w-56">
            <Button
              type="submit"
              loading={saving}
            >
              Enregistrer
            </Button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default CvEdit;