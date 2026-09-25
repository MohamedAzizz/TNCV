import {
  useState,
  type FormEvent,
} from "react";

import { Link } from "react-router-dom";
import { ArrowLeft, Mail } from "lucide-react";

import Logo from "../../components/common/Logo";
import Button from "../../components/common/Button";
import AuthLayout from "../../components/layout/AuthLayout";

const ForgotPassword = () => {
  const [email, setEmail] =
    useState("");

  const [submitted, setSubmitted] =
    useState(false);

  const handleSubmit = (
    event: FormEvent
  ) => {
    event.preventDefault();

    setSubmitted(true);
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
          <Link
            to="/signin"
            className="
              mb-6 inline-flex items-center
              gap-2 text-sm text-slate-500
              hover:text-[#79B947]
            "
          >
            <ArrowLeft size={16} />
            Retour
          </Link>

          <div className="mb-7">
            <h1 className="text-2xl font-bold text-[#131B2E]">
              Mot de passe oublié ?
            </h1>

            <p className="mt-2 text-sm leading-6 text-slate-500">
              Saisissez votre adresse email.
              Nous vous enverrons un code de
              validation.
            </p>
          </div>

          {submitted ? (
            <div
              className="
                rounded-xl
                border border-[#79B947]/20
                bg-[#79B947]/10
                p-4
                text-sm
                text-[#4c8326]
              "
            >
              Si cette adresse existe, un code
              de validation sera envoyé.
            </div>
          ) : (
            <form
              onSubmit={handleSubmit}
              className="space-y-5"
            >
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
                    required
                    value={email}
                    onChange={(e) =>
                      setEmail(e.target.value)
                    }
                    placeholder="vous@example.com"
                    className="
                      w-full rounded-xl
                      border border-slate-200
                      py-3 pl-11 pr-4
                      text-sm
                      outline-none
                      focus:border-[#79B947]
                      focus:ring-4
                      focus:ring-[#79B947]/10
                    "
                  />
                </div>
              </div>

              <Button type="submit">
                Envoyer le code
              </Button>
            </form>
          )}

          <p className="mt-6 text-center text-sm text-slate-500">
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

export default ForgotPassword;