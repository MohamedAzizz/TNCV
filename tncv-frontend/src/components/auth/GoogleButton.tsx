const GoogleButton = () => {
  return (
    <button
      type="button"
      className="
        flex
        w-full
        items-center
        justify-center
        gap-3
        rounded-xl
        border
        border-slate-200
        bg-white
        px-4
        py-3
        text-sm
        font-medium
        text-slate-700
        transition
        hover:bg-slate-50
      "
    >
      <span className="font-bold text-lg">G</span>
      Continuer avec Google
    </button>
  );
};

export default GoogleButton;