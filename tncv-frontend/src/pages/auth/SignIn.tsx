import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import AuthLayout from "../../components/layout/AuthLayout";
import Input from "../../components/common/Input";
import PasswordInput from "../../components/auth/PasswordInput";
import Button from "../../components/common/Button";
import GoogleButton from "../../components/auth/GoogleButton";
import { useAuth } from "../../hooks/useAuth";

const SignIn = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (
    event: React.FormEvent
  ) => {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      await login({
        email,
        password,
      });

      navigate("/dashboard");
    } catch {
      setError(
        "Email ou mot de passe incorrect."
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
            Bon retour
          </h1>

          <p className="text-sm text-slate-500">
            Connectez-vous à votre espace TNCV.
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
          <Input
            id="email"
            label="Adresse email"
            type="email"
            placeholder="exemple@email.com"
            value={email}
            onChange={(e) =>
              setEmail(e.target.value)
            }
            required
          />

          <div>
            <PasswordInput
              label="Mot de passe"
              value={password}
              onChange={setPassword}
              placeholder="Votre mot de passe"
            />

            <div className="mt-2 text-right">
              <Link
                to="/forgot-password"
                className="text-xs font-medium text-[#79B947] hover:underline"
              >
                Mot de passe oublié ?
              </Link>
            </div>
          </div>

          <Button
            type="submit"
            loading={loading}
          >
            Se connecter
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
          Vous n'avez pas encore de compte ?{" "}
          <Link
            to="/signup"
            className="font-semibold text-[#79B947]"
          >
            Créer un compte
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
};

export default SignIn;