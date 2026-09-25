import AuthLayout from "../../components/layout/AuthLayout";

const VerifyCode = () => {
  return (
    <AuthLayout>
      <div className="rounded-3xl border border-[#e5ebe0] bg-white p-8 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-900">
          Vérification du compte
        </h1>

        <p className="mt-2 text-sm text-slate-500">
          Entrez le code de vérification à 6 chiffres.
        </p>
      </div>
    </AuthLayout>
  );
};

export default VerifyCode;