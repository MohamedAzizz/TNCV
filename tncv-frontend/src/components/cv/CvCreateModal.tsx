import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  X,
  ChevronRight,
  ChevronLeft,
  Check,
  Plus,
  Trash2,
  Sparkles,
  Briefcase,
  GraduationCap,
  Wrench,
  Languages as LanguagesIcon,
  CheckCircle2,
  FileText,
  User,
  AlertCircle,
  Loader2,
} from "lucide-react";
import { cvService } from "../../services/cvService";
import type { Cv } from "../../types/cv";

interface CvCreateModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (newCv: Cv) => void;
  initialTemplate?: "moderne" | "tech" | "minimal";
  userDefaults?: {
    fullName?: string;
    email?: string;
  };
}

interface ExperienceItem {
  company: string;
  position: string;
  location: string;
  startDate: string;
  endDate: string;
  current: boolean;
  description: string;
}

interface EducationItem {
  institution: string;
  degree: string;
  fieldOfStudy: string;
  location: string;
  startDate: string;
  endDate: string;
  current: boolean;
  description: string;
}

interface SkillItem {
  name: string;
  category: string;
  level: string;
}

interface LanguageItem {
  name: string;
  level: string;
}

const POPULAR_SKILLS = [
  "Java",
  "Spring Boot",
  "React",
  "TypeScript",
  "JavaScript",
  "Docker",
  "PostgreSQL",
  "Git & GitHub",
  "Python",
  "Tailwind CSS",
  "REST API",
  "Microservices",
  "Gestion de projet",
  "Méthode Agile / Scrum",
];

const SUMMARY_SUGGESTIONS = [
  {
    title: "Développeur Full Stack",
    text: "Développeur Full Stack passionné avec une solide maîtrise des technologies modernes (React, Spring Boot, PostgreSQL). Expérimenté dans la conception d'architectures résilientes, la création d'APIs RESTful et l'optimisation des performances.",
  },
  {
    title: "Ingénieur Logiciel & Cloud",
    text: "Ingénieur logiciel rigoureux, spécialisé dans l'écosystème microservices et les architectures cloud. Fortes compétences en conteneurisation Docker, CI/CD et développement d'applications hautement disponibles.",
  },
  {
    title: "Profil Polyvalent & Agile",
    text: "Professionnel dynamique orienté résultats, alliant compétences techniques et capacité à collaborer au sein d'équipes agiles. Toujours motivé par l'apprentissage continu et l'impact positif sur les projets.",
  },
];

export const CvCreateModal = ({
  isOpen,
  onClose,
  onSuccess,
  initialTemplate = "moderne",
  userDefaults,
}: CvCreateModalProps) => {
  const navigate = useNavigate();

  const [currentStep, setCurrentStep] = useState<number>(1);
  const [selectedTemplate, setSelectedTemplate] = useState<"moderne" | "tech" | "minimal">(
    initialTemplate
  );

  // Form states
  const [generalInfo, setGeneralInfo] = useState({
    title: "",
    fullName: userDefaults?.fullName || "",
    email: userDefaults?.email || "",
    phone: "",
    address: "",
    linkedin: "",
    github: "",
    summary: "",
  });

  const [experiences, setExperiences] = useState<ExperienceItem[]>([
    {
      company: "",
      position: "",
      location: "",
      startDate: "",
      endDate: "",
      current: false,
      description: "",
    },
  ]);

  const [educations, setEducations] = useState<EducationItem[]>([
    {
      institution: "",
      degree: "",
      fieldOfStudy: "",
      location: "",
      startDate: "",
      endDate: "",
      current: false,
      description: "",
    },
  ]);

  const [skills, setSkills] = useState<SkillItem[]>([
    { name: "React", category: "Frontend", level: "Avancé" },
    { name: "Spring Boot", category: "Backend", level: "Avancé" },
  ]);

  const [languages, setLanguages] = useState<LanguageItem[]>([
    { name: "Français", level: "Courant" },
    { name: "Anglais", level: "Professionnel" },
  ]);

  const [newSkillName, setNewSkillName] = useState("");
  const [newSkillCategory, setNewSkillCategory] = useState("Technique");
  const [newSkillLevel, setNewSkillLevel] = useState("Intermédiaire");

  const [newLangName, setNewLangName] = useState("");
  const [newLangLevel, setNewLangLevel] = useState("Intermédiaire");

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submissionProgress, setSubmissionProgress] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [createdCv, setCreatedCv] = useState<Cv | null>(null);

  if (!isOpen) return null;

  const steps = [
    { id: 1, label: "Coordonnées", icon: User },
    { id: 2, label: "Profil & Résumé", icon: FileText },
    { id: 3, label: "Expériences", icon: Briefcase },
    { id: 4, label: "Formation", icon: GraduationCap },
    { id: 5, label: "Compétences", icon: Wrench },
    { id: 6, label: "Récapitulatif", icon: CheckCircle2 },
  ];

  // Validation per step
  const validateStep = (step: number): boolean => {
    setErrorMessage("");
    if (step === 1) {
      if (!generalInfo.title.trim()) {
        setErrorMessage("Le titre du CV est obligatoire (ex: Développeur Full Stack).");
        return false;
      }
      if (!generalInfo.fullName.trim()) {
        setErrorMessage("Le nom complet est obligatoire.");
        return false;
      }
      if (!generalInfo.email.trim() || !generalInfo.email.includes("@")) {
        setErrorMessage("Une adresse email valide est obligatoire.");
        return false;
      }
    }
    return true;
  };

  const handleNext = () => {
    if (validateStep(currentStep)) {
      setCurrentStep((prev) => Math.min(prev + 1, 6));
    }
  };

  const handlePrev = () => {
    setErrorMessage("");
    setCurrentStep((prev) => Math.max(prev - 1, 1));
  };

  // Experience handlers
  const handleAddExperience = () => {
    setExperiences((prev) => [
      ...prev,
      {
        company: "",
        position: "",
        location: "",
        startDate: "",
        endDate: "",
        current: false,
        description: "",
      },
    ]);
  };

  const handleRemoveExperience = (index: number) => {
    setExperiences((prev) => prev.filter((_, i) => i !== index));
  };

  const handleUpdateExperience = (
    index: number,
    field: keyof ExperienceItem,
    value: any
  ) => {
    setExperiences((prev) =>
      prev.map((exp, i) => (i === index ? { ...exp, [field]: value } : exp))
    );
  };

  // Education handlers
  const handleAddEducation = () => {
    setEducations((prev) => [
      ...prev,
      {
        institution: "",
        degree: "",
        fieldOfStudy: "",
        location: "",
        startDate: "",
        endDate: "",
        current: false,
        description: "",
      },
    ]);
  };

  const handleRemoveEducation = (index: number) => {
    setEducations((prev) => prev.filter((_, i) => i !== index));
  };

  const handleUpdateEducation = (
    index: number,
    field: keyof EducationItem,
    value: any
  ) => {
    setEducations((prev) =>
      prev.map((edu, i) => (i === index ? { ...edu, [field]: value } : edu))
    );
  };

  // Skill handlers
  const handleAddSkill = () => {
    if (!newSkillName.trim()) return;
    setSkills((prev) => [
      ...prev,
      {
        name: newSkillName.trim(),
        category: newSkillCategory,
        level: newSkillLevel,
      },
    ]);
    setNewSkillName("");
  };

  const handleRemoveSkill = (index: number) => {
    setSkills((prev) => prev.filter((_, i) => i !== index));
  };

  const handleQuickAddSkill = (skillName: string) => {
    if (!skills.some((s) => s.name.toLowerCase() === skillName.toLowerCase())) {
      setSkills((prev) => [
        ...prev,
        { name: skillName, category: "Technique", level: "Avancé" },
      ]);
    }
  };

  // Language handlers
  const handleAddLanguage = () => {
    if (!newLangName.trim()) return;
    setLanguages((prev) => [
      ...prev,
      {
        name: newLangName.trim(),
        level: newLangLevel,
      },
    ]);
    setNewLangName("");
  };

  const handleRemoveLanguage = (index: number) => {
    setLanguages((prev) => prev.filter((_, i) => i !== index));
  };

  // Submit complete CV to Backend
  const handleSubmitCv = async () => {
    setIsSubmitting(true);
    setErrorMessage("");
    setSubmissionProgress("Création du CV dans la base de données...");

    try {
      // 1. Create main CV
      const newCv = await cvService.createCv({
        title: generalInfo.title.trim(),
        fullName: generalInfo.fullName.trim(),
        email: generalInfo.email.trim(),
        phone: generalInfo.phone.trim() || undefined,
        address: generalInfo.address.trim() || undefined,
        linkedin: generalInfo.linkedin.trim() || undefined,
        github: generalInfo.github.trim() || undefined,
        summary: generalInfo.summary.trim() || undefined,
      });

      const cvId = newCv.id!;

      // 2. Add experiences
      const validExperiences = experiences.filter(
        (e) => e.company.trim() && e.position.trim()
      );
      if (validExperiences.length > 0) {
        setSubmissionProgress("Enregistrement des expériences professionnelles...");
        for (const exp of validExperiences) {
          try {
            await cvService.addExperience(cvId, {
              company: exp.company.trim(),
              position: exp.position.trim(),
              location: exp.location.trim() || undefined,
              startDate: exp.startDate.trim() || undefined,
              endDate: exp.endDate.trim() || undefined,
              current: exp.current,
              description: exp.description.trim() || undefined,
            });
          } catch (e) {
            console.warn("Erreur lors de l'ajout d'une expérience:", e);
          }
        }
      }

      // 3. Add educations
      const validEducations = educations.filter(
        (e) => e.institution.trim() && e.degree.trim()
      );
      if (validEducations.length > 0) {
        setSubmissionProgress("Enregistrement des formations & diplômes...");
        for (const edu of validEducations) {
          try {
            await cvService.addEducation(cvId, {
              institution: edu.institution.trim(),
              degree: edu.degree.trim(),
              fieldOfStudy: edu.fieldOfStudy.trim() || undefined,
              location: edu.location.trim() || undefined,
              startDate: edu.startDate.trim() || undefined,
              endDate: edu.endDate.trim() || undefined,
              current: edu.current,
              description: edu.description.trim() || undefined,
            });
          } catch (e) {
            console.warn("Erreur lors de l'ajout d'une formation:", e);
          }
        }
      }

      // 4. Add skills
      const validSkills = skills.filter((s) => s.name.trim());
      if (validSkills.length > 0) {
        setSubmissionProgress("Enregistrement des compétences clés...");
        for (const sk of validSkills) {
          try {
            await cvService.addSkill(cvId, {
              name: sk.name.trim(),
              category: sk.category || "Général",
              level: sk.level || "Intermédiaire",
            });
          } catch (e) {
            console.warn("Erreur lors de l'ajout d'une compétence:", e);
          }
        }
      }

      // 5. Add languages
      const validLanguages = languages.filter((l) => l.name.trim());
      if (validLanguages.length > 0) {
        setSubmissionProgress("Enregistrement des langues...");
        for (const lang of validLanguages) {
          try {
            await cvService.addLanguage(cvId, {
              name: lang.name.trim(),
              level: lang.level || "Intermédiaire",
            });
          } catch (e) {
            console.warn("Erreur lors de l'ajout d'une langue:", e);
          }
        }
      }

      setSubmissionProgress("CV créé avec succès !");
      setCreatedCv(newCv);
      onSuccess(newCv);
    } catch (err: any) {
      console.error(err);
      setErrorMessage(
        err?.response?.data?.message ||
          "Une erreur est survenue lors de la création de votre CV. Vérifiez votre connexion avec le serveur."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 sm:p-5 bg-black/60 backdrop-blur-sm overflow-y-auto animate-fade-in">
      <div className="relative w-full max-w-4xl bg-white rounded-3xl shadow-2xl border border-[#e2ece0] overflow-hidden flex flex-col max-h-[92vh]">
        {/* Modal Top Header */}
        <div className="px-6 py-5 border-b border-[#e5eee2] bg-gradient-to-r from-white via-[#f7fbf4] to-[#f0f7ec] flex items-center justify-between shrink-0">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-[#edf7e7] border border-[#d6ebd0] text-[#346b1d] flex items-center justify-center shadow-2xs">
              <Sparkles size={20} />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-lg sm:text-xl font-bold text-[#142111]">
                  Créateur de CV Intelligent
                </h2>
                <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-[#e8f5e3] text-[#346b1d] border border-[#cfe9c6]">
                  Modèle {selectedTemplate.toUpperCase()}
                </span>
              </div>
              <p className="text-xs text-[#52634e]">
                Étape {currentStep} sur 6 • {steps[currentStep - 1].label}
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 rounded-xl text-[#657760] hover:text-[#182615] hover:bg-[#edf4ea] transition-colors cursor-pointer"
            title="Fermer"
          >
            <X size={20} />
          </button>
        </div>

        {/* Stepper Progress Bar */}
        <div className="px-6 py-3 bg-[#f8fbf5] border-b border-[#e5eee2] shrink-0">
          <div className="flex items-center justify-between gap-1 overflow-x-auto py-1">
            {steps.map((step) => {
              const Icon = step.icon;
              const isActive = currentStep === step.id;
              const isPast = currentStep > step.id;

              return (
                <button
                  key={step.id}
                  onClick={() => {
                    if (step.id < currentStep || validateStep(currentStep)) {
                      setCurrentStep(step.id);
                    }
                  }}
                  className={`flex items-center gap-2 px-3 py-1.5 rounded-xl text-xs font-semibold transition-all shrink-0 cursor-pointer ${
                    isActive
                      ? "bg-[#79b947] text-white shadow-2xs"
                      : isPast
                      ? "bg-[#edf7e7] text-[#2e6217] hover:bg-[#e2f2da]"
                      : "text-[#71826b] hover:bg-[#f0f4ee]"
                  }`}
                >
                  <div
                    className={`w-5 h-5 rounded-full flex items-center justify-center text-[10px] ${
                      isActive
                        ? "bg-white/20 text-white"
                        : isPast
                        ? "bg-[#dcf3d4] text-[#285e13]"
                        : "bg-slate-200 text-slate-600"
                    }`}
                  >
                    {isPast ? <Check size={12} /> : step.id}
                  </div>
                  <Icon size={13} className="hidden md:inline shrink-0 opacity-80" />
                  <span className="hidden sm:inline">{step.label}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Modal Body / Scrollable Content */}
        <div className="p-6 sm:p-8 overflow-y-auto flex-1 bg-white">
          {errorMessage && (
            <div className="mb-6 p-4 rounded-2xl bg-red-50 border border-red-200 flex items-start gap-3 text-sm text-red-700">
              <AlertCircle size={18} className="shrink-0 mt-0.5 text-red-600" />
              <span>{errorMessage}</span>
            </div>
          )}

          {/* Success Screen */}
          {createdCv ? (
            <div className="py-12 flex flex-col items-center justify-center text-center">
              <div className="w-18 h-18 rounded-3xl bg-[#edf7e7] border-2 border-[#79b947] flex items-center justify-center text-[#356b00] shadow-md mb-5 animate-bounce">
                <CheckCircle2 size={42} />
              </div>
              <span className="inline-block px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider bg-[#dcf3d4] text-[#2a6015] mb-2">
                Enregistré avec succès dans le cloud TNCV
              </span>
              <h3 className="text-2xl font-bold text-[#142111]">
                Votre CV "{createdCv.title}" est prêt !
              </h3>
              <p className="mt-2 text-sm text-[#556750] max-w-md">
                Toutes vos données (coordonnées, expériences, formations, compétences et langues) ont été liées et enregistrées dans le microservice CV.
              </p>
              <div className="mt-8 flex flex-col sm:flex-row items-center gap-4 w-full max-w-md">
                <button
                  onClick={() => {
                    onClose();
                    navigate(`/cvs/${createdCv.id}`);
                  }}
                  className="w-full sm:flex-1 h-12 flex items-center justify-center gap-2 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white font-semibold text-sm shadow-md transition-all cursor-pointer"
                >
                  <FileText size={18} />
                  <span>Consulter mon CV</span>
                </button>
                <button
                  onClick={() => {
                    onClose();
                  }}
                  className="w-full sm:flex-1 h-12 flex items-center justify-center gap-2 rounded-xl bg-[#f2f6ee] hover:bg-[#e4ede0] text-[#34462e] border border-[#dbe6d6] font-semibold text-sm transition-all cursor-pointer"
                >
                  <span>Retour au tableau de bord</span>
                </button>
              </div>
            </div>
          ) : (
            <>
              {/* STEP 1: Coordonnées */}
              {currentStep === 1 && (
                <div className="space-y-6">
                  <div>
                    <h3 className="text-lg font-bold text-[#142111]">
                      Informations générales & Coordonnées
                    </h3>
                    <p className="text-xs text-[#52634e] mt-1">
                      Ces informations figureront dans l'en-tête de votre CV et permettront aux recruteurs de vous contacter facilement.
                    </p>
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="sm:col-span-2">
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Intitulé du CV <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="text"
                        placeholder="Ex: Développeur Full Stack Senior, Lead UI/UX Designer..."
                        value={generalInfo.title}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, title: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Nom complet <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="text"
                        placeholder="Ex: Mohamed Aziz Cherni"
                        value={generalInfo.fullName}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, fullName: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Email professionnel <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="email"
                        placeholder="votre.email@example.com"
                        value={generalInfo.email}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, email: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Téléphone
                      </label>
                      <input
                        type="tel"
                        placeholder="+216 20 123 456"
                        value={generalInfo.phone}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, phone: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Ville / Adresse
                      </label>
                      <input
                        type="text"
                        placeholder="Tunis, Tunisie"
                        value={generalInfo.address}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, address: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Profil LinkedIn (URL)
                      </label>
                      <input
                        type="url"
                        placeholder="https://linkedin.com/in/monprofil"
                        value={generalInfo.linkedin}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, linkedin: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-bold text-[#23331f] mb-1.5 uppercase tracking-wide">
                        Profil GitHub / Portfolio (URL)
                      </label>
                      <input
                        type="url"
                        placeholder="https://github.com/monprofil"
                        value={generalInfo.github}
                        onChange={(e) =>
                          setGeneralInfo({ ...generalInfo, github: e.target.value })
                        }
                        className="w-full px-4 py-2.5 rounded-xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all"
                      />
                    </div>
                  </div>
                </div>
              )}

              {/* STEP 2: Profil & Résumé */}
              {currentStep === 2 && (
                <div className="space-y-6">
                  <div>
                    <h3 className="text-lg font-bold text-[#142111]">
                      Accroche & Résumé Professionnel
                    </h3>
                    <p className="text-xs text-[#52634e] mt-1">
                      Une synthèse de 3 à 5 lignes qui résume vos points forts et votre valeur ajoutée. Les recruteurs et les systèmes ATS y accordent une importance primordiale.
                    </p>
                  </div>

                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <label className="text-xs font-bold text-[#23331f] uppercase tracking-wide">
                        Votre résumé professionnel
                      </label>
                      <span className="text-xs text-[#71826b]">
                        {generalInfo.summary.length} caractères
                      </span>
                    </div>
                    <textarea
                      rows={5}
                      placeholder="Présentez brièvement vos compétences clés, vos réalisations marquantes et ce que vous recherchez..."
                      value={generalInfo.summary}
                      onChange={(e) =>
                        setGeneralInfo({ ...generalInfo, summary: e.target.value })
                      }
                      className="w-full p-4 rounded-2xl border border-[#d7e3d3] bg-[#fdfefe] focus:bg-white text-sm text-[#142111] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none transition-all resize-y"
                    />
                  </div>

                  {/* Suggestions IA */}
                  <div className="rounded-2xl bg-[#f7fbf4] border border-[#dbe8d6] p-4">
                    <div className="flex items-center gap-2 mb-3 text-xs font-bold text-[#356b00] uppercase tracking-wider">
                      <Sparkles size={16} />
                      <span>Suggestions intelligentes TNCV (cliquez pour insérer)</span>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-2.5">
                      {SUMMARY_SUGGESTIONS.map((item, idx) => (
                        <button
                          key={idx}
                          type="button"
                          onClick={() =>
                            setGeneralInfo({ ...generalInfo, summary: item.text })
                          }
                          className="text-left p-3 rounded-xl bg-white border border-[#e1ebd9] hover:border-[#79b947] hover:shadow-2xs transition-all group cursor-pointer"
                        >
                          <span className="text-xs font-bold text-[#22351e] group-hover:text-[#356b00] block mb-1">
                            {item.title}
                          </span>
                          <span className="text-[11px] text-[#63765e] line-clamp-3 leading-relaxed">
                            {item.text}
                          </span>
                        </button>
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {/* STEP 3: Expériences */}
              {currentStep === 3 && (
                <div className="space-y-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="text-lg font-bold text-[#142111]">
                        Expériences Professionnelles
                      </h3>
                      <p className="text-xs text-[#52634e] mt-1">
                        Détaillez vos postes passés et actuels par ordre antéchronologique.
                      </p>
                    </div>
                    <button
                      type="button"
                      onClick={handleAddExperience}
                      className="inline-flex items-center gap-1.5 px-3 py-2 rounded-xl bg-[#edf7e7] hover:bg-[#dfeecd] text-[#2c5f16] border border-[#d2e8cb] text-xs font-bold transition-all cursor-pointer shadow-2xs"
                    >
                      <Plus size={16} />
                      <span>Ajouter un poste</span>
                    </button>
                  </div>

                  <div className="space-y-4">
                    {experiences.map((exp, index) => (
                      <div
                        key={index}
                        className="rounded-2xl border border-[#e2ece0] bg-[#fbfdfa] p-5 relative group"
                      >
                        <div className="flex items-center justify-between mb-4">
                          <span className="text-xs font-bold uppercase tracking-wider text-[#437920] bg-[#eef7ec] px-2.5 py-0.5 rounded-full border border-[#d6ebd0]">
                            Poste #{index + 1}
                          </span>
                          {experiences.length > 1 && (
                            <button
                              type="button"
                              onClick={() => handleRemoveExperience(index)}
                              className="text-red-500 hover:text-red-700 p-1 rounded-lg hover:bg-red-50 transition-colors cursor-pointer"
                              title="Supprimer ce poste"
                            >
                              <Trash2 size={16} />
                            </button>
                          )}
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Intitulé du poste
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: Développeur Backend Java"
                              value={exp.position}
                              onChange={(e) =>
                                handleUpdateExperience(index, "position", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Entreprise
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: Orange Labs, Société Générale..."
                              value={exp.company}
                              onChange={(e) =>
                                handleUpdateExperience(index, "company", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Lieu / Ville
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: Paris, France (ou Télétravail)"
                              value={exp.location}
                              onChange={(e) =>
                                handleUpdateExperience(index, "location", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div className="grid grid-cols-2 gap-2">
                            <div>
                              <label className="block text-xs font-bold text-[#23331f] mb-1">
                                Date début
                              </label>
                              <input
                                type="text"
                                placeholder="01/2022"
                                value={exp.startDate}
                                onChange={(e) =>
                                  handleUpdateExperience(index, "startDate", e.target.value)
                                }
                                className="w-full px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs focus:border-[#79b947] outline-none"
                              />
                            </div>
                            <div>
                              <label className="block text-xs font-bold text-[#23331f] mb-1">
                                Date fin
                              </label>
                              <input
                                type="text"
                                placeholder={exp.current ? "Actuel" : "12/2024"}
                                disabled={exp.current}
                                value={exp.current ? "Présent" : exp.endDate}
                                onChange={(e) =>
                                  handleUpdateExperience(index, "endDate", e.target.value)
                                }
                                className={`w-full px-3 py-2 rounded-xl border border-[#d7e3d3] text-xs outline-none ${
                                  exp.current ? "bg-slate-100 text-slate-400" : "bg-white"
                                }`}
                              />
                            </div>
                          </div>

                          <div className="sm:col-span-2 flex items-center gap-2 pt-1">
                            <input
                              type="checkbox"
                              id={`curr-${index}`}
                              checked={exp.current}
                              onChange={(e) =>
                                handleUpdateExperience(index, "current", e.target.checked)
                              }
                              className="w-4 h-4 rounded border-[#b8c9b2] text-[#4d8225] focus:ring-[#79b947] accent-[#4d8225] cursor-pointer"
                            />
                            <label
                              htmlFor={`curr-${index}`}
                              className="text-xs font-medium text-[#40523b] cursor-pointer"
                            >
                              J'occupe actuellement ce poste
                            </label>
                          </div>

                          <div className="sm:col-span-2">
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Missions et réalisations clés
                            </label>
                            <textarea
                              rows={3}
                              placeholder="• Développement d'APIs REST Spring Boot et intégration frontend React&#10;• Amélioration des temps de réponse de 35% grâce au cache Redis"
                              value={exp.description}
                              onChange={(e) =>
                                handleUpdateExperience(index, "description", e.target.value)
                              }
                              className="w-full p-3 rounded-xl border border-[#d7e3d3] bg-white text-xs text-[#142111] focus:border-[#79b947] outline-none"
                            />
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* STEP 4: Formations & Diplômes */}
              {currentStep === 4 && (
                <div className="space-y-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="text-lg font-bold text-[#142111]">
                        Formations & Diplômes
                      </h3>
                      <p className="text-xs text-[#52634e] mt-1">
                        Indiquez votre parcours académique, vos diplômes et certifications universitaires.
                      </p>
                    </div>
                    <button
                      type="button"
                      onClick={handleAddEducation}
                      className="inline-flex items-center gap-1.5 px-3 py-2 rounded-xl bg-[#edf7e7] hover:bg-[#dfeecd] text-[#2c5f16] border border-[#d2e8cb] text-xs font-bold transition-all cursor-pointer shadow-2xs"
                    >
                      <Plus size={16} />
                      <span>Ajouter un diplôme</span>
                    </button>
                  </div>

                  <div className="space-y-4">
                    {educations.map((edu, index) => (
                      <div
                        key={index}
                        className="rounded-2xl border border-[#e2ece0] bg-[#fbfdfa] p-5 relative group"
                      >
                        <div className="flex items-center justify-between mb-4">
                          <span className="text-xs font-bold uppercase tracking-wider text-[#437920] bg-[#eef7ec] px-2.5 py-0.5 rounded-full border border-[#d6ebd0]">
                            Formation #{index + 1}
                          </span>
                          {educations.length > 1 && (
                            <button
                              type="button"
                              onClick={() => handleRemoveEducation(index)}
                              className="text-red-500 hover:text-red-700 p-1 rounded-lg hover:bg-red-50 transition-colors cursor-pointer"
                              title="Supprimer cette formation"
                            >
                              <Trash2 size={16} />
                            </button>
                          )}
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Diplôme / Titre
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: Diplôme National d'Ingénieur en Informatique"
                              value={edu.degree}
                              onChange={(e) =>
                                handleUpdateEducation(index, "degree", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Établissement / Université
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: INSAT, ENSI, Université de Paris..."
                              value={edu.institution}
                              onChange={(e) =>
                                handleUpdateEducation(index, "institution", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div>
                            <label className="block text-xs font-bold text-[#23331f] mb-1">
                              Domaine d'études
                            </label>
                            <input
                              type="text"
                              placeholder="Ex: Génie Logiciel, Systèmes Distribués"
                              value={edu.fieldOfStudy}
                              onChange={(e) =>
                                handleUpdateEducation(index, "fieldOfStudy", e.target.value)
                              }
                              className="w-full px-3.5 py-2 rounded-xl border border-[#d7e3d3] bg-white text-sm focus:border-[#79b947] outline-none"
                            />
                          </div>

                          <div className="grid grid-cols-2 gap-2">
                            <div>
                              <label className="block text-xs font-bold text-[#23331f] mb-1">
                                Année début
                              </label>
                              <input
                                type="text"
                                placeholder="2019"
                                value={edu.startDate}
                                onChange={(e) =>
                                  handleUpdateEducation(index, "startDate", e.target.value)
                                }
                                className="w-full px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs focus:border-[#79b947] outline-none"
                              />
                            </div>
                            <div>
                              <label className="block text-xs font-bold text-[#23331f] mb-1">
                                Année fin
                              </label>
                              <input
                                type="text"
                                placeholder={edu.current ? "En cours" : "2024"}
                                disabled={edu.current}
                                value={edu.current ? "En cours" : edu.endDate}
                                onChange={(e) =>
                                  handleUpdateEducation(index, "endDate", e.target.value)
                                }
                                className={`w-full px-3 py-2 rounded-xl border border-[#d7e3d3] text-xs outline-none ${
                                  edu.current ? "bg-slate-100 text-slate-400" : "bg-white"
                                }`}
                              />
                            </div>
                          </div>

                          <div className="sm:col-span-2 flex items-center gap-2 pt-1">
                            <input
                              type="checkbox"
                              id={`edu-curr-${index}`}
                              checked={edu.current}
                              onChange={(e) =>
                                handleUpdateEducation(index, "current", e.target.checked)
                              }
                              className="w-4 h-4 rounded border-[#b8c9b2] text-[#4d8225] focus:ring-[#79b947] accent-[#4d8225] cursor-pointer"
                            />
                            <label
                              htmlFor={`edu-curr-${index}`}
                              className="text-xs font-medium text-[#40523b] cursor-pointer"
                            >
                              Formation en cours
                            </label>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* STEP 5: Compétences & Langues */}
              {currentStep === 5 && (
                <div className="space-y-7">
                  {/* Compétences */}
                  <div>
                    <h3 className="text-lg font-bold text-[#142111] mb-1">
                      Compétences Clés & Outils
                    </h3>
                    <p className="text-xs text-[#52634e] mb-4">
                      Les mots-clés techniques permettent aux filtres ATS de faire correspondre votre profil avec les offres d'emploi ciblées.
                    </p>

                    {/* Ajout rapide de compétences */}
                    <div className="mb-4">
                      <span className="text-xs font-bold text-[#356b00] uppercase tracking-wider block mb-2">
                        Suggestions populaires :
                      </span>
                      <div className="flex flex-wrap gap-1.5">
                        {POPULAR_SKILLS.map((sk) => (
                          <button
                            key={sk}
                            type="button"
                            onClick={() => handleQuickAddSkill(sk)}
                            className="px-2.5 py-1 rounded-lg text-xs font-medium bg-[#f0f7ec] hover:bg-[#dcf3d4] text-[#2c5f16] border border-[#d2e8cb] transition-colors cursor-pointer flex items-center gap-1"
                          >
                            <Plus size={12} />
                            <span>{sk}</span>
                          </button>
                        ))}
                      </div>
                    </div>

                    {/* Input ajout compétence */}
                    <div className="flex flex-col sm:flex-row gap-2 mb-4 p-3 rounded-2xl bg-[#f8fbf5] border border-[#e1ebd9]">
                      <input
                        type="text"
                        placeholder="Ex: Docker, Next.js, Kubernetes..."
                        value={newSkillName}
                        onChange={(e) => setNewSkillName(e.target.value)}
                        onKeyDown={(e) => {
                          if (e.key === "Enter") {
                            e.preventDefault();
                            handleAddSkill();
                          }
                        }}
                        className="flex-1 px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs outline-none focus:border-[#79b947]"
                      />
                      <select
                        value={newSkillCategory}
                        onChange={(e) => setNewSkillCategory(e.target.value)}
                        className="px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs outline-none"
                      >
                        <option value="Technique">Technique</option>
                        <option value="Frontend">Frontend</option>
                        <option value="Backend">Backend</option>
                        <option value="DevOps">DevOps</option>
                        <option value="Database">Database</option>
                        <option value="Soft Skills">Soft Skills</option>
                      </select>
                      <select
                        value={newSkillLevel}
                        onChange={(e) => setNewSkillLevel(e.target.value)}
                        className="px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs outline-none"
                      >
                        <option value="Débutant">Débutant</option>
                        <option value="Intermédiaire">Intermédiaire</option>
                        <option value="Avancé">Avancé</option>
                        <option value="Expert">Expert</option>
                      </select>
                      <button
                        type="button"
                        onClick={handleAddSkill}
                        className="px-4 py-2 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white text-xs font-semibold shrink-0 cursor-pointer flex items-center justify-center gap-1"
                      >
                        <Plus size={14} />
                        <span>Ajouter</span>
                      </button>
                    </div>

                    {/* Liste des compétences */}
                    <div className="flex flex-wrap gap-2">
                      {skills.map((sk, index) => (
                        <div
                          key={index}
                          className="inline-flex items-center gap-2 px-3 py-1.5 rounded-xl bg-white border border-[#d6e5d1] text-xs font-medium text-[#182615] shadow-2xs"
                        >
                          <span className="font-semibold">{sk.name}</span>
                          <span className="text-[10px] text-[#556750] px-1.5 py-0.2 bg-[#f0f6ec] rounded">
                            {sk.level}
                          </span>
                          <button
                            type="button"
                            onClick={() => handleRemoveSkill(index)}
                            className="text-[#899c82] hover:text-red-500 cursor-pointer"
                          >
                            <X size={13} />
                          </button>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Langues */}
                  <div className="pt-4 border-t border-[#e8efe4]">
                    <div className="flex items-center gap-2 mb-2">
                      <LanguagesIcon size={18} className="text-[#356b00]" />
                      <h4 className="text-base font-bold text-[#142111]">
                        Langues maîtrisées
                      </h4>
                    </div>

                    <div className="flex flex-col sm:flex-row gap-2 mb-4 p-3 rounded-2xl bg-[#f8fbf5] border border-[#e1ebd9]">
                      <input
                        type="text"
                        placeholder="Ex: Français, Anglais, Allemand..."
                        value={newLangName}
                        onChange={(e) => setNewLangName(e.target.value)}
                        onKeyDown={(e) => {
                          if (e.key === "Enter") {
                            e.preventDefault();
                            handleAddLanguage();
                          }
                        }}
                        className="flex-1 px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs outline-none focus:border-[#79b947]"
                      />
                      <select
                        value={newLangLevel}
                        onChange={(e) => setNewLangLevel(e.target.value)}
                        className="px-3 py-2 rounded-xl border border-[#d7e3d3] bg-white text-xs outline-none"
                      >
                        <option value="Langue maternelle">Langue maternelle</option>
                        <option value="Bilingue">Bilingue</option>
                        <option value="Courant (C1/C2)">Courant (C1/C2)</option>
                        <option value="Professionnel (B2)">Professionnel (B2)</option>
                        <option value="Intermédiaire (B1)">Intermédiaire (B1)</option>
                        <option value="Notions (A2)">Notions (A2)</option>
                      </select>
                      <button
                        type="button"
                        onClick={handleAddLanguage}
                        className="px-4 py-2 rounded-xl bg-[#346b1d] hover:bg-[#285515] text-white text-xs font-semibold shrink-0 cursor-pointer flex items-center justify-center gap-1"
                      >
                        <Plus size={14} />
                        <span>Ajouter langue</span>
                      </button>
                    </div>

                    <div className="flex flex-wrap gap-2">
                      {languages.map((lg, index) => (
                        <div
                          key={index}
                          className="inline-flex items-center gap-2 px-3 py-1.5 rounded-xl bg-white border border-[#d6e5d1] text-xs font-medium text-[#182615] shadow-2xs"
                        >
                          <span className="font-semibold">{lg.name}</span>
                          <span className="text-[10px] text-[#346b1d] font-semibold bg-[#eef7ec] px-1.5 py-0.5 rounded">
                            {lg.level}
                          </span>
                          <button
                            type="button"
                            onClick={() => handleRemoveLanguage(index)}
                            className="text-[#899c82] hover:text-red-500 cursor-pointer"
                          >
                            <X size={13} />
                          </button>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {/* STEP 6: Récapitulatif & Finalisation */}
              {currentStep === 6 && (
                <div className="space-y-6">
                  <div>
                    <h3 className="text-lg font-bold text-[#142111]">
                      Révision & Enregistrement
                    </h3>
                    <p className="text-xs text-[#52634e] mt-1">
                      Vérifiez les données de votre CV avant de lancer la création et la synchronisation avec le serveur.
                    </p>
                  </div>

                  {/* Template Picker in Review */}
                  <div className="p-4 rounded-2xl bg-[#f7fbf4] border border-[#dbe8d6]">
                    <span className="text-xs font-bold uppercase tracking-wider text-[#356b00] block mb-2">
                      Modèle graphique sélectionné
                    </span>
                    <div className="grid grid-cols-3 gap-3">
                      {(["moderne", "tech", "minimal"] as const).map((tpl) => (
                        <button
                          key={tpl}
                          type="button"
                          onClick={() => setSelectedTemplate(tpl)}
                          className={`p-3 rounded-xl border text-center transition-all cursor-pointer ${
                            selectedTemplate === tpl
                              ? "bg-white border-[#79b947] ring-2 ring-[#79b947]/20 shadow-xs"
                              : "bg-white/60 border-[#e1ebd9] hover:bg-white"
                          }`}
                        >
                          <span className="text-xs font-bold capitalize text-[#192816] block">
                            {tpl === "moderne"
                              ? "Moderne Épuré"
                              : tpl === "tech"
                              ? "Exécutif Tech"
                              : "Minimaliste Chic"}
                          </span>
                          <span className="text-[10px] text-[#657660]">
                            {tpl === "moderne" ? "Format 2 colonnes" : tpl === "tech" ? "Format Tech sidebar" : "1 colonne classique"}
                          </span>
                        </button>
                      ))}
                    </div>
                  </div>

                  {/* Summary Card */}
                  <div className="rounded-2xl border border-[#e2ece0] bg-white p-5 shadow-xs space-y-4">
                    <div className="flex items-center justify-between pb-3 border-b border-[#eef4ec]">
                      <div>
                        <h4 className="text-base font-bold text-[#142111]">
                          {generalInfo.title || "CV sans titre"}
                        </h4>
                        <p className="text-xs text-[#52634e]">
                          {generalInfo.fullName} • {generalInfo.email} {generalInfo.phone ? `• ${generalInfo.phone}` : ""}
                        </p>
                      </div>
                      <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-[#edf7e7] text-[#2c5f16] border border-[#cfe9c6]">
                        Prêt pour export ATS
                      </span>
                    </div>

                    <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
                      <div className="p-3 rounded-xl bg-[#f8fbf5] border border-[#e4eee0]">
                        <span className="text-lg font-bold text-[#142111]">
                          {experiences.filter((e) => e.company).length}
                        </span>
                        <span className="text-[11px] text-[#657660] block">Expériences</span>
                      </div>
                      <div className="p-3 rounded-xl bg-[#f8fbf5] border border-[#e4eee0]">
                        <span className="text-lg font-bold text-[#142111]">
                          {educations.filter((e) => e.institution).length}
                        </span>
                        <span className="text-[11px] text-[#657660] block">Formations</span>
                      </div>
                      <div className="p-3 rounded-xl bg-[#f8fbf5] border border-[#e4eee0]">
                        <span className="text-lg font-bold text-[#142111]">
                          {skills.length}
                        </span>
                        <span className="text-[11px] text-[#657660] block">Compétences</span>
                      </div>
                      <div className="p-3 rounded-xl bg-[#f8fbf5] border border-[#e4eee0]">
                        <span className="text-lg font-bold text-[#142111]">
                          {languages.length}
                        </span>
                        <span className="text-[11px] text-[#657660] block">Langues</span>
                      </div>
                    </div>

                    {generalInfo.summary && (
                      <div className="pt-2">
                        <span className="text-xs font-bold text-[#356b00] uppercase tracking-wide block mb-1">
                          Accroche :
                        </span>
                        <p className="text-xs text-[#4a5845] leading-relaxed italic bg-[#fbfdf9] p-3 rounded-xl border border-[#e9f2e6]">
                          "{generalInfo.summary}"
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              )}
            </>
          )}
        </div>

        {/* Modal Bottom Actions */}
        {!createdCv && (
          <div className="px-6 py-4 border-t border-[#e5eee2] bg-[#f8fbf5] flex items-center justify-between shrink-0">
            {currentStep > 1 ? (
              <button
                type="button"
                onClick={handlePrev}
                disabled={isSubmitting}
                className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl border border-[#d2ded0] bg-white hover:bg-[#edf3eb] text-[#3b4c37] text-xs font-semibold transition-all cursor-pointer"
              >
                <ChevronLeft size={16} />
                <span>Précédent</span>
              </button>
            ) : (
              <button
                type="button"
                onClick={onClose}
                disabled={isSubmitting}
                className="px-4 py-2.5 rounded-xl border border-[#d2ded0] bg-white hover:bg-[#edf3eb] text-[#3b4c37] text-xs font-semibold transition-all cursor-pointer"
              >
                Annuler
              </button>
            )}

            <div className="flex items-center gap-3">
              {currentStep < 6 ? (
                <button
                  type="button"
                  onClick={handleNext}
                  className="inline-flex items-center gap-1.5 px-5 py-2.5 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white text-xs font-semibold shadow-xs transition-all cursor-pointer"
                >
                  <span>Suivant : {steps[currentStep].label}</span>
                  <ChevronRight size={16} />
                </button>
              ) : (
                <button
                  type="button"
                  onClick={handleSubmitCv}
                  disabled={isSubmitting}
                  className="inline-flex items-center gap-2 px-6 py-3 rounded-xl bg-[#245217] hover:bg-[#1a3c10] text-white text-xs font-bold shadow-md hover:shadow-lg transition-all active:scale-[0.99] cursor-pointer disabled:opacity-70"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 size={16} className="animate-spin" />
                      <span>{submissionProgress || "Enregistrement en cours..."}</span>
                    </>
                  ) : (
                    <>
                      <Check size={16} />
                      <span>Créer et enregistrer mon CV</span>
                    </>
                  )}
                </button>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
