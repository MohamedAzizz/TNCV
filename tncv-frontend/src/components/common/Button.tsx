import type { ButtonHTMLAttributes, ReactNode } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  loading?: boolean;
}

const Button = ({
  children,
  loading = false,
  disabled,
  className = "",
  ...props
}: ButtonProps) => {
  return (
    <button
      disabled={disabled || loading}
      className={`
        flex w-full items-center justify-center gap-2
        rounded-xl px-5 py-3.5
        text-sm font-semibold text-white
        transition-all duration-200
        bg-[#79B947]
        hover:bg-[#68a83a]
        hover:shadow-lg hover:shadow-[#79B947]/20
        active:scale-[0.99]
        disabled:cursor-not-allowed
        disabled:opacity-60
        ${className}
      `}
      {...props}
    >
      {loading && (
        <span
          className="h-4 w-4 animate-spin rounded-full border-2 border-white/30 border-t-white"
        />
      )}

      {children}
    </button>
  );
};

export default Button;