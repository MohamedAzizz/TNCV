import AuthLayout from "../../components/layout/AuthLayout";

const ForgotPassword = () => {
  return (
    <AuthLayout>
      <div className="rounded-3xl border border-[#e5ebe0] bg-white p-8 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-900">
          Mot de passe oublié
        </h1>

        <p className="mt-2 text-sm text-slate-500">
          La récupération du mot de passe sera connectée
          au service d'authentification.
        </p>
      </div>
    </AuthLayout>
  );
};

export default ForgotPassword;