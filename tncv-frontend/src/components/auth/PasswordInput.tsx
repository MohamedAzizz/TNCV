import { useState } from "react";
import { Eye, EyeOff, LockKeyhole } from "lucide-react";

interface PasswordInputProps {
  label?: string;
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  error?: string;
  id?: string;
}

const PasswordInput = ({
  label = "Mot de passe",
  value,
  onChange,
  placeholder = "••••••••",
  error,
  id = "password",
}: PasswordInputProps) => {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="w-full">
      <label
        htmlFor={id}
        className="mb-2 block text-sm font-medium text-[#131B2E]"
      >
        {label}
      </label>

      <div className="relative">
        <LockKeyhole
          size={18}
          className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
        />

        <input
          id={id}
          type={showPassword ? "text" : "password"}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder}
          className={`
            w-full rounded-xl border bg-white
            py-3 pl-11 pr-12 text-sm text-[#131B2E]
            outline-none transition
            placeholder:text-slate-400
            focus:border-[#79B947]
            focus:ring-4 focus:ring-[#79B947]/10
            ${
              error
                ? "border-red-400"
                : "border-slate-200"
            }
          `}
        />

        <button
          type="button"
          onClick={() => setShowPassword((value) => !value)}
          className="
            absolute right-3 top-1/2
            -translate-y-1/2
            rounded-lg p-2
            text-slate-400
            transition
            hover:bg-slate-100
            hover:text-[#131B2E]
          "
          aria-label={
            showPassword
              ? "Masquer le mot de passe"
              : "Afficher le mot de passe"
          }
        >
          {showPassword ? (
            <EyeOff size={18} />
          ) : (
            <Eye size={18} />
          )}
        </button>
      </div>

      {error && (
        <p className="mt-1.5 text-xs text-red-500">
          {error}
        </p>
      )}
    </div>
  );
};

export default PasswordInput;