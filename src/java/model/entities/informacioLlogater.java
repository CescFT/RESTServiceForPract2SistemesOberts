package model.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

/**
 * Classe embedded que conte la informació per a un llogater
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Embeddable
public class informacioLlogater implements Serializable {

    @Column(name = "NOM_LLOGATER")
    @Size(max = 500)
    private String nom;
    @Column(name = "COGNOM_LLOGATER")
    @Size(max = 500)
    private String cognom;

    @Column(name = "NIF")
    @Size(max = 500)
    private String dni;

    @Column(name = "EDAT_LLOGATER")
    private int edat;

    private boolean fumador;

    private boolean teMascotes;

    @Enumerated(EnumType.STRING)
    private SexeLlogater sexe;

    /**
     * getter del dni
     *
     * @return dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * setter del dni
     *
     * @param dni dni
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * getter de la edat
     *
     * @return edat del llogater
     */
    public int getEdat() {
        return edat;
    }

    /**
     * setter la edat
     *
     * @param edat edat del llogater
     */
    public void setEdat(int edat) {
        this.edat = edat;
    }

    /**
     * getter per saber si es fumador el llogater
     *
     * @return cert o fals
     */
    public boolean isFumador() {
        return fumador;
    }

    /**
     * setter per establir si fuma o no el llogater
     *
     * @param fumador cert o fals
     */
    public void setFumador(boolean fumador) {
        this.fumador = fumador;
    }

    /**
     * getter per saber si te mascotes o no el llogater
     *
     * @return cert o fals
     */
    public boolean isTeMascotes() {
        return teMascotes;
    }

    /**
     * setter per saber el llogater te mascotes
     *
     * @param teMascotes cert o fals
     */
    public void setTeMascotes(boolean teMascotes) {
        this.teMascotes = teMascotes;
    }

    /**
     * getter del nom del llogater
     *
     * @return nom llogater
     */
    public String getNom() {
        return nom;
    }

    /**
     * setter del nom del llogater
     *
     * @param nom nom llogater
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * getter del cognom del llogater
     *
     * @return cognom
     */
    public String getCognom() {
        return cognom;
    }

    /**
     * setter del congnom
     *
     * @param cognom cognnom
     */
    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    /**
     * getter del sexe del llogater
     *
     * @return sexe
     */
    public SexeLlogater getSexe() {
        return sexe;
    }

    /**
     * setter del sexe del llogater
     *
     * @param sexe sexe llogater
     */
    public void setSexe(SexeLlogater sexe) {
        this.sexe = sexe;
    }

    /**
     * tostring
     *
     * @return string
     */
    @Override
    public String toString() {
        return "informacioLlogater{" + "nom=" + nom + ", cognom=" + cognom + ", edat=" + edat + ", fumador=" + fumador + ", teMascotes=" + teMascotes + ", sexe=" + sexe + '}';
    }

}
