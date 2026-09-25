import { useState } from "react";
import { Eye, EyeOff } from "lucide-react";

interface PasswordInputProps {
  label: string;
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

const PasswordInput = ({
  label,
  value,
  onChange,
  placeholder,
}: PasswordInputProps) => {
  const [visible, setVisible] = useState(false);

  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-slate-700">
        {label}
      </label>

      <div className="relative">
        <input
          type={visible ? "text" : "password"}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder}
          className="
            w-full
            rounded-xl
            border
            border-slate-200
            px-4
            py-3
            pr-12
            text-sm
            outline-none
            focus:border-[#79B947]
            focus:ring-2
            focus:ring-[#79B947]/20
          "
        />

        <button
          type="button"
          onClick={() => setVisible(!visible)}
          className="
            absolute
            right-3
            top-1/2
            -translate-y-1/2
            text-slate-400
            hover:text-slate-700
          "
        >
          {visible ? (
            <EyeOff size={18} />
          ) : (
            <Eye size={18} />
          )}
        </button>
      </div>
    </div>
  );
};

export default PasswordInput;