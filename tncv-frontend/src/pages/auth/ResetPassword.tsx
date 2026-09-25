import {
  useState,
  type FormEvent,
} from "react";

import { Link } from "react-router-dom";

import Logo from "../../components/common/Logo";
import Button from "../../components/common/Button";
import PasswordInput from "../../components/auth/PasswordInput";
import PasswordStrength from "../../components/auth/PasswordStrength";
import AuthLayout from "../../components/layout/AuthLayout";

const ResetPassword = () => {
  const [password, setPassword] =
    useState("");

  const [confirmPassword, setConfirmPassword] =
    useState("");

  const [message, setMessage] =
    useState("");

  const handleSubmit = (
    event: FormEvent
  ) => {
    event.preventDefault();

    if (password.length < 8) {
      setMessage(
        "Le mot de passe doit contenir au moins 8 caractères."
      );
      return;
    }

    if (password !== confirmPassword) {
      setMessage(
        "Les mots de passe ne correspondent pas."
      );
      return;
    }

    setMessage(
      "Votre mot de passe a été modifié."
    );
  };

  return (
    <AuthLayout>
      <div className="w-full max-w-[440px]">
        <div className="mb-6 flex justify-center">
          <Logo size="lg" />
        </div>

        <div
          className="
            rounded-3xl
            border border-slate-200
            bg-white
            p-6
            shadow-[0_20px_60px_rgba(19,27,46,0.07)]
            sm:p-8
          "
        >
          <h1 className="text-2xl font-bold text-[#131B2E]">
            Nouveau mot de passe
          </h1>

          <p className="mt-2 mb-6 text-sm text-slate-500">
            Choisissez un nouveau mot de passe
            sécurisé.
          </p>

          <form
            onSubmit={handleSubmit}
            className="space-y-5"
          >
            <div>
              <PasswordInput
                value={password}
                onChange={setPassword}
                label="Nouveau mot de passe"
              />

              <PasswordStrength
                password={password}
              />
            </div>

            <PasswordInput
              value={confirmPassword}
              onChange={setConfirmPassword}
              label="Confirmer le mot de passe"
            />

            {message && (
              <div className="rounded-xl bg-slate-50 p-3 text-sm text-slate-600">
                {message}
              </div>
            )}

            <Button type="submit">
              Modifier le mot de passe
            </Button>
          </form>

          <p className="mt-6 text-center text-sm">
            <Link
              to="/signin"
              className="font-semibold text-[#79B947]"
            >
              Retour à la connexion
            </Link>
          </p>
        </div>
      </div>
    </AuthLayout>
  );
};

export default ResetPassword;