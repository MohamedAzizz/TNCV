interface PasswordStrengthProps {
  password: string;
}

const PasswordStrength = ({
  password,
}: PasswordStrengthProps) => {
  if (!password) {
    return null;
  }

  let score = 0;

  if (password.length >= 8) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  const messages = [
    "",
    "Très faible",
    "Faible",
    "Moyen",
    "Fort",
  ];

  return (
    <div className="mt-3">
      <div className="mb-2 flex gap-1.5">
        {[1, 2, 3, 4].map((bar) => (
          <div
            key={bar}
            className={`
              h-1.5 flex-1 rounded-full transition
              ${
                bar <= score
                  ? "bg-[#79B947]"
                  : "bg-slate-200"
              }
            `}
          />
        ))}
      </div>

      <p className="text-xs text-slate-500">
        Force :{" "}
        <span className="font-medium text-[#131B2E]">
          {messages[score]}
        </span>
      </p>

      <div className="mt-2 space-y-1 text-xs text-slate-400">
        <p className={password.length >= 8 ? "text-[#79B947]" : ""}>
          ✓ Au moins 8 caractères
        </p>

        <p className={/[A-Z]/.test(password) ? "text-[#79B947]" : ""}>
          ✓ Une lettre majuscule
        </p>

        <p className={/[0-9]/.test(password) ? "text-[#79B947]" : ""}>
          ✓ Un chiffre
        </p>

        <p
          className={
            /[^A-Za-z0-9]/.test(password)
              ? "text-[#79B947]"
              : ""
          }
        >
          ✓ Un caractère spécial
        </p>
      </div>
    </div>
  );
};

export default PasswordStrength;