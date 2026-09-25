interface PasswordStrengthProps {
  password: string;
}

const PasswordStrength = ({
  password,
}: PasswordStrengthProps) => {
  let score = 0;

  if (password.length >= 8) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  const labels = [
    "Très faible",
    "Faible",
    "Moyenne",
    "Bonne",
    "Forte",
  ];

  if (!password) {
    return null;
  }

  return (
    <div className="space-y-2">
      <div className="flex gap-1">
        {[1, 2, 3, 4].map((item) => (
          <div
            key={item}
            className="h-1.5 flex-1 rounded-full bg-slate-200"
            style={
              item <= score
                ? { backgroundColor: "#79B947" }
                : undefined
            }
          />
        ))}
      </div>

      <p className="text-xs text-slate-500">
        {labels[score]}
      </p>
    </div>
  );
};

export default PasswordStrength;