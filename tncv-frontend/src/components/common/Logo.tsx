import logo from "../../assets/images/tncv_logo.png";

interface LogoProps {
  size?: "sm" | "md" | "lg";
  className?: string;
}

const Logo = ({ size = "md", className = "" }: LogoProps) => {
  const sizes = {
    sm: "h-10",
    md: "h-14",
    lg: "h-16",
  };

  return (
    <img
      src={logo}
      alt="TNCV Logo"
      className={`${sizes[size]} w-auto object-contain select-none ${className}`}
    />
  );
};

export default Logo;