package gestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Patient> patients = new ArrayList<>();
    static List<Medecin> medecins = new ArrayList<>();
    static List<ServiceHospitalier> services = new ArrayList<>();
    static int prochainIdPatient = 1;
    static int prochainIdMedecin = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Créer quelques services par défaut
        services.add(new ServiceHospitalier("CARD", "Cardiologie", 30));
        services.add(new ServiceHospitalier("URG",  "Urgences", 50));
        services.add(new ServiceHospitalier("PED",  "Pédiatrie", 20));

        int choix;
        do {
            afficherMenu();
            choix = lireEntier(sc);
            switch (choix) {
                case 1 -> ajouterPatient(sc);
                case 2 -> ajouterMedecin(sc);
                case 3 -> afficherTousLesPatients();
                case 4 -> afficherTousLesMedecins();
                case 5 -> affecterPatientAuService(sc);
                case 6 -> tableauDeBordServices();
                case 7 -> supprimerPatient(sc);
                case 8 -> modifierPatient(sc);
                case 0 -> System.out.println("\n👋 Fermeture de MedManager.");
                default -> System.out.println("⚠ Choix invalide.");
            }
        } while (choix != 0);

        sc.close();
    }

    static void afficherMenu() {
        System.out.println("\n══════ MedManager v1.0 ══════");
        System.out.println("  1. ➕ Ajouter un patient");
        System.out.println("  2. ➕ Ajouter un médecin");
        System.out.println("  3. 📋 Afficher les patients");
        System.out.println("  4. 📋 Afficher les médecins");
        System.out.println("  5. 🏥 Affecter patient → service");
        System.out.println("  6. 📊 Tableau de bord des services");
        System.out.println("  7. 🗑 Supprimer un patient");
        System.out.println("  8. ✏\uFE0F Modifier un patient");
        System.out.println("  0. 🚪 Quitter");
        System.out.print("Votre choix : ");
    }

    static int lireEntier(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("⚠ Nombre attendu : ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    static void ajouterPatient(Scanner sc) {
        System.out.println("\n--- Nouveau Patient ---");
        String id = String.format("P%03d", prochainIdPatient++);

        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine();
        System.out.print("Date de naissance (AAAA-MM-JJ) : ");
        LocalDate dn = LocalDate.parse(sc.nextLine());

        Patient p = new Patient(id, nom, prenom, dn);

        System.out.print("Groupe sanguin (A+, A-, B+, B-, AB+, AB-, O+, O-) : ");
        p.setGroupeSanguin(sc.nextLine());

        patients.add(p);
        System.out.println("✅ " + p.getIdentiteComplete()
                + " enregistré (" + p.getAge() + " ans)");
    }

    static void ajouterMedecin(Scanner sc) {
        System.out.println("\n--- Nouveau Médecin ---");
        String id = String.format("M%03d", prochainIdMedecin++);

        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine();
        System.out.print("Date de naissance (AAAA-MM-JJ) : ");
        LocalDate dn = LocalDate.parse(sc.nextLine());
        System.out.print("Spécialité : ");
        String spe = sc.nextLine();
        System.out.print("Matricule : ");
        String mat = sc.nextLine();

        Medecin m = new Medecin(id, nom, prenom, dn, spe, mat);
        medecins.add(m);
        System.out.println("✅ " + m + " enregistré");
    }

    static void afficherTousLesPatients() {
        if (patients.isEmpty()) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }
        System.out.println("\n--- Patients ---");
        System.out.printf("%-6s %-15s %-15s %-5s %-5s%n",
                "ID", "Nom", "Prénom", "Âge", "Sang");
        System.out.println("─".repeat(50));
        for (Patient p : patients) {
            System.out.printf("%-6s %-15s %-15s %-5d %-5s%n",
                    p.getId(), p.getNom(), p.getPrenom(),
                    p.getAge(),
                    p.getGroupeSanguin() != null ? p.getGroupeSanguin() : "—");
        }
    }

    static void afficherTousLesMedecins() {
        if (medecins.isEmpty()) {
            System.out.println("\nAucun médecin enregistré.");
            return;
        }
        System.out.println("\n--- Médecins ---");
        for (Medecin m : medecins) {
            System.out.println("  → " + m);
        }
    }

    static void affecterPatientAuService(Scanner sc) {
        if (patients.isEmpty()) {
            System.out.println("\nAucun patient à affecter.");
            return;
        }

        // Choisir le patient
        System.out.print("\nID du patient : ");
        String idPat = sc.nextLine();
        Patient patient = null;
        for (Patient p : patients) {
            if (p.getId().equals(idPat)) { patient = p; break; }
        }
        if (patient == null) {
            System.out.println("⚠ Patient introuvable.");
            return;
        }

        // Choisir le service
        System.out.println("Services disponibles :");
        for (int i = 0; i < services.size(); i++) {
            System.out.println("  " + (i+1) + ". " + services.get(i));
        }
        System.out.print("Votre choix : ");
        int idx = lireEntier(sc) - 1;

        if (idx < 0 || idx >= services.size()) {
            System.out.println("⚠ Service invalide.");
            return;
        }

        ServiceHospitalier service = services.get(idx);
        if (service.admettre(patient)) {
            System.out.println("✅ " + patient.getIdentiteComplete()
                    + " → " + service.getNom());
        }
    }

    static void tableauDeBordServices() {
        for (ServiceHospitalier s : services) {
            s.afficherTableauDeBord();
        }
    }

    static void supprimerPatient(Scanner sc) {

        if (patients.isEmpty()) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }

        System.out.print("\nID du patient à supprimer : ");
        String id = sc.nextLine();

        Patient patientTemp = new Patient(id, "", "", LocalDate.now());

        if (!patients.contains(patientTemp)) {
            System.out.println("⚠ Patient introuvable.");
            return;
        }

        Patient patientASupprimer = null;
        for (Patient p : patients) {
            if (p.equals(patientTemp)) {
                patientASupprimer = p;
                break;
            }
        }

        System.out.println("\nPatient trouvé :");
        System.out.println(patientASupprimer);

        System.out.print("Confirmer la suppression ? (oui/non) : ");
        String confirmation = sc.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {

            for (ServiceHospitalier service : services) {
                service.retirerPatient(patientASupprimer);
            }
            patients.remove(patientASupprimer);

            System.out.println("✅ Patient supprimé complètement du système.");
        } else {
            System.out.println("❌ Suppression annulée.");
        }
    }

    static void modifierPatient(Scanner sc){
        if (patients.isEmpty()) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }

        System.out.print("\nID du patient à modifier : ");
        String id = sc.nextLine();

        Patient patient = null;
        for (Patient p : patients) {
            if (p.getId().equals(id)) {
                patient = p;
                break;
            }
        }

        if (patient == null) {
            System.out.println("⚠ Patient introuvable.");
            return;
        }

        System.out.println("\nPatient actuel :");
        System.out.println(patient);

        // ───── Modification champ par champ ─────

        System.out.print("Nouveau nom (" + patient.getNom() + ") : ");
        String saisie = sc.nextLine();
        if (!saisie.isEmpty()) {
            patient.setNom(saisie);
        }

        System.out.print("Nouveau prénom (" + patient.getPrenom() + ") : ");
        saisie = sc.nextLine();
        if (!saisie.isEmpty()) {
            patient.setPrenom(saisie);
        }

        System.out.print("Nouvelle date de naissance ("
                + patient.getDateNaissance() + ") [AAAA-MM-JJ] : ");
        saisie = sc.nextLine();
        if (!saisie.isEmpty()) {
            patient.setDateNaissance(LocalDate.parse(saisie));
        }

        System.out.print("Nouveau téléphone ("
                + (patient.getTelephone() != null ? patient.getTelephone() : "—")
                + ") : ");
        saisie = sc.nextLine();
        if (!saisie.isEmpty()) {
            try {
                patient.setTelephone(saisie);
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ " + e.getMessage());
            }
        }

        System.out.print("Nouveau groupe sanguin ("
                + patient.getGroupeSanguin() + ") : ");
        saisie = sc.nextLine();
        if (!saisie.isEmpty()) {
            try {
                patient.setGroupeSanguin(saisie);
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ " + e.getMessage());
            }
        }

        System.out.println("✅ Modification terminée.");
    }
}