import type { InputHTMLAttributes } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

const Input = ({
  label,
  error,
  className = "",
  id,
  ...props
}: InputProps) => {
  return (
    <div className="w-full">
      {label && (
        <label
          htmlFor={id}
          className="mb-2 block text-sm font-medium text-[#131B2E]"
        >
          {label}
        </label>
      )}

      <input
        id={id}
        className={`
          w-full rounded-xl border bg-white px-4 py-3
          text-sm text-[#131B2E]
          outline-none transition
          placeholder:text-slate-400
          focus:border-[#79B947]
          focus:ring-4 focus:ring-[#79B947]/10
          ${
            error
              ? "border-red-400 focus:border-red-500 focus:ring-red-100"
              : "border-slate-200"
          }
          ${className}
        `}
        {...props}
      />

      {error && (
        <p className="mt-1.5 text-xs text-red-500">
          {error}
        </p>
      )}
    </div>
  );
};

export default Input;