package gestion;

import java.util.Scanner;

public class Main {


    static final int MAX_PATIENTS = 20;
    static String[] nomsPatients = new String[MAX_PATIENTS];
    static String[] prenomsPatients = new String[MAX_PATIENTS];
    static int[] anneesNaissance = new int[MAX_PATIENTS];
    static int[] servicePatient = new int[MAX_PATIENTS];
    static int nbPatients = 0;


    static final int MAX_SERVICES = 10;
    static String[] nomsServices = new String[MAX_SERVICES];
    static int[] capacitesServices = new int[MAX_SERVICES];
    static int[] occupesServices = new int[MAX_SERVICES];
    static int nbServices = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        initialiserServicesParDefaut();

        int choix;
        do {
            afficherMenu();
            choix = lireEntier(scanner);

            switch (choix) {
                case 1 -> ajouterPatient(scanner);
                case 2 -> afficherPatients(false);
                case 3 -> rechercherPatient(scanner);
                case 4 -> afficherStatistiques();
                case 5 -> afficherPatientsTriesParNom();
                case 0 -> System.out.println("\n👋 Au revoir !");
                default -> System.out.println("⚠ Choix invalide.");
            }
        } while (choix != 0);

        scanner.close();
    }


    static void afficherMenu() {
        System.out.println("\n══════ MedManager v0.1 ══════");
        System.out.println("  1. ➕ Ajouter un patient");
        System.out.println("  2. 📋 Afficher tous les patients");
        System.out.println("  3. 🔍 Rechercher un patient");
        System.out.println("  4. 📊 Statistiques");
        System.out.println("  5. Afficher patients triés (par nom)");
        System.out.println("  0.  Quitter");
        System.out.print("Votre choix : ");
    }


    static int lireEntier(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("⚠ Entrez un nombre : ");
            scanner.next();
        }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }


    static void ajouterPatient(Scanner scanner) {
        if (nbPatients >= MAX_PATIENTS) {
            System.out.println("⚠ Capacité maximale de patients atteinte !");
            return;
        }
        if (nbServices == 0) {
            System.out.println("⚠ Aucun service disponible. Impossible d'ajouter un patient.");
            return;
        }

        System.out.println("\n--- Nouveau Patient ---");

        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine().trim();


        int idxService = choisirService(scanner);
        if (idxService == -1) {
            System.out.println("⚠ Aucun service n'a de place. Ajout annulé.");
            return;
        }


        int annee;
        int age;
        do {
            System.out.print("Année de naissance : ");
            annee = lireEntier(scanner);
            age = 2026 - annee;

            if (age < 0 || age > 150) {
                System.out.println("❌ Âge invalide (" + age + "). Réessayez (0 à 150).");
            }
        } while (age < 0 || age > 150);


        nomsPatients[nbPatients] = nom;
        prenomsPatients[nbPatients] = prenom;
        anneesNaissance[nbPatients] = annee;
        servicePatient[nbPatients] = idxService;

        nbPatients++;
        occupesServices[idxService]++;

        System.out.println("✅ Patient enregistré (" + age + " ans) - Service: " + nomsServices[idxService]);
    }


    static int choisirService(Scanner scanner) {
        while (true) {
            afficherServices();
            System.out.print("Choisir un service (numéro) : ");
            int choix = lireEntier(scanner);

            if (choix < 1 || choix > nbServices) {
                System.out.println("⚠ Numéro de service invalide.");
                continue;
            }

            int idx = choix - 1;
            if (occupesServices[idx] >= capacitesServices[idx]) {
                System.out.println("❌ Service complet. Choisissez un autre.");
                continue;
            }

            return idx;
        }
    }

    static void afficherServices() {
        System.out.println("\n--- Services ---");
        for (int i = 0; i < nbServices; i++) {
            System.out.printf("%d) %-15s  (%d/%d)%n",
                    (i + 1), nomsServices[i], occupesServices[i], capacitesServices[i]);
        }
    }

    static void initialiserServicesParDefaut() {

        ajouterService("Urgences", 3);
        ajouterService("Cardio", 2);
        ajouterService("Pédiatrie", 2);
        ajouterService("Chirurgie", 2);
    }

    static void ajouterService(String nom, int capacite) {
        if (nbServices >= MAX_SERVICES) return;
        nomsServices[nbServices] = nom;
        capacitesServices[nbServices] = capacite;
        occupesServices[nbServices] = 0;
        nbServices++;
    }


    static void afficherPatients(boolean avecIndexSource) {
        if (nbPatients == 0) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }


        int wNum = 5, wNom = 16, wPrenom = 16, wAge = 7;

        System.out.println();
        System.out.println("┌" + "─".repeat(wNum) + "┬" + "─".repeat(wNom) + "┬" + "─".repeat(wPrenom) + "┬" + "─".repeat(wAge) + "┐");
        System.out.printf("│%3s │ %-14s │ %-14s │%5s │%n", "#", "Nom", "Prénom", "Âge");
        System.out.println("├" + "─".repeat(wNum) + "┼" + "─".repeat(wNom) + "┼" + "─".repeat(wPrenom) + "┼" + "─".repeat(wAge) + "┤");

        for (int i = 0; i < nbPatients; i++) {
            int age = 2026 - anneesNaissance[i];
            System.out.printf("│%3d │ %-14s │ %-14s │%5d │%n", (i + 1), nomsPatients[i], prenomsPatients[i], age);
        }

        System.out.println("└" + "─".repeat(wNum) + "┴" + "─".repeat(wNom) + "┴" + "─".repeat(wPrenom) + "┴" + "─".repeat(wAge) + "┘");
        System.out.println("Total : " + nbPatients + " patient(s)");
    }


    static void rechercherPatient(Scanner scanner) {
        if (nbPatients == 0) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }

        System.out.print("\nRechercher (nom) : ");
        String recherche = scanner.nextLine().toLowerCase();
        boolean trouve = false;

        for (int i = 0; i < nbPatients; i++) {
            if (nomsPatients[i].toLowerCase().contains(recherche)) {
                int age = 2026 - anneesNaissance[i];
                String serv = nomsServices[servicePatient[i]];
                System.out.println("→ " + prenomsPatients[i] + " " + nomsPatients[i] + " (" + age + " ans) - " + serv);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun résultat pour \"" + recherche + "\"");
        }
    }


    static void afficherStatistiques() {
        if (nbPatients == 0) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }

        int somme = 0;
        int minAge = 2026 - anneesNaissance[0];
        int maxAge = 2026 - anneesNaissance[0];

        for (int i = 0; i < nbPatients; i++) {
            int age = 2026 - anneesNaissance[i];
            somme += age;
            if (age < minAge) minAge = age;
            if (age > maxAge) maxAge = age;
        }

        double ageMoyen = (double) somme / nbPatients;

        System.out.println("\n---  Statistiques ---");
        System.out.println("Total patients : " + nbPatients);
        System.out.printf("Âge moyen      : %.2f%n", ageMoyen);
        System.out.println("Plus jeune     : " + minAge + " ans");
        System.out.println("Plus vieux     : " + maxAge + " ans");
    }


    static void afficherPatientsTriesParNom() {
        if (nbPatients == 0) {
            System.out.println("\nAucun patient enregistré.");
            return;
        }


        String[] noms = new String[nbPatients];
        String[] prenoms = new String[nbPatients];
        int[] annees = new int[nbPatients];
        int[] services = new int[nbPatients];

        for (int i = 0; i < nbPatients; i++) {
            noms[i] = nomsPatients[i];
            prenoms[i] = prenomsPatients[i];
            annees[i] = anneesNaissance[i];
            services[i] = servicePatient[i];
        }


        for (int i = 0; i < nbPatients - 1; i++) {
            for (int j = 0; j < nbPatients - 1 - i; j++) {
                if (noms[j].compareToIgnoreCase(noms[j + 1]) > 0) {
                    // swap noms
                    String tmpS = noms[j]; noms[j] = noms[j + 1]; noms[j + 1] = tmpS;
                    // swap prenoms
                    tmpS = prenoms[j]; prenoms[j] = prenoms[j + 1]; prenoms[j + 1] = tmpS;
                    // swap annees
                    int tmpI = annees[j]; annees[j] = annees[j + 1]; annees[j + 1] = tmpI;
                    // swap services
                    tmpI = services[j]; services[j] = services[j + 1]; services[j + 1] = tmpI;
                }
            }
        }


        int wNum = 5, wNom = 16, wPrenom = 16, wAge = 7;

        System.out.println();
        System.out.println("┌" + "─".repeat(wNum) + "┬" + "─".repeat(wNom) + "┬" + "─".repeat(wPrenom) + "┬" + "─".repeat(wAge) + "┐");
        System.out.printf("│%3s │ %-14s │ %-14s │%5s │%n", "#", "Nom", "Prénom", "Âge");
        System.out.println("├" + "─".repeat(wNum) + "┼" + "─".repeat(wNom) + "┼" + "─".repeat(wPrenom) + "┼" + "─".repeat(wAge) + "┤");

        for (int i = 0; i < nbPatients; i++) {
            int age = 2026 - annees[i];
            System.out.printf("│%3d │ %-14s │ %-14s │%5d │%n", (i + 1), noms[i], prenoms[i], age);
        }

        System.out.println("└" + "─".repeat(wNum) + "┴" + "─".repeat(wNom) + "┴" + "─".repeat(wPrenom) + "┴" + "─".repeat(wAge) + "┘");
        System.out.println("Total : " + nbPatients + " patient(s) (triés)");
    }
}