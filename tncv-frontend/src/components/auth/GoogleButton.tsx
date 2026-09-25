interface GoogleButtonProps {
  onClick?: () => void;
}

const GoogleButton = ({
  onClick,
}: GoogleButtonProps) => {
  return (
    <button
      type="button"
      onClick={onClick}
      className="
        flex w-full items-center justify-center gap-3
        rounded-xl border border-slate-200
        bg-white px-4 py-3
        text-sm font-medium text-[#131B2E]
        transition
        hover:border-slate-300
        hover:bg-slate-50
      "
    >
      <svg
        width="18"
        height="18"
        viewBox="0 0 24 24"
      >
        <path
          fill="#4285F4"
          d="M21.35 12.27c0-.79-.07-1.55-.22-2.27H12v4.3h5.22a4.46 4.46 0 0 1-1.94 2.93v2.44h3.14c1.84-1.7 2.93-4.2 2.93-7.4Z"
        />
        <path
          fill="#34A853"
          d="M12 21.5c2.63 0 4.84-.87 6.45-2.35l-3.14-2.44c-.87.58-1.98.93-3.31.93-2.55 0-4.71-1.72-5.49-4.04H3.27v2.52A9.75 9.75 0 0 0 12 21.5Z"
        />
        <path
          fill="#FBBC05"
          d="M6.51 13.6A5.86 5.86 0 0 1 6.2 12c0-.56.1-1.1.31-1.6V7.88H3.27A9.75 9.75 0 0 0 2.25 12c0 1.57.38 3.05 1.02 4.12l3.24-2.52Z"
        />
        <path
          fill="#EA4335"
          d="M12 6.36c1.43 0 2.71.49 3.72 1.45l2.79-2.79C16.84 3.46 14.63 2.5 12 2.5a9.75 9.75 0 0 0-8.73 5.38L6.51 10.4C7.29 8.08 9.45 6.36 12 6.36Z"
        />
      </svg>

      Continuer avec Google
    </button>
  );
};

export default GoogleButton;