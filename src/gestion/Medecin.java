package gestion;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Medecin {

    private String id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String telephone;
    private String specialite;
    private String matricule;

    public Medecin(String id, String nom, String prenom,
                   LocalDate dateNaissance, String specialite,
                   String matricule) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.specialite = specialite;
        this.matricule = matricule;
    }

    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now())
                .getYears();
    }

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getSpecialite() { return specialite; }
    public String getMatricule() { return matricule; }

    @Override
    public String toString() {
        return "Dr. " + prenom + " " + nom
                + " — " + specialite
                + " (Mat: " + matricule + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medecin autre = (Medecin) obj;
        return id.equals(autre.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
