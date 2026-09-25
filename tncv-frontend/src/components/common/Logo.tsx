import logo from "../../assets/images/tncv_logo.png";

interface LogoProps {
  size?: "sm" | "md" | "lg";
}

const Logo = ({ size = "md" }: LogoProps) => {
  const sizes = {
    sm: "h-8",
    md: "h-10",
    lg: "h-14",
  };

  return (
    <img
      src={logo}
      alt="TNCV"
      className={`${sizes[size]} w-auto object-contain`}
    />
  );
};

export default Logo;