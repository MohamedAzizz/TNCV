import {
  useRef,
  useState,
  type KeyboardEvent,
} from "react";

import { Link } from "react-router-dom";

import Logo from "../../components/common/Logo";
import Button from "../../components/common/Button";
import AuthLayout from "../../components/layout/AuthLayout";

const VerifyCode = () => {
  const [code, setCode] =
    useState(["", "", "", "", "", ""]);

  const inputs =
    useRef<(HTMLInputElement | null)[]>(
      []
    );

  const handleChange = (
    index: number,
    value: string
  ) => {
    if (!/^\d?$/.test(value)) {
      return;
    }

    const next = [...code];
    next[index] = value;

    setCode(next);

    if (
      value &&
      index < 5
    ) {
      inputs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (
    index: number,
    event: KeyboardEvent<HTMLInputElement>
  ) => {
    if (
      event.key === "Backspace" &&
      !code[index] &&
      index > 0
    ) {
      inputs.current[index - 1]?.focus();
    }
  };

  const handleSubmit = () => {
    const verificationCode =
      code.join("");

    console.log(
      "Code:",
      verificationCode
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
            text-center
            shadow-[0_20px_60px_rgba(19,27,46,0.07)]
            sm:p-8
          "
        >
          <h1 className="text-2xl font-bold text-[#131B2E]">
            Vérification
          </h1>

          <p className="mt-2 text-sm leading-6 text-slate-500">
            Entrez le code à 6 chiffres reçu
            par email.
          </p>

          <div className="my-8 flex justify-center gap-2">
            {code.map((value, index) => (
              <input
                key={index}
                ref={(element) => {
                  inputs.current[index] =
                    element;
                }}
                value={value}
                maxLength={1}
                inputMode="numeric"
                onChange={(e) =>
                  handleChange(
                    index,
                    e.target.value
                  )
                }
                onKeyDown={(e) =>
                  handleKeyDown(
                    index,
                    e
                  )
                }
                className="
                  h-12 w-11
                  rounded-xl
                  border border-slate-200
                  text-center text-lg
                  font-semibold
                  text-[#131B2E]
                  outline-none
                  focus:border-[#79B947]
                  focus:ring-4
                  focus:ring-[#79B947]/10
                "
              />
            ))}
          </div>

          <Button
            type="button"
            onClick={handleSubmit}
          >
            Vérifier le code
          </Button>

          <p className="mt-6 text-sm text-slate-500">
            Vous n'avez pas reçu le code ?
          </p>

          <button
            type="button"
            className="
              mt-1
              text-sm font-semibold
              text-[#79B947]
              hover:underline
            "
          >
            Renvoyer le code
          </button>

          <p className="mt-6 text-sm">
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

export default VerifyCode;