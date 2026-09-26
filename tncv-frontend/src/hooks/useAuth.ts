/**
 * Re-export du hook useAuth depuis AuthContext.
 * Ce fichier existe pour la compatibilité avec les imports
 * qui utilisent "hooks/useAuth" (ex: DashboardLayout).
 *
 * Source unique de vérité : context/AuthContext.tsx
 */
export { useAuth } from "../context/AuthContext";