import type { ButtonHTMLAttributes, ReactNode } from "react";

interface ButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  loading?: boolean;
}

const Button = ({
  children,
  loading = false,
  disabled,
  ...props
}: ButtonProps) => {
  return (
    <button
      {...props}
      disabled={disabled || loading}
      className="
        w-full
        rounded-xl
        px-4
        py-3
        font-semibold
        text-white
        transition-all
        duration-200
        hover:opacity-90
        active:scale-[0.98]
        disabled:cursor-not-allowed
        disabled:opacity-60
      "
      style={{ backgroundColor: "#79B947" }}
    >
      {loading ? "Chargement..." : children}
    </button>
  );
};

export default Button;