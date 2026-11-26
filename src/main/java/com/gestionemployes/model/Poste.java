package com.gestionemployes.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "postes")
public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste")
    private Long idPoste;

    @Column(nullable = false, unique = true, length = 100)
    private String intitule;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "poste", fetch = FetchType.LAZY)
    private List<Employe> employes = new ArrayList<>();

    public Poste() {
    }

    public Long getIdPoste() { return idPoste; }
    public void setIdPoste(Long idPoste) { this.idPoste = idPoste; }

    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Employe> getEmployes() { return employes; }
    public void setEmployes(List<Employe> employes) { this.employes = employes; }
}
