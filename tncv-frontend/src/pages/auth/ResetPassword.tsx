import AuthLayout from "../../components/layout/AuthLayout";

const ResetPassword = () => {
  return (
    <AuthLayout>
      <div className="rounded-3xl border border-[#e5ebe0] bg-white p-8 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-900">
          Nouveau mot de passe
        </h1>
      </div>
    </AuthLayout>
  );
};

export default ResetPassword;