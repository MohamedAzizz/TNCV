import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import AuthLayout from "../../components/layout/AuthLayout";
import Input from "../../components/common/Input";
import PasswordInput from "../../components/auth/PasswordInput";
import PasswordStrength from "../../components/auth/PasswordStrength";
import GoogleButton from "../../components/auth/GoogleButton";
import Button from "../../components/common/Button";
import { useAuth } from "../../hooks/useAuth";

const SignUp = () => {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [terms, setTerms] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (
    event: React.FormEvent
  ) => {
    event.preventDefault();

    if (!terms) {
      setError(
        "Vous devez accepter les conditions d'utilisation."
      );
      return;
    }

    setError("");
    setLoading(true);

    try {
      await register({
        username: email,
        email,
        password,
        firstName,
        lastName,
      });

      navigate("/signin");
    } catch {
      setError(
        "Impossible de créer le compte. Vérifiez vos informations."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout>
      <div className="rounded-3xl border border-[#e5ebe0] bg-white p-7 shadow-[0_12px_40px_-10px_rgba(20,35,15,0.08)] sm:p-9">
        <div className="mb-7 text-center">
          <h1 className="mb-2 text-2xl font-bold text-slate-900">
            Créer un compte
          </h1>

          <p className="text-sm leading-relaxed text-slate-500">
            Commencez votre expérience avec TNCV.
          </p>
        </div>

        {error && (
          <div className="mb-5 rounded-xl bg-red-50 px-4 py-3 text-sm text-red-600">
            {error}
          </div>
        )}

        <form
          onSubmit={handleSubmit}
          className="space-y-5"
        >
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <Input
              label="Prénom"
              id="firstName"
              placeholder="Aziz"
              value={firstName}
              onChange={(e) =>
                setFirstName(e.target.value)
              }
              required
            />

            <Input
              label="Nom"
              id="lastName"
              placeholder="Cherni"
              value={lastName}
              onChange={(e) =>
                setLastName(e.target.value)
              }
              required
            />
          </div>

          <Input
            label="Adresse email"
            id="email"
            type="email"
            placeholder="exemple@email.com"
            value={email}
            onChange={(e) =>
              setEmail(e.target.value)
            }
            required
          />

          <div className="space-y-3">
            <PasswordInput
              label="Mot de passe"
              value={password}
              onChange={setPassword}
              placeholder="Créer un mot de passe"
            />

            <PasswordStrength
              password={password}
            />
          </div>

          <label className="flex items-start gap-3 text-sm text-slate-500">
            <input
              type="checkbox"
              checked={terms}
              onChange={(e) =>
                setTerms(e.target.checked)
              }
              className="mt-1 accent-[#79B947]"
            />

            <span>
              J'accepte les conditions d'utilisation
              et la politique de confidentialité.
            </span>
          </label>

          <Button
            type="submit"
            loading={loading}
          >
            Créer mon compte
          </Button>
        </form>

        <div className="my-6 flex items-center gap-3">
          <div className="h-px flex-1 bg-slate-200" />

          <span className="text-xs text-slate-400">
            OU
          </span>

          <div className="h-px flex-1 bg-slate-200" />
        </div>

        <GoogleButton />

        <p className="mt-7 text-center text-sm text-slate-500">
          Vous avez déjà un compte ?{" "}
          <Link
            to="/signin"
            className="font-semibold text-[#79B947]"
          >
            Se connecter
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
};

export default SignUp;