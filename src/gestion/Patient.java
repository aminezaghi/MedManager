package gestion;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Patient {

    // ── Attributs (les données du patient) ──
    private String id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String telephone;
    private String groupeSanguin;

    // Constructeur complet
    public Patient(String id, String nom, String prenom,
                   LocalDate dateNaissance, String telephone,
                   String groupeSanguin) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.groupeSanguin = groupeSanguin;
    }

    // Constructeur minimal — appelle le constructeur complet
    public Patient(String id, String nom, String prenom,
                   LocalDate dateNaissance) {
        this(id, nom, prenom, dateNaissance, null, null);
    }

    // ── Méthodes (les comportements du patient) ──
    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now())
                .getYears();
    }

    public String getIdentiteComplete() {
        return prenom + " " + nom + " (ID: " + id + ")";
    }

    public void setGroupeSanguin(String groupe) {
        List<String> valides = List.of(
                "A+", "A-", "B+", "B-",
                "AB+", "AB-", "O+", "O-"
        );

        if (groupe == null || !valides.contains(groupe)) {
            throw new IllegalArgumentException(
                    "Groupe sanguin invalide : " + groupe
            );
        }
        this.groupeSanguin = groupe;
    }

    // Le getter est simple — pas de validation en lecture
    public String getGroupeSanguin() {
        return groupeSanguin;
    }


    @Override
    public String toString() {
        return "Patient{"
                + "id='" + id + "'"
                + ", nom='" + nom + "'"
                + ", prenom='" + prenom + "'"
                + ", age=" + getAge()
                + ", telephone=" + getTelephone()
                + ", groupe sanguin=" + getGroupeSanguin()
                + "}";
    }

    @Override
    public boolean equals(Object obj) {
        // 1. Même référence mémoire ? → Forcément égal
        if (this == obj) return true;

        // 2. Null ou type différent ? → Pas égal
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. Comparer le contenu — ici, l'ID est l'identité
        Patient autre = (Patient) obj;
        return id.equals(autre.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();  // Basé sur le même champ que equals()
    }

    public String getId(){ return this.id; }
    public String getNom(){ return this.nom;}
    public String getPrenom(){ return this.prenom; }
    public LocalDate getDateNaissance(){ return this.dateNaissance; }
    public String getTelephone(){ return this.telephone; }

    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setTelephone(String telephone) {

        if (telephone == null || !telephone.matches("[+\\d\\s]{10,}")) {

            throw new IllegalArgumentException(
                    "Numéro invalide : " + telephone +
                            " (au moins 10 caractères, chiffres, espaces ou + uniquement)"
            );
        }

        this.telephone = telephone;
    }
}
