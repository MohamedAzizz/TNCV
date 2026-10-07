import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { cvService } from "../../services/cvService";
import type { Cv } from "../../types/cv";
import { CvCreateModal } from "../../components/cv/CvCreateModal";
import tncvLogo from "../../assets/images/tncv_logo.png";

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  // State for user's CVs
  const [cvs, setCvs] = useState<Cv[]>([]);
  const [loadingCvs, setLoadingCvs] = useState(true);

  // Modal creation state
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [selectedTemplate, setSelectedTemplate] = useState<"moderne" | "tech" | "minimal">("moderne");

  // File upload state for Option 01 (Importer un CV existant)
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [isDragOver, setIsDragOver] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  // ATS Modal state (for Analyser mon CV / Guide ATS)
  const [atsModalOpen, setAtsModalOpen] = useState(false);
  const [atsModalContent, setAtsModalContent] = useState<{
    title: string;
    score: number;
    points: string[];
  } | null>(null);

  // Search state
  const [searchQuery, setSearchQuery] = useState("");

  // Mobile sidebar toggle
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  // Notification toast
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  // Load user CVs on mount
  const loadCvs = async () => {
    try {
      setLoadingCvs(true);
      const data = await cvService.getMyCvs();
      setCvs(data || []);
    } catch (error) {
      console.error("Erreur chargement des CVs:", error);
    } finally {
      setLoadingCvs(false);
    }
  };

  useEffect(() => {
    loadCvs();
  }, []);

  // Handle file selection
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setUploadedFile(file);
      showToast(`Fichier "${file.name}" chargé pour l'analyse IA !`);
    }
  };

  // Drag and drop handlers
  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = () => {
    setIsDragOver(false);
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragOver(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0];
      setUploadedFile(file);
      showToast(`Fichier "${file.name}" déposé avec succès !`);
    }
  };

  // Click on "Analyser mon CV"
  const handleAnalyzeCv = () => {
    if (!uploadedFile) {
      fileInputRef.current?.click();
    } else {
      setAtsModalContent({
        title: `Audit ATS : ${uploadedFile.name}`,
        score: 91,
        points: [
          "Format vectoriel et typographie 100% compatibles avec Workday, Taleo et Greenhouse.",
          "Mots-clés détectés en adéquation avec les postes cibles du marché tunisien & international.",
          "Rubriques Expériences et Compétences bien structurées sans tableaux complexes bloquants.",
          "Recommandation IA : Détaillez davantage vos réalisations avec des données chiffrées (% ou métriques).",
        ],
      });
      setAtsModalOpen(true);
    }
  };

  // Open creation modal
  const handleOpenCreateModal = (template?: "moderne" | "tech" | "minimal") => {
    if (template) setSelectedTemplate(template);
    setIsCreateModalOpen(true);
  };

  // Handle successful CV creation from modal
  const handleCvCreated = (_newCv: Cv) => {
    loadCvs();
    showToast("Votre nouveau CV a été créé et synchronisé avec succès !");
  };

  // Delete CV
  const handleDeleteCv = async (id?: number) => {
    if (!id) return;
    if (window.confirm("Êtes-vous sûr de vouloir supprimer ce CV ?")) {
      try {
        await cvService.deleteCv(id);
        setCvs((prev) => prev.filter((c) => c.id !== id));
        showToast("CV supprimé avec succès.");
      } catch (err) {
        console.error(err);
        alert("Erreur lors de la suppression.");
      }
    }
  };

  // User initials & display name
  const userName = user?.firstName
    ? `${user.firstName} ${user.lastName ? user.lastName[0] + "." : ""}`
    : "Alexandre D.";
  const userInitials = user?.firstName
    ? `${user.firstName[0]}${user.lastName ? user.lastName[0] : ""}`.toUpperCase()
    : "AD";

  // Filtered CVs for search
  const filteredCvs = cvs.filter(
    (c) =>
      c.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      c.fullName.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="bg-[#fbfcfe] font-sans text-[#131b2e] antialiased selection:bg-[#edf7e7] selection:text-[#245217] min-h-screen">
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed bottom-6 right-6 z-50 flex items-center gap-3 px-5 py-3.5 rounded-2xl bg-[#142111] text-white shadow-xl border border-[#2b4c1f] text-sm animate-fade-in">
          <span className="material-symbols-outlined text-[#79b947] text-[20px]">
            check_circle
          </span>
          <span>{toastMessage}</span>
        </div>
      )}

      {/* SIDEBAR NAVIGATION INSPIRED BY REFERENCE */}
      <aside
        className={`fixed top-0 left-0 bottom-0 w-64 bg-white border-r border-[#e2ece0] z-50 flex flex-col justify-between p-5 shadow-xs overflow-y-auto transition-transform duration-300 ${
          mobileMenuOpen ? "translate-x-0" : "-translate-x-full lg:translate-x-0"
        }`}
      >
        <div className="flex flex-col gap-6">
          {/* Brand Header with TNCV Logo */}
          <div className="flex items-center gap-3 px-2 py-1">
            <div className="h-10 w-10 flex items-center justify-center p-1 rounded-xl bg-gradient-to-br from-amber-50 to-amber-100/60 border border-amber-200/50 shadow-xs shrink-0">
              <img
                alt="TNCV Logo"
                className="h-7 w-auto object-contain"
                src={tncvLogo}
              />
            </div>
            <div className="flex flex-col">
              <div className="flex items-center gap-1.5">
                <span className="font-bold text-lg tracking-tight text-[#172213]">
                  TNCV
                </span>
                <span className="inline-flex items-center px-1.5 py-0.5 rounded-full text-[10px] font-semibold bg-[#e8f5e3] text-[#346b1d] border border-[#cfe9c6]">
                  AI Suite
                </span>
              </div>
              <span className="text-[11px] text-[#657660]">
                Plateforme Carrière
              </span>
            </div>
          </div>

          {/* Category 1: NAVIGATION PRINCIPALE */}
          <div className="flex flex-col gap-1.5">
            <span className="text-[11px] font-bold uppercase tracking-wider text-[#8b9c84] px-3 mb-1">
              Menu Principal
            </span>

            {/* Accueil (Active) */}
            <Link
              to="/dashboard"
              className="flex items-center justify-between px-3 py-2.5 rounded-xl bg-[#edf7e7] text-[#245217] font-semibold text-sm border border-[#d9edd0] shadow-2xs transition-all"
            >
              <div className="flex items-center gap-3">
                <span
                  className="material-symbols-outlined text-[20px] text-[#356b00]"
                  style={{ fontVariationSettings: "'FILL' 1" }}
                >
                  dashboard
                </span>
                <span>Accueil</span>
              </div>
              <span className="w-1.5 h-1.5 rounded-full bg-[#79b947]"></span>
            </Link>

            {/* Mes CVs */}
            <Link
              to="/cvs"
              className="flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  folder_open
                </span>
                <span>Mes CVs</span>
              </div>
              <span className="px-2 py-0.5 text-[11px] font-bold rounded-full bg-[#79b947] text-white shadow-2xs">
                {cvs.length}
              </span>
            </Link>

            {/* Créateur de CV */}
            <button
              onClick={() => handleOpenCreateModal()}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  auto_fix_high
                </span>
                <span>Créateur de CV</span>
              </div>
              <span className="text-[10px] font-bold px-1.5 py-0.5 rounded-full bg-[#e3f4dc] text-[#3b721e] border border-[#cce8c2]">
                IA
              </span>
            </button>

            {/* Optimisation ATS */}
            <button
              onClick={() => {
                setAtsModalContent({
                  title: "Optimisation & Conformité ATS",
                  score: 88,
                  points: [
                    "Analyse syntaxique en temps réel des sections de vos CV.",
                    "Détection automatique des compétences recherchées par les recruteurs du secteur IT et management.",
                    "Structure recommandée : En-tête épuré, Profil, Expériences avec métriques, Formation, Compétences classées.",
                    "Élimination des artefacts graphiques complexes qui bloquent les logiciels ATS.",
                  ],
                });
                setAtsModalOpen(true);
              }}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  verified
                </span>
                <span>Optimisation ATS</span>
              </div>
            </button>

            {/* Offres d'emploi */}
            <button
              onClick={() => showToast("Module Offres d'emploi en cours de synchronisation avec les partenaires TNCV.")}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  work_outline
                </span>
                <span>Offres d'emploi</span>
              </div>
              <span className="px-2 py-0.5 text-[11px] font-bold rounded-full bg-[#e8f5e3] text-[#346b1d] border border-[#cfe9c6]">
                5
              </span>
            </button>
          </div>

          {/* Category 2: OUTILS & GESTION */}
          <div className="flex flex-col gap-1.5 pt-2">
            <span className="text-[11px] font-bold uppercase tracking-wider text-[#8b9c84] px-3 mb-1">
              Outils &amp; Gestion
            </span>

            <button
              onClick={() => {
                const el = document.getElementById("template-selector-section");
                if (el) el.scrollIntoView({ behavior: "smooth" });
              }}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  layers
                </span>
                <span>Modèles de CV</span>
              </div>
            </button>

            <button
              onClick={() => showToast("Historique de 8 candidatures actives suivi par le tracker TNCV.")}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  inventory_2
                </span>
                <span>Candidatures envoyées</span>
              </div>
              <span className="px-2 py-0.5 text-[11px] font-bold rounded-full bg-[#fbf5dc] text-[#87650f] border border-[#f3e4ad]">
                8
              </span>
            </button>

            <button
              onClick={() => {
                setAtsModalContent({
                  title: "Statistiques Globales ATS",
                  score: 88,
                  points: [
                    "Score moyen calculé sur l'ensemble de vos CVs : 88%.",
                    "Taux de lisibilité machine : 94% de réussite aux tests ATS.",
                    "Temps moyen de consultation par les recruteurs partenaires : 45 secondes.",
                    "3 CVs prêts pour l'exportation et les candidatures directes.",
                  ],
                });
                setAtsModalOpen(true);
              }}
              className="w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
                  insights
                </span>
                <span>Statistiques ATS</span>
              </div>
            </button>
          </div>
        </div>

        {/* Bottom Section: COMPTE & ADMIN */}
        <div className="flex flex-col gap-1.5 pt-4 border-t border-[#e5eee2]">
          <span className="text-[11px] font-bold uppercase tracking-wider text-[#8b9c84] px-3 mb-1">
            Compte &amp; Paramètres
          </span>

          <button
            onClick={() => showToast(`Connecté avec l'adresse : ${user?.email || "alexandre.d@example.com"}`)}
            className="flex items-center gap-3 px-3 py-2 rounded-xl text-[#4a5845] hover:text-[#172213] hover:bg-[#f4f7f2] font-medium text-sm transition-all group text-left cursor-pointer"
          >
            <span className="material-symbols-outlined text-[20px] text-[#71826b] group-hover:text-[#356b00]">
              settings
            </span>
            <span>Paramètres du compte</span>
          </button>

          {/* User profile capsule */}
          <div className="flex items-center justify-between px-3 py-2.5 rounded-xl bg-[#f8fbf5] border border-[#e4ede1] my-1">
            <div className="flex items-center gap-2.5 min-w-0">
              <div className="relative flex items-center justify-center shrink-0">
                <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-[#245217] to-[#4d8225] text-white flex items-center justify-center font-semibold text-xs shadow-xs">
                  {userInitials}
                </div>
                <span className="absolute bottom-0 right-0 w-2 h-2 rounded-full bg-[#79b947] ring-2 ring-white"></span>
              </div>
              <div className="flex flex-col leading-tight min-w-0">
                <span className="text-xs font-semibold text-[#1a2617] truncate">
                  {userName}
                </span>
                <span className="text-[10px] text-[#42791f] font-semibold">
                  Compte Pro
                </span>
              </div>
            </div>
            <button
              onClick={() => showToast(`Session active : ${user?.email || "Pro"}`)}
              className="text-[#71826b] hover:text-[#172213] p-1 cursor-pointer"
              title="Options profil"
            >
              <span className="material-symbols-outlined text-[18px]">
                expand_more
              </span>
            </button>
          </div>

          {/* Déconnexion */}
          <button
            onClick={() => {
              logout();
              navigate("/signin");
            }}
            className="flex items-center gap-3 px-3 py-2 rounded-xl text-[#ba1a1a] hover:bg-[#ffdad6]/40 font-medium text-sm transition-all group text-left cursor-pointer"
          >
            <span className="material-symbols-outlined text-[20px] text-[#ba1a1a]">
              logout
            </span>
            <span>Déconnexion</span>
          </button>
        </div>
      </aside>

      {/* Mobile Overlay */}
      {mobileMenuOpen && (
        <div
          onClick={() => setMobileMenuOpen(false)}
          className="fixed inset-0 bg-black/40 z-40 lg:hidden backdrop-blur-xs"
        />
      )}

      {/* TOPBAR HEADER WITH SEARCH & QUICK CONTROLS */}
      <header className="fixed top-0 left-0 lg:left-64 right-0 z-40 bg-white/90 backdrop-blur-md border-b border-[#e5ece1] h-18 px-4 sm:px-6 lg:px-8 flex items-center justify-between">
        {/* Mobile menu button */}
        <button
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          className="lg:hidden p-2 rounded-xl text-[#4d5b47] hover:bg-[#f2f6ee] mr-2"
          aria-label="Ouvrir le menu"
        >
          <span className="material-symbols-outlined text-[24px]">menu</span>
        </button>

        {/* Search bar */}
        <div className="relative flex items-center flex-1 max-w-md group">
          <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-[#71826b] group-focus-within:text-[#4d8225] transition-colors">
            <span className="material-symbols-outlined text-[20px]">search</span>
          </div>
          <input
            className="w-full pl-10 pr-14 py-2 bg-[#f4f7f1] hover:bg-[#edf2e9] focus:bg-white text-[#192416] placeholder:text-[#7f8f7a] text-sm rounded-full transition-all duration-200 border border-[#e1e9dd] focus:border-[#79b947] focus:ring-2 focus:ring-[#79b947]/20 outline-none"
            placeholder="Rechercher CV, poste, mot-clé..."
            type="search"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <div className="absolute inset-y-0 right-0 pr-2.5 flex items-center pointer-events-none">
            <kbd className="px-2 py-0.5 text-[11px] font-medium text-[#657760] bg-white rounded-md border border-[#d5ded1] shadow-2xs">
              ⌘K
            </kbd>
          </div>
        </div>

        {/* Quick controls */}
        <div className="flex items-center gap-2 sm:gap-3 shrink-0">
          <button
            onClick={() => showToast("Vous avez 3 notifications importantes sur l'optimisation de vos CVs.")}
            aria-label="Notifications"
            className="relative p-2.5 rounded-full text-[#4d5b47] hover:bg-[#f2f6ee] hover:text-[#1b2716] transition-colors cursor-pointer"
          >
            <span className="material-symbols-outlined text-[21px]">
              notifications
            </span>
            <span className="absolute top-2 right-2 flex h-2.5 w-2.5">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-[#79b947] opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-[#4d8225] ring-2 ring-white"></span>
            </span>
          </button>

          <button
            onClick={() => {
              setAtsModalContent({
                title: "Centre d'Aide & Guide TNCV",
                score: 100,
                points: [
                  "1. Création de CV : Utilisez l'assistant guidé pour remplir vos sections pas à pas.",
                  "2. Importation : Déposez votre CV au format PDF ou DOCX pour auditer sa conformité ATS.",
                  "3. Optimisation : Nos algorithmes mettent en avant les verbes d'action et mots-clés pertinents.",
                  "4. Export : Vos CVs sont stockés en base sécurisée et téléchargeables en PDF vectoriel.",
                ],
              });
              setAtsModalOpen(true);
            }}
            aria-label="Aide et Support"
            className="p-2.5 rounded-full text-[#4d5b47] hover:bg-[#f2f6ee] hover:text-[#1b2716] transition-colors cursor-pointer"
          >
            <span className="material-symbols-outlined text-[21px]">
              help_outline
            </span>
          </button>
        </div>
      </header>

      {/* MAIN CONTENT AREA */}
      <main className="w-full pt-24 pb-16 pl-4 pr-4 lg:pl-72 lg:pr-8 max-w-[1550px] mx-auto">
        <div className="flex flex-col w-full gap-8">
          {/* 2. BANNIÈRE DE BIENVENUE & STATS (HERO SECTION) */}
          <div className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-white via-[#f7fbf4] to-[#f0f7ec] border border-[#e4ece0] p-6 sm:p-8 lg:p-10 shadow-xs">
            {/* Glow ambient background accents */}
            <div className="absolute -right-16 -top-24 h-80 w-80 rounded-full bg-[#79b947]/12 blur-3xl pointer-events-none"></div>
            <div className="absolute right-1/4 -bottom-20 h-64 w-64 rounded-full bg-[#f3cf7a]/15 blur-2xl pointer-events-none"></div>

            <div className="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-8">
              {/* Title & Subtitle */}
              <div className="max-w-2xl flex flex-col items-start">
                <div className="inline-flex items-center gap-2 rounded-full bg-white/90 border border-[#d6e5d1] px-3.5 py-1.5 text-[#3b721e] shadow-2xs mb-3">
                  <span className="relative flex h-2 w-2">
                    <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-[#79b947] opacity-75"></span>
                    <span className="relative inline-flex rounded-full h-2 w-2 bg-[#4d8225]"></span>
                  </span>
                  <span className="text-xs font-bold tracking-wider uppercase text-[#35681c]">
                    Tableau de bord intelligent
                  </span>
                </div>
                <h1 className="text-3xl sm:text-4xl lg:text-[40px] font-extrabold text-[#142111] tracking-tight leading-tight">
                  Bienvenue sur TNCV, {user?.firstName || "Alex"}{" "}
                  <span className="inline-block transform hover:rotate-12 transition-transform duration-200">
                    👋
                  </span>
                </h1>
                <p className="mt-3 text-base sm:text-lg text-[#4a5845] leading-relaxed">
                  Propulsez votre carrière avec des CV percutants optimisés pour les recruteurs et les systèmes ATS.
                </p>
              </div>

              {/* 3 Modern Stats Cards */}
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-3.5 shrink-0 w-full lg:w-auto">
                {/* Stat 1: CVs */}
                <div className="flex items-center gap-3.5 rounded-2xl bg-white/95 border border-[#e2ece0] px-4 py-3.5 shadow-2xs hover:shadow-sm hover:border-[#cadcc5] transition-all">
                  <div className="w-12 h-12 rounded-xl bg-[#edf7e7] border border-[#d5ebd0] flex items-center justify-center shrink-0 text-[#437d22]">
                    <span className="material-symbols-outlined text-[24px]">
                      description
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-2xl font-bold text-[#142111] leading-none">
                      {loadingCvs ? "..." : cvs.length}
                    </span>
                    <span className="text-xs font-medium text-[#657660] mt-1">
                      CVs actifs
                    </span>
                  </div>
                </div>

                {/* Stat 2: Score ATS */}
                <div className="flex items-center gap-3.5 rounded-2xl bg-white/95 border border-[#e2ece0] px-4 py-3.5 shadow-2xs hover:shadow-sm hover:border-[#cadcc5] transition-all">
                  <div className="relative w-12 h-12 rounded-xl bg-[#edf7e7] border border-[#d5ebd0] flex items-center justify-center shrink-0">
                    {/* Mini circular progress SVG */}
                    <svg className="w-9 h-9 transform -rotate-90" viewBox="0 0 36 36">
                      <path
                        className="text-[#d8ebd2]"
                        d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="3.5"
                      ></path>
                      <path
                        className="text-[#4d8225]"
                        d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                        fill="none"
                        stroke="currentColor"
                        strokeDasharray="88, 100"
                        strokeLinecap="round"
                        strokeWidth="3.5"
                      ></path>
                    </svg>
                    <span
                      className="material-symbols-outlined text-[18px] text-[#346b1d] absolute"
                      style={{ fontVariationSettings: "'FILL' 1" }}
                    >
                      speed
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <div className="flex items-baseline gap-1">
                      <span className="text-2xl font-bold text-[#30651a] leading-none">
                        88%
                      </span>
                      <span className="text-[10px] font-bold text-[#5c8a49] tracking-wider uppercase">
                        ATS
                      </span>
                    </div>
                    <span className="text-xs font-medium text-[#657660] mt-1">
                      Score moyen
                    </span>
                  </div>
                </div>

                {/* Stat 3: Offres cibles */}
                <div className="flex items-center gap-3.5 rounded-2xl bg-white/95 border border-[#e2ece0] px-4 py-3.5 shadow-2xs hover:shadow-sm hover:border-[#cadcc5] transition-all">
                  <div className="w-12 h-12 rounded-xl bg-[#f2f6ee] border border-[#dbe6d6] flex items-center justify-center shrink-0 text-[#4c5c46]">
                    <span className="material-symbols-outlined text-[24px]">
                      target
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-2xl font-bold text-[#142111] leading-none">
                      5
                    </span>
                    <span className="text-xs font-medium text-[#657660] mt-1">
                      Offres cibles
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* 3. LES DEUX GRANDES PARTIES CENTRALES (LES ACTIONS MAJEURES) */}
          <section className="grid grid-cols-1 lg:grid-cols-2 gap-7 items-stretch">
            {/* PARTIE 1 : Importer un CV existant */}
            <div className="flex flex-col justify-between rounded-3xl bg-white border border-[#e2ece0] p-7 sm:p-8 shadow-sm hover:shadow-md hover:border-[#cfdecb] transition-all duration-300">
              <div className="flex flex-col">
                {/* Top Tags */}
                <div className="flex items-center justify-between gap-2">
                  <span className="inline-flex items-center gap-1.5 rounded-full bg-[#edf7e7] border border-[#d7ecd1] px-3 py-1 text-xs font-semibold text-[#3b721e]">
                    <span className="material-symbols-outlined text-[15px]">
                      bolt
                    </span>
                    Rapide &amp; Intelligent • Analyse IA
                  </span>
                  <span className="text-xs font-semibold uppercase tracking-wider text-[#8b9c84]">
                    Option 01
                  </span>
                </div>

                {/* Title & Icon */}
                <div className="mt-5 flex items-start gap-4">
                  <div className="w-13 h-13 rounded-2xl bg-gradient-to-tr from-[#edf7e7] to-[#e0f1d8] border border-[#cee5c6] flex items-center justify-center shrink-0 text-[#3d7722] shadow-2xs">
                    <span className="material-symbols-outlined text-[30px]">
                      cloud_upload
                    </span>
                  </div>
                  <div>
                    <h2 className="text-xl sm:text-2xl font-bold text-[#142111] tracking-tight">
                      Importer un CV existant
                    </h2>
                    <p className="text-sm text-[#50604c] mt-1 leading-normal">
                      Évaluez votre score ATS et générez des axes d'optimisation en 10 secondes.
                    </p>
                  </div>
                </div>

                {/* Drag & Drop Zone */}
                <div
                  id="dropzone-area"
                  onClick={() => fileInputRef.current?.click()}
                  onDragOver={handleDragOver}
                  onDragLeave={handleDragLeave}
                  onDrop={handleDrop}
                  className={`group relative mt-6 flex flex-col items-center justify-center rounded-2xl border-2 border-dashed p-7 text-center transition-all duration-200 cursor-pointer shadow-inner ${
                    isDragOver
                      ? "border-[#79b947] bg-[#edf7e7]"
                      : "border-[#b6ccae] hover:border-[#79b947] bg-[#f8fbf5] hover:bg-[#f1f8ee]"
                  }`}
                >
                  <input
                    ref={fileInputRef}
                    accept=".pdf,.docx,.txt"
                    className="hidden"
                    id="cv-upload-input"
                    type="file"
                    onChange={handleFileChange}
                  />

                  {uploadedFile ? (
                    <div className="flex items-center justify-center gap-3.5 py-2">
                      <div className="w-12 h-12 rounded-xl bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#2b6415] shadow-xs">
                        <span className="material-symbols-outlined text-[26px]">
                          task_alt
                        </span>
                      </div>
                      <div className="text-left">
                        <div className="text-sm font-bold text-[#142111] max-w-[220px] truncate">
                          {uploadedFile.name}
                        </div>
                        <div className="text-xs text-[#52634e] mt-0.5">
                          {(uploadedFile.size / 1024 / 1024).toFixed(2)} Mo • Prêt pour analyse IA immédiate
                        </div>
                      </div>
                    </div>
                  ) : (
                    <>
                      <div className="w-14 h-14 rounded-2xl bg-white border border-[#dce9d7] text-[#4d8225] flex items-center justify-center shadow-xs mb-3 group-hover:scale-105 group-hover:bg-[#edf7e7] transition-all">
                        <span className="material-symbols-outlined text-[28px]">
                          upload_file
                        </span>
                      </div>
                      <p className="text-base font-semibold text-[#182615]">
                        Glissez-déposez votre CV ici
                      </p>
                      <p className="text-sm text-[#556750] mt-0.5">
                        ou{" "}
                        <span className="text-[#3b721e] font-semibold underline underline-offset-2 decoration-[#79b947]">
                          parcourir vos fichiers
                        </span>
                      </p>
                      <div className="mt-4 inline-flex items-center gap-2 rounded-full bg-white px-3.5 py-1 text-[#677962] text-xs font-medium border border-[#e1ebdc] shadow-2xs">
                        <span className="material-symbols-outlined text-[15px] text-[#4d8225]">
                          task_alt
                        </span>
                        <span>PDF • DOCX • Jusqu'à 10 Mo</span>
                      </div>
                    </>
                  )}
                </div>

                {/* Checklist */}
                <div className="mt-6 space-y-3">
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Extraction automatique des compétences clés et parcours</span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Audit de lisibilité et conformité ATS instantané</span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Suggestions ciblées basées sur les mots-clés du secteur</span>
                  </div>
                </div>
              </div>

              {/* Action Button */}
              <div className="mt-8 pt-2">
                <button
                  id="import-btn"
                  onClick={handleAnalyzeCv}
                  className="w-full h-12 flex items-center justify-center gap-2.5 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white font-semibold text-sm shadow-[0_4px_14px_rgba(121,185,71,0.35)] hover:shadow-[0_6px_20px_rgba(121,185,71,0.45)] transition-all duration-200 active:scale-[0.99] cursor-pointer"
                >
                  <span>Analyser mon CV</span>
                  <span className="material-symbols-outlined text-[18px]">
                    arrow_forward
                  </span>
                </button>
              </div>
            </div>

            {/* PARTIE 2 : Créer un nouveau CV */}
            <div
              id="template-selector-section"
              className="flex flex-col justify-between rounded-3xl bg-white border border-[#e2ece0] p-7 sm:p-8 shadow-sm hover:shadow-md hover:border-[#cfdecb] transition-all duration-300"
            >
              <div className="flex flex-col">
                {/* Top Tags */}
                <div className="flex items-center justify-between gap-2">
                  <span className="inline-flex items-center gap-1.5 rounded-full bg-[#fbf5dc] border border-[#f3e4ad] px-3 py-1 text-xs font-semibold text-[#87650f]">
                    <span
                      className="material-symbols-outlined text-[15px]"
                      style={{ fontVariationSettings: "'FILL' 1" }}
                    >
                      star
                    </span>
                    Recommandé • Assisté par IA
                  </span>
                  <span className="text-xs font-semibold uppercase tracking-wider text-[#8b9c84]">
                    Option 02
                  </span>
                </div>

                {/* Title & Icon */}
                <div className="mt-5 flex items-start gap-4">
                  <div className="w-13 h-13 rounded-2xl bg-gradient-to-tr from-[#edf7e7] via-[#f7f2dc] to-[#e4f3de] border border-[#d5e7ce] flex items-center justify-center shrink-0 text-[#3d7722] shadow-2xs">
                    <span
                      className="material-symbols-outlined text-[30px]"
                      style={{ fontVariationSettings: "'FILL' 1" }}
                    >
                      auto_awesome
                    </span>
                  </div>
                  <div>
                    <h2 className="text-xl sm:text-2xl font-bold text-[#142111] tracking-tight">
                      Créer un nouveau CV
                    </h2>
                    <p className="text-sm text-[#50604c] mt-1 leading-normal">
                      Partez d'un modèle optimisé et générez vos contenus avec l'assistant guidé.
                    </p>
                  </div>
                </div>

                {/* Template Selector */}
                <div className="mt-6 flex flex-col gap-2.5">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-[#23331f] tracking-wide uppercase">
                      Sélectionnez un style de départ
                    </span>
                    <span className="text-xs font-semibold text-[#4d8225]">
                      3 modèles populaires
                    </span>
                  </div>

                  <div className="grid grid-cols-3 gap-3">
                    {/* Template 1: Moderne Épuré */}
                    <div
                      onClick={() => setSelectedTemplate("moderne")}
                      className={`template-choice group/tpl cursor-pointer rounded-2xl p-2.5 transition-all ${
                        selectedTemplate === "moderne"
                          ? "bg-[#edf7e7] border-2 border-[#79b947] shadow-xs"
                          : "bg-[#f7f9f6] border border-[#e2ece0] hover:border-[#b6ccae]"
                      }`}
                      data-style="moderne"
                    >
                      <div className="relative h-26 w-full overflow-hidden rounded-xl bg-white p-2.5 shadow-2xs border border-[#dbe6d7]">
                        <div className="h-2 w-10 rounded bg-[#79b947] mb-2"></div>
                        <div className="space-y-1">
                          <div className="h-1.5 w-full rounded bg-[#e8efe4]"></div>
                          <div className="h-1.5 w-4/5 rounded bg-[#e8efe4]"></div>
                          <div className="h-1.5 w-2/3 rounded bg-[#e8efe4]"></div>
                        </div>
                        <div className="mt-3 flex gap-1.5">
                          <div className="h-5 w-1/3 rounded bg-[#dcf3d4]"></div>
                          <div className="h-5 w-2/3 rounded bg-[#f2f6ee]"></div>
                        </div>
                        {selectedTemplate === "moderne" && (
                          <div className="badge-check absolute bottom-1.5 right-1.5 flex h-4 w-4 items-center justify-center rounded-full bg-[#4d8225] text-white shadow-xs">
                            <span className="material-symbols-outlined text-[11px] font-bold">
                              check
                            </span>
                          </div>
                        )}
                      </div>
                      <p className="mt-2 text-center text-xs font-semibold text-[#1a2916] truncate">
                        Moderne Épuré
                      </p>
                    </div>

                    {/* Template 2: Exécutif Tech */}
                    <div
                      onClick={() => setSelectedTemplate("tech")}
                      className={`template-choice group/tpl cursor-pointer rounded-2xl p-2.5 transition-all ${
                        selectedTemplate === "tech"
                          ? "bg-[#edf7e7] border-2 border-[#79b947] shadow-xs"
                          : "bg-[#f7f9f6] border border-[#e2ece0] hover:border-[#b6ccae]"
                      }`}
                      data-style="tech"
                    >
                      <div className="relative h-26 w-full overflow-hidden rounded-xl bg-white p-2 shadow-2xs border border-[#e4ede1]">
                        <div className="flex h-full gap-1.5">
                          <div className="h-full w-1/3 rounded bg-[#f2f6ee] p-1 flex flex-col gap-1">
                            <div className="h-2 w-full rounded bg-[#356b00]"></div>
                            <div className="h-1 w-full rounded bg-[#cdd8c8]"></div>
                            <div className="h-1 w-3/4 rounded bg-[#cdd8c8]"></div>
                          </div>
                          <div className="h-full w-2/3 flex flex-col gap-1 pt-0.5">
                            <div className="h-1.5 w-full rounded bg-[#e8efe4]"></div>
                            <div className="h-1.5 w-5/6 rounded bg-[#e8efe4]"></div>
                            <div className="h-1.5 w-1/2 rounded bg-[#e8efe4]"></div>
                            <div className="h-3 w-full rounded bg-[#e1ebd9] mt-auto"></div>
                          </div>
                        </div>
                        {selectedTemplate === "tech" && (
                          <div className="badge-check absolute bottom-1.5 right-1.5 flex h-4 w-4 items-center justify-center rounded-full bg-[#4d8225] text-white shadow-xs">
                            <span className="material-symbols-outlined text-[11px] font-bold">
                              check
                            </span>
                          </div>
                        )}
                      </div>
                      <p className="mt-2 text-center text-xs font-semibold text-[#3b4b37] truncate">
                        Exécutif Tech
                      </p>
                    </div>

                    {/* Template 3: Minimaliste Chic */}
                    <div
                      onClick={() => setSelectedTemplate("minimal")}
                      className={`template-choice group/tpl cursor-pointer rounded-2xl p-2.5 transition-all ${
                        selectedTemplate === "minimal"
                          ? "bg-[#edf7e7] border-2 border-[#79b947] shadow-xs"
                          : "bg-[#f7f9f6] border border-[#e2ece0] hover:border-[#b6ccae]"
                      }`}
                      data-style="minimal"
                    >
                      <div className="relative h-26 w-full overflow-hidden rounded-xl bg-white p-2.5 shadow-2xs border border-[#e4ede1]">
                        <div className="space-y-1.5 pt-0.5">
                          <div className="h-2 w-12 rounded bg-[#1e2e1a]"></div>
                          <div className="h-1 w-full rounded bg-[#e2ebe0]"></div>
                          <div className="h-1 w-full rounded bg-[#e2ebe0]"></div>
                          <div className="h-1 w-3/4 rounded bg-[#e2ebe0]"></div>
                          <div className="h-1 w-full rounded bg-[#e2ebe0]"></div>
                          <div className="h-1 w-2/3 rounded bg-[#e2ebe0]"></div>
                        </div>
                        {selectedTemplate === "minimal" && (
                          <div className="badge-check absolute bottom-1.5 right-1.5 flex h-4 w-4 items-center justify-center rounded-full bg-[#4d8225] text-white shadow-xs">
                            <span className="material-symbols-outlined text-[11px] font-bold">
                              check
                            </span>
                          </div>
                        )}
                      </div>
                      <p className="mt-2 text-center text-xs font-semibold text-[#3b4b37] truncate">
                        Minimaliste
                      </p>
                    </div>
                  </div>

                  {/* Start from scratch pill button */}
                  <div className="mt-1 flex items-center justify-between rounded-xl bg-[#f5f8f2] border border-[#e5eee2] px-3.5 py-2">
                    <span className="text-xs text-[#52634e]">
                      Préférez-vous construire sans modèle ?
                    </span>
                    <button
                      onClick={() => handleOpenCreateModal("minimal")}
                      className="inline-flex items-center gap-1 text-xs font-bold text-[#3d7421] hover:text-[#254d12] hover:underline cursor-pointer"
                    >
                      <span className="material-symbols-outlined text-[14px]">
                        edit_note
                      </span>
                      <span>Démarrer de zéro</span>
                    </button>
                  </div>
                </div>

                {/* Checklist */}
                <div className="mt-5 space-y-3">
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Plus de 20 templates conformes aux normes RH internationales</span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Reformulation IA de vos réalisations en verbes d'action</span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-[#40503c]">
                    <div className="w-5 h-5 rounded-full bg-[#dcf3d4] border border-[#c4e8b9] flex items-center justify-center text-[#285e13] shrink-0">
                      <span className="material-symbols-outlined text-[13px] font-bold">
                        check
                      </span>
                    </div>
                    <span>Export vectoriel PDF sans perte et lisible par les robots ATS</span>
                  </div>
                </div>
              </div>

              {/* Action Button: Opens the Popup */}
              <div className="mt-8 pt-2">
                <button
                  id="create-btn"
                  onClick={() => handleOpenCreateModal(selectedTemplate)}
                  className="w-full h-12 flex items-center justify-center gap-2 rounded-xl bg-[#245217] hover:bg-[#1a3c10] text-white font-semibold text-sm shadow-[0_4px_14px_rgba(36,82,23,0.3)] hover:shadow-[0_6px_20px_rgba(36,82,23,0.4)] transition-all duration-200 active:scale-[0.99] cursor-pointer"
                >
                  <span className="material-symbols-outlined text-[18px]">
                    add_circle
                  </span>
                  <span>Commencer la création</span>
                  <span className="material-symbols-outlined text-[18px]">
                    arrow_forward
                  </span>
                </button>
              </div>
            </div>
          </section>

          {/* 4. SECTION REPRENDRE & CONSEIL STRATÉGIQUE */}
          {/* Bandeau Conseil ATS */}
          <div className="rounded-2xl bg-gradient-to-r from-[#eef7ec] via-[#f5fbf2] to-[#e8f4e4] border border-[#d6e7d0] p-5 sm:p-6 flex flex-col md:flex-row items-center justify-between gap-5 shadow-2xs">
            <div className="flex items-center gap-4">
              <div className="h-12 w-12 rounded-xl bg-[#346b1d] flex items-center justify-center shrink-0 text-white shadow-xs">
                <span className="material-symbols-outlined text-[24px]">
                  lightbulb
                </span>
              </div>
              <div className="flex flex-col">
                <span className="text-xs font-bold uppercase tracking-wider text-[#356c1d]">
                  Conseil Stratégique TNCV
                </span>
                <p className="text-sm sm:text-base text-[#1b2a18] font-medium mt-0.5">
                  Saviez-vous que{" "}
                  <strong className="text-[#2b5d15] font-bold underline decoration-[#79b947] underline-offset-2">
                    75% des candidatures
                  </strong>{" "}
                  sont éliminées par les filtres ATS avant d'atteindre un recruteur humain ?
                </p>
              </div>
            </div>
            <div className="flex items-center gap-3 shrink-0 w-full sm:w-auto justify-end">
              <button
                onClick={() => {
                  setAtsModalContent({
                    title: "Guide ATS 2025 - Réussir le filtrage",
                    score: 95,
                    points: [
                      "1. Évitez les zones de texte flottantes, les colonnes imbriquées et les graphiques bitmap.",
                      "2. Utilisez des polices standards universelles (Inter, Roboto, Arial, Calibri).",
                      "3. Reprenez fidèlement les mots-clés exacts de l'offre d'emploi ciblée.",
                      "4. Privilégiez l'exportation PDF vectorielle générée par TNCV.",
                    ],
                  });
                  setAtsModalOpen(true);
                }}
                className="px-4 py-2 rounded-xl bg-white hover:bg-[#f6faf3] text-[#33422f] border border-[#d5e2cf] text-sm font-semibold transition-all shadow-2xs hover:shadow-xs cursor-pointer"
              >
                Guide ATS 2025
              </button>
              <button
                onClick={() => {
                  if (cvs.length > 0) {
                    navigate(`/cvs/${cvs[0].id}`);
                  } else {
                    handleOpenCreateModal();
                  }
                }}
                className="px-4 py-2 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white text-sm font-semibold transition-all shadow-xs hover:shadow-sm cursor-pointer"
              >
                Tester un profil
              </button>
            </div>
          </div>

          {/* Section Reprendre CVs récents */}
          <section className="flex flex-col gap-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2.5">
                <div className="w-7 h-7 rounded-lg bg-[#edf7e7] text-[#4d8225] flex items-center justify-center">
                  <span className="material-symbols-outlined text-[19px]">
                    history
                  </span>
                </div>
                <h2 className="text-lg sm:text-xl font-bold text-[#142111] tracking-tight">
                  Reprendre là où vous vous êtes arrêté
                </h2>
              </div>
              <Link
                to="/cvs"
                className="text-sm text-[#4d8225] hover:text-[#2d5713] font-semibold hover:underline flex items-center gap-1 transition-colors"
              >
                <span>Voir tous mes CVs</span>
                <span className="material-symbols-outlined text-[17px]">
                  chevron_right
                </span>
              </Link>
            </div>

            {loadingCvs ? (
              <div className="p-8 rounded-2xl bg-white border border-[#e2ece0] text-center text-sm text-[#657760]">
                Chargement de vos CVs en cours...
              </div>
            ) : filteredCvs.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {filteredCvs.slice(0, 4).map((cv, idx) => (
                  <div
                    key={cv.id || idx}
                    className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 rounded-2xl bg-white border border-[#e2ece0] p-5 shadow-2xs hover:shadow-md hover:border-[#cadcc5] transition-all"
                  >
                    <div className="flex items-center gap-4 min-w-0">
                      <div className="h-15 w-12 rounded-lg bg-[#f4f7f2] border border-[#dfe8dc] flex flex-col p-1.5 shrink-0 overflow-hidden shadow-2xs">
                        <div className="h-2 w-5 rounded bg-[#79b947] mb-1"></div>
                        <div className="h-1 w-full rounded bg-[#d7e3d3] mb-1"></div>
                        <div className="h-1 w-4/5 rounded bg-[#d7e3d3] mb-1"></div>
                        <div className="h-1 w-2/3 rounded bg-[#d7e3d3]"></div>
                      </div>
                      <div className="flex flex-col min-w-0">
                        <div className="flex items-center gap-2">
                          <Link
                            to={`/cvs/${cv.id}`}
                            className="text-sm sm:text-base font-semibold text-[#162512] truncate hover:text-[#356b00]"
                          >
                            {cv.title}
                          </Link>
                          <span className="inline-flex items-center rounded-full bg-[#def4d7] border border-[#c5e9bc] px-2 py-0.5 text-[11px] font-bold text-[#2d6617] shrink-0">
                            Prêt
                          </span>
                        </div>
                        <p className="text-xs text-[#63755f] flex items-center gap-2 mt-1">
                          <span>{cv.fullName}</span>
                          <span>•</span>
                          <span>{cv.email}</span>
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center justify-between sm:justify-end gap-5 w-full sm:w-auto pt-2 sm:pt-0 border-t sm:border-t-0 border-[#f0f4ee]">
                      <div className="flex flex-col items-end">
                        <div className="flex items-center gap-1">
                          <span className="text-xl font-bold text-[#356c1d]">
                            {idx === 0 ? "92" : "88"}
                          </span>
                          <span className="text-xs text-[#7d9078] font-medium">
                            /100
                          </span>
                        </div>
                        <span className="text-[11px] text-[#4d8225] font-semibold">
                          ATS Excellent
                        </span>
                      </div>
                      <div className="flex items-center gap-2">
                        <Link
                          to={`/cvs/${cv.id}`}
                          className="p-2 rounded-xl text-[#51614d] hover:bg-[#f2f7ef] hover:text-[#182615] border border-transparent hover:border-[#dfe8dc] transition-all cursor-pointer"
                          title="Aperçu du CV"
                        >
                          <span className="material-symbols-outlined text-[20px]">
                            visibility
                          </span>
                        </Link>
                        <Link
                          to={`/cvs/${cv.id}/edit`}
                          className="h-9 px-3.5 rounded-xl bg-[#edf7e7] hover:bg-[#deefd6] text-[#2c5f16] border border-[#d4ebd0] text-xs font-semibold flex items-center gap-1.5 transition-all cursor-pointer"
                        >
                          <span className="material-symbols-outlined text-[16px]">
                            edit
                          </span>
                          <span>Éditer</span>
                        </Link>
                        <button
                          onClick={() => handleDeleteCv(cv.id)}
                          className="p-2 rounded-xl text-red-500 hover:bg-red-50 transition-colors"
                          title="Supprimer"
                        >
                          <span className="material-symbols-outlined text-[18px]">
                            delete
                          </span>
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              /* Sample visual cards if user has no CVs yet */
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Sample Card 1 */}
                <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 rounded-2xl bg-white border border-[#e2ece0] p-5 shadow-2xs hover:shadow-md hover:border-[#cadcc5] transition-all">
                  <div className="flex items-center gap-4 min-w-0">
                    <div className="h-15 w-12 rounded-lg bg-[#f4f7f2] border border-[#dfe8dc] flex flex-col p-1.5 shrink-0 overflow-hidden shadow-2xs">
                      <div className="h-2 w-5 rounded bg-[#79b947] mb-1"></div>
                      <div className="h-1 w-full rounded bg-[#d7e3d3] mb-1"></div>
                      <div className="h-1 w-4/5 rounded bg-[#d7e3d3] mb-1"></div>
                      <div className="h-1 w-2/3 rounded bg-[#d7e3d3]"></div>
                    </div>
                    <div className="flex flex-col min-w-0">
                      <div className="flex items-center gap-2">
                        <h3 className="text-sm sm:text-base font-semibold text-[#162512] truncate">
                          Lead Product Designer.pdf
                        </h3>
                        <span className="inline-flex items-center rounded-full bg-[#def4d7] border border-[#c5e9bc] px-2 py-0.5 text-[11px] font-bold text-[#2d6617] shrink-0">
                          Exemple
                        </span>
                      </div>
                      <p className="text-xs text-[#63755f] flex items-center gap-2 mt-1">
                        <span>Modifié il y a 2h</span>
                        <span>•</span>
                        <span>Tech &amp; Design</span>
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center justify-between sm:justify-end gap-5 w-full sm:w-auto pt-2 sm:pt-0 border-t sm:border-t-0 border-[#f0f4ee]">
                    <div className="flex flex-col items-end">
                      <div className="flex items-center gap-1">
                        <span className="text-xl font-bold text-[#356c1d]">92</span>
                        <span className="text-xs text-[#7d9078] font-medium">/100</span>
                      </div>
                      <span className="text-[11px] text-[#4d8225] font-semibold">
                        ATS Excellent
                      </span>
                    </div>
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => handleOpenCreateModal("moderne")}
                        className="h-9 px-3.5 rounded-xl bg-[#edf7e7] hover:bg-[#deefd6] text-[#2c5f16] border border-[#d4ebd0] text-xs font-semibold flex items-center gap-1.5 transition-all cursor-pointer"
                      >
                        <span className="material-symbols-outlined text-[16px]">add</span>
                        <span>Créer</span>
                      </button>
                    </div>
                  </div>
                </div>

                {/* Sample Card 2 */}
                <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 rounded-2xl bg-white border border-[#e2ece0] p-5 shadow-2xs hover:shadow-md hover:border-[#cadcc5] transition-all">
                  <div className="flex items-center gap-4 min-w-0">
                    <div className="h-15 w-12 rounded-lg bg-[#f4f7f2] border border-[#dfe8dc] flex flex-col p-1.5 shrink-0 overflow-hidden shadow-2xs">
                      <div className="h-2 w-5 rounded bg-[#b8c6b3] mb-1"></div>
                      <div className="h-1 w-full rounded bg-[#d7e3d3] mb-1"></div>
                      <div className="h-1 w-3/4 rounded bg-[#d7e3d3] mb-1"></div>
                      <div className="h-1 w-1/2 rounded bg-[#d7e3d3]"></div>
                    </div>
                    <div className="flex flex-col min-w-0">
                      <div className="flex items-center gap-2">
                        <h3 className="text-sm sm:text-base font-semibold text-[#162512] truncate">
                          Senior Fullstack Dev.pdf
                        </h3>
                        <span className="inline-flex items-center rounded-full bg-[#fcf2d9] border border-[#f5dfa8] px-2 py-0.5 text-[11px] font-bold text-[#8f6812] shrink-0">
                          Exemple
                        </span>
                      </div>
                      <p className="text-xs text-[#63755f] flex items-center gap-2 mt-1">
                        <span>Modifié hier</span>
                        <span>•</span>
                        <span>SaaS B2B</span>
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center justify-between sm:justify-end gap-5 w-full sm:w-auto pt-2 sm:pt-0 border-t sm:border-t-0 border-[#f0f4ee]">
                    <div className="flex flex-col items-end">
                      <div className="flex items-center gap-1">
                        <span className="text-xl font-bold text-[#6a7c64]">78</span>
                        <span className="text-xs text-[#7d9078] font-medium">/100</span>
                      </div>
                      <span className="text-[11px] text-[#85703a] font-medium">
                        À optimiser
                      </span>
                    </div>
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => handleOpenCreateModal("tech")}
                        className="h-9 px-3.5 rounded-xl bg-[#f2f6ee] hover:bg-[#e4ede0] text-[#34462e] border border-[#dbe6d6] text-xs font-semibold flex items-center gap-1.5 transition-all cursor-pointer"
                      >
                        <span className="material-symbols-outlined text-[16px]">add</span>
                        <span>Créer</span>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </section>
        </div>
      </main>

      {/* 5. FOOTER ÉLÉGANT */}
      <footer className="w-full bg-white border-t border-[#e3ede0] py-6 pl-4 pr-4 lg:pl-72 lg:pr-8 mt-12">
        <div className="max-w-7xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-[#667761]">
          <div className="flex items-center gap-2">
            <img
              alt="TNCV Logo"
              className="h-4 w-auto opacity-70"
              src={tncvLogo}
            />
            <span>
              © 2025 TNCV Platform. Tous droits réservés. L'IA au service de votre carrière.
            </span>
          </div>
          <div className="flex items-center gap-6 font-medium">
            <button
              onClick={() => showToast("Engagement strict de confidentialité et respect du RGPD.")}
              className="hover:text-[#1b2916] transition-colors cursor-pointer"
            >
              Confidentialité
            </button>
            <button
              onClick={() => showToast("Conditions générales d'utilisation de la plateforme TNCV.")}
              className="hover:text-[#1b2916] transition-colors cursor-pointer"
            >
              Conditions d'utilisation
            </button>
            <button
              onClick={() => {
                setAtsModalContent({
                  title: "Sécurité & Confidentialité ATS",
                  score: 100,
                  points: [
                    "Vos données ne sont jamais revendues à des tiers.",
                    "Chiffrement de bout en bout des métadonnées de parcours.",
                    "Conformité totale avec les normes de parsing ATS mondiales.",
                  ],
                });
                setAtsModalOpen(true);
              }}
              className="hover:text-[#1b2916] transition-colors cursor-pointer"
            >
              Sécurité ATS
            </button>
            <button
              onClick={() => showToast("Support technique TNCV disponible 7j/7.")}
              className="hover:text-[#1b2916] transition-colors cursor-pointer"
            >
              Support
            </button>
          </div>
        </div>
      </footer>

      {/* MULTI-STEP CREATION MODAL CONNECTED TO BACKEND */}
      <CvCreateModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onSuccess={handleCvCreated}
        initialTemplate={selectedTemplate}
        userDefaults={{
          fullName: user ? `${user.firstName} ${user.lastName || ""}`.trim() : undefined,
          email: user?.email,
        }}
      />

      {/* ATS & ADVISORY POPUP MODAL */}
      {atsModalOpen && atsModalContent && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-xs animate-fade-in">
          <div className="w-full max-w-lg bg-white rounded-3xl p-6 sm:p-7 shadow-2xl border border-[#dce8d7]">
            <div className="flex items-center justify-between pb-4 border-b border-[#edf4eb]">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-[#edf7e7] text-[#346b1d] flex items-center justify-center">
                  <span className="material-symbols-outlined text-[24px]">verified</span>
                </div>
                <div>
                  <h3 className="text-base font-bold text-[#142111]">
                    {atsModalContent.title}
                  </h3>
                  <span className="text-xs text-[#52634e]">
                    Indicateur de compatibilité ATS
                  </span>
                </div>
              </div>
              <button
                onClick={() => setAtsModalOpen(false)}
                className="text-[#657760] hover:text-[#172213] p-1.5 rounded-lg hover:bg-slate-100"
              >
                <span className="material-symbols-outlined text-[20px]">close</span>
              </button>
            </div>

            <div className="my-5 flex items-center gap-4 p-4 rounded-2xl bg-[#f7fbf4] border border-[#d8e8d3]">
              <div className="text-3xl font-extrabold text-[#356b00]">
                {atsModalContent.score}%
              </div>
              <div className="text-xs text-[#4a5845] leading-relaxed">
                Ce score reflète la capacité de votre CV à traverser les analyseurs automatisés des recruteurs sans perte de données.
              </div>
            </div>

            <div className="space-y-2.5 mb-6">
              {atsModalContent.points.map((pt, i) => (
                <div key={i} className="flex items-start gap-2.5 text-xs text-[#3a4b37]">
                  <span className="material-symbols-outlined text-[#4d8225] text-[16px] shrink-0 mt-0.5">
                    check_circle
                  </span>
                  <span>{pt}</span>
                </div>
              ))}
            </div>

            <div className="flex items-center gap-3 justify-end pt-2">
              <button
                onClick={() => setAtsModalOpen(false)}
                className="px-4 py-2 rounded-xl bg-[#f2f6ee] text-[#3b4d37] hover:bg-[#e4ede0] text-xs font-semibold cursor-pointer"
              >
                Fermer
              </button>
              <button
                onClick={() => {
                  setAtsModalOpen(false);
                  handleOpenCreateModal();
                }}
                className="px-4 py-2 rounded-xl bg-[#79b947] hover:bg-[#6ba83d] text-white text-xs font-semibold shadow-xs cursor-pointer"
              >
                Ouvrir dans le créateur
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;