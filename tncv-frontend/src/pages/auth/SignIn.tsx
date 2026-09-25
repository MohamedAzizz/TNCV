import {
  useState,
  type FormEvent,
} from "react";

import { Link, useNavigate } from "react-router-dom";
import { Mail, ShieldCheck } from "lucide-react";

import Logo from "../../components/common/Logo";
import Button from "../../components/common/Button";
import GoogleButton from "../../components/auth/GoogleButton";
import PasswordInput from "../../components/auth/PasswordInput";
import AuthLayout from "../../components/layout/AuthLayout";

import { useAuth } from "../../context/AuthContext";
import { isValidEmail } from "../../utils/validators";

const SignIn = () => {
  const navigate = useNavigate();

  const { login } = useAuth();

  const [email, setEmail] =
    useState("");

  const [password, setPassword] =
    useState("");

  const [rememberMe, setRememberMe] =
    useState(false);

  const [error, setError] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const handleSubmit = async (
    event: FormEvent
  ) => {
    event.preventDefault();

    setError("");

    if (!isValidEmail(email)) {
      setError(
        "Veuillez saisir une adresse email valide."
      );
      return;
    }

    if (!password) {
      setError(
        "Veuillez saisir votre mot de passe."
      );
      return;
    }

    try {
      setLoading(true);

      await login({
        email,
        password,
      });

      navigate("/dashboard");
    } catch (err: any) {
      const message =
        err?.response?.data?.message ||
        "Email ou mot de passe incorrect.";

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout>
      <div className="w-full max-w-[440px]">
        {/* Logo */}
        <div className="mb-6 flex justify-center">
          <Logo size="lg" />
        </div>

        {/* Card */}
        <div
          className="
            rounded-3xl
            border border-slate-200/80
            bg-white
            p-6
            shadow-[0_20px_60px_rgba(19,27,46,0.07)]
            sm:p-8
          "
        >
          {/* Header */}
          <div className="mb-7 text-center">
            <h1 className="text-2xl font-bold tracking-tight text-[#131B2E]">
              Connexion à votre espace
            </h1>

            <p className="mt-2 text-sm text-slate-500">
              Retrouvez vos CV et continuez votre parcours
              professionnel.
            </p>
          </div>

          {/* Google */}
          <GoogleButton
            onClick={() => {
              console.log(
                "Google OAuth sera connecté plus tard."
              );
            }}
          />

          {/* Divider */}
          <div className="my-6 flex items-center gap-4">
            <div className="h-px flex-1 bg-slate-200" />

            <span className="text-xs text-slate-400">
              OU
            </span>

            <div className="h-px flex-1 bg-slate-200" />
          </div>

          <form
            onSubmit={handleSubmit}
            className="space-y-5"
          >
            {/* Email */}
            <div>
              <label
                htmlFor="email"
                className="mb-2 block text-sm font-medium text-[#131B2E]"
              >
                Adresse email
              </label>

              <div className="relative">
                <Mail
                  size={18}
                  className="
                    absolute left-4 top-1/2
                    -translate-y-1/2
                    text-slate-400
                  "
                />

                <input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) =>
                    setEmail(e.target.value)
                  }
                  placeholder="vous@example.com"
                  autoComplete="email"
                  className="
                    w-full rounded-xl
                    border border-slate-200
                    bg-white
                    py-3 pl-11 pr-4
                    text-sm text-[#131B2E]
                    outline-none
                    transition
                    placeholder:text-slate-400
                    focus:border-[#79B947]
                    focus:ring-4
                    focus:ring-[#79B947]/10
                  "
                />
              </div>
            </div>

            {/* Password */}
            <PasswordInput
              value={password}
              onChange={setPassword}
              label="Mot de passe"
            />

            {/* Options */}
            <div className="flex items-center justify-between gap-4">
              <label className="flex cursor-pointer items-center gap-2">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) =>
                    setRememberMe(
                      e.target.checked
                    )
                  }
                  className="
                    h-4 w-4
                    rounded
                    border-slate-300
                    text-[#79B947]
                    accent-[#79B947]
                  "
                />

                <span className="text-xs text-slate-500">
                  Se souvenir de moi
                </span>
              </label>

              <Link
                to="/forgot-password"
                className="
                  text-xs font-medium
                  text-[#79B947]
                  hover:underline
                "
              >
                Mot de passe oublié ?
              </Link>
            </div>

            {/* Error */}
            {error && (
              <div
                className="
                  rounded-xl
                  border border-red-200
                  bg-red-50
                  px-4 py-3
                  text-sm text-red-600
                "
              >
                {error}
              </div>
            )}

            {/* Login */}
            <Button
              type="submit"
              loading={loading}
            >
              Se connecter
            </Button>
          </form>

          {/* Register */}
          <p className="mt-7 text-center text-sm text-slate-500">
            Vous n'avez pas encore de compte ?{" "}
            <Link
              to="/signup"
              className="
                font-semibold
                text-[#79B947]
                hover:underline
              "
            >
              Créer un compte
            </Link>
          </p>
        </div>

        {/* Security */}
        <div className="mt-6 flex items-center justify-center gap-2 text-xs text-slate-400">
          <ShieldCheck
            size={15}
            className="text-[#79B947]"
          />

          Vos données sont protégées et sécurisées.
        </div>

        <p className="mt-3 text-center text-xs text-slate-400">
          © {new Date().getFullYear()} TNCV
        </p>
      </div>
    </AuthLayout>
  );
};

export default SignIn;