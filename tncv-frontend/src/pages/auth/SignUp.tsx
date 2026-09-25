import {
  useState,
  type FormEvent,
} from "react";

import { Link, useNavigate } from "react-router-dom";
import {
  Mail,
  ShieldCheck,
  User,
} from "lucide-react";

import Logo from "../../components/common/Logo";
import Button from "../../components/common/Button";
import GoogleButton from "../../components/auth/GoogleButton";
import PasswordInput from "../../components/auth/PasswordInput";
import PasswordStrength from "../../components/auth/PasswordStrength";
import AuthLayout from "../../components/layout/AuthLayout";

import { useAuth } from "../../context/AuthContext";
import {
  isValidEmail,
  isValidPassword,
} from "../../utils/validators";

const SignUp = () => {
  const navigate = useNavigate();

  const { register } = useAuth();

  const [firstName, setFirstName] =
    useState("");

  const [lastName, setLastName] =
    useState("");

  const [email, setEmail] =
    useState("");

  const [username, setUsername] =
    useState("");

  const [password, setPassword] =
    useState("");

  const [confirmPassword, setConfirmPassword] =
    useState("");

  const [acceptedTerms, setAcceptedTerms] =
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

    if (
      !firstName.trim() ||
      !lastName.trim()
    ) {
      setError(
        "Veuillez saisir votre prénom et votre nom."
      );
      return;
    }

    if (!isValidEmail(email)) {
      setError(
        "Veuillez saisir une adresse email valide."
      );
      return;
    }

    if (!username.trim()) {
      setError(
        "Veuillez saisir un nom d'utilisateur."
      );
      return;
    }

    if (!isValidPassword(password)) {
      setError(
        "Le mot de passe doit contenir au moins 8 caractères."
      );
      return;
    }

    if (password !== confirmPassword) {
      setError(
        "Les mots de passe ne correspondent pas."
      );
      return;
    }

    if (!acceptedTerms) {
      setError(
        "Vous devez accepter les conditions d'utilisation."
      );
      return;
    }

    try {
      setLoading(true);

      await register({
        firstName,
        lastName,
        email,
        username,
        password,
      });

      navigate("/signin");
    } catch (err: any) {
      const message =
        err?.response?.data?.message ||
        "Une erreur est survenue lors de la création du compte.";

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout>
      <div className="w-full max-w-[440px]">
        {/* Logo */}
        <div className="mb-5 flex justify-center">
          <Logo size="md" />
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
          <div className="mb-6 text-center">
            <h1 className="text-2xl font-bold tracking-tight text-[#131B2E]">
              Créer un compte
            </h1>

            <p className="mt-2 text-sm text-slate-500">
              Créez votre compte TNCV et construisez
              votre CV professionnel.
            </p>
          </div>

          {/* Google */}
          <GoogleButton />

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
            className="space-y-4"
          >
            {/* Name */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div>
                <label
                  htmlFor="firstName"
                  className="mb-2 block text-sm font-medium text-[#131B2E]"
                >
                  Prénom
                </label>

                <div className="relative">
                  <User
                    size={17}
                    className="
                      absolute left-4 top-1/2
                      -translate-y-1/2
                      text-slate-400
                    "
                  />

                  <input
                    id="firstName"
                    value={firstName}
                    onChange={(e) =>
                      setFirstName(
                        e.target.value
                      )
                    }
                    placeholder="Mohamed"
                    className="
                      w-full rounded-xl
                      border border-slate-200
                      py-3 pl-10 pr-4
                      text-sm
                      outline-none
                      transition
                      focus:border-[#79B947]
                      focus:ring-4
                      focus:ring-[#79B947]/10
                    "
                  />
                </div>
              </div>

              <div>
                <label
                  htmlFor="lastName"
                  className="mb-2 block text-sm font-medium text-[#131B2E]"
                >
                  Nom
                </label>

                <input
                  id="lastName"
                  value={lastName}
                  onChange={(e) =>
                    setLastName(
                      e.target.value
                    )
                  }
                  placeholder="Cherni"
                  className="
                    w-full rounded-xl
                    border border-slate-200
                    py-3 px-4
                    text-sm
                    outline-none
                    transition
                    focus:border-[#79B947]
                    focus:ring-4
                    focus:ring-[#79B947]/10
                  "
                />
              </div>
            </div>

            {/* Username */}
            <div>
              <label
                htmlFor="username"
                className="mb-2 block text-sm font-medium text-[#131B2E]"
              >
                Nom d'utilisateur
              </label>

              <input
                id="username"
                value={username}
                onChange={(e) =>
                  setUsername(e.target.value)
                }
                placeholder="mohamedcher"
                autoComplete="username"
                className="
                  w-full rounded-xl
                  border border-slate-200
                  py-3 px-4
                  text-sm
                  outline-none
                  transition
                  focus:border-[#79B947]
                  focus:ring-4
                  focus:ring-[#79B947]/10
                "
              />
            </div>

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
                  size={17}
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
                    py-3 pl-10 pr-4
                    text-sm
                    outline-none
                    transition
                    focus:border-[#79B947]
                    focus:ring-4
                    focus:ring-[#79B947]/10
                  "
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <PasswordInput
                value={password}
                onChange={setPassword}
                label="Mot de passe"
                id="register-password"
              />

              <PasswordStrength
                password={password}
              />
            </div>

            {/* Confirm password */}
            <PasswordInput
              value={confirmPassword}
              onChange={setConfirmPassword}
              label="Confirmer le mot de passe"
              id="confirm-password"
            />

            {/* Terms */}
            <label className="flex cursor-pointer items-start gap-3 pt-1">
              <input
                type="checkbox"
                checked={acceptedTerms}
                onChange={(e) =>
                  setAcceptedTerms(
                    e.target.checked
                  )
                }
                className="
                  mt-0.5 h-4 w-4
                  rounded
                  border-slate-300
                  accent-[#79B947]
                "
              />

              <span className="text-xs leading-5 text-slate-500">
                J'accepte les{" "}
                <a
                  href="#"
                  className="font-medium text-[#79B947] hover:underline"
                >
                  conditions d'utilisation
                </a>{" "}
                et la{" "}
                <a
                  href="#"
                  className="font-medium text-[#79B947] hover:underline"
                >
                  politique de confidentialité
                </a>
                .
              </span>
            </label>

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

            {/* Submit */}
            <Button
              type="submit"
              loading={loading}
            >
              Créer mon compte
            </Button>
          </form>

          {/* Sign in */}
          <p className="mt-6 text-center text-sm text-slate-500">
            Vous avez déjà un compte ?{" "}
            <Link
              to="/signin"
              className="
                font-semibold
                text-[#79B947]
                hover:underline
              "
            >
              Se connecter
            </Link>
          </p>
        </div>

        {/* Security */}
        <div className="mt-5 flex items-center justify-center gap-2 text-xs text-slate-400">
          <ShieldCheck
            size={15}
            className="text-[#79B947]"
          />

          Création de compte sécurisée
        </div>

        <p className="mt-3 text-center text-xs text-slate-400">
          © {new Date().getFullYear()} TNCV
        </p>
      </div>
    </AuthLayout>
  );
};

export default SignUp;