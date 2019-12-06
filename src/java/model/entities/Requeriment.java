package model.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Classe embedded que conte el requeriment d'una habitacio
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Embeddable
public class Requeriment implements Serializable {

    @Enumerated(EnumType.STRING)
    private SexeLlogater sexe;

    private int rangEdatMin;
    private int rangEdatMax;

    private Boolean fumador;

    private Boolean mascotes;

    /**
     * constructor buit
     */
    public Requeriment() {

    }

    /**
     * getter del sexe del requirment
     *
     * @return sexe del requeriment
     */
    public SexeLlogater getSexe() {
        return sexe;
    }

    /**
     * setter del sexe del requiment
     *
     * @param sexe sexe del requeriment
     */
    public void setSexe(SexeLlogater sexe) {
        this.sexe = sexe;
    }

    /**
     * getter de si accepta fumadors
     *
     * @return cert si accepta o fals si no ho accepta
     */
    public Boolean getFumador() {
        return fumador;
    }

    /**
     * setter de si accepta fumador
     *
     * @param fumador cert o fals
     */
    public void setFumador(Boolean fumador) {
        this.fumador = fumador;
    }

    /**
     * getter de si accepta mascotes
     *
     * @return cert si accepta, fals si no accepta
     */
    public Boolean getMascotes() {
        return mascotes;
    }

    /**
     * setter de si accepta mascotes
     *
     * @param mascotes cert o fals
     */
    public void setMascotes(Boolean mascotes) {
        this.mascotes = mascotes;
    }

    /**
     * getter del rang minim edat
     *
     * @return edat minima
     */
    public int getRangEdatMin() {
        return rangEdatMin;
    }

    /**
     * setter del rang edat min
     *
     * @param rangEdatMin edat minima
     */
    public void setRangEdatMin(int rangEdatMin) {
        if (rangEdatMin < 0) {
            this.rangEdatMin = 0;
        } else {
            this.rangEdatMin = rangEdatMin;
        }

        if (this.rangEdatMax < this.rangEdatMin) {
            this.rangEdatMin = this.rangEdatMax;
            this.rangEdatMax = this.rangEdatMin;
        }
    }

    /**
     * getter del rang edat max
     *
     * @return maxima edat
     */
    public int getRangEdatMax() {
        return rangEdatMax;
    }

    /**
     * setter del rang edat max
     *
     * @param rangEdatMax edat maxima
     */
    public void setRangEdatMax(int rangEdatMax) {
        if (rangEdatMax > 99) {
            this.rangEdatMax = 99;
        } else {
            this.rangEdatMax = rangEdatMax;
        }

        if (this.rangEdatMin > this.rangEdatMax) {
            this.rangEdatMax = this.rangEdatMin;
            this.rangEdatMin = this.rangEdatMax;
        }
    }

    /**
     * getter que retorna si accepta fumadors o no
     *
     * @return cert si accepta o fals si no accepta
     */
    public boolean isFumador() {
        return fumador;
    }

    /**
     * setter del requeriment per determinar si acceptem fumadors o no
     *
     * @param fumador cert o fals
     */
    public void setFumador(boolean fumador) {
        this.fumador = fumador;
    }

    /**
     * getter que retorna si s'accepten mascotes o no
     *
     * @return cert o fals
     */
    public boolean isMascotes() {
        return mascotes;
    }

    /**
     * setter per determinar si acceptem o no mascotes
     *
     * @param mascotes cert o fals
     */
    public void setMascotes(boolean mascotes) {
        this.mascotes = mascotes;
    }

    /**
     * tostring
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Requeriment{" + "sexe=" + sexe + ", rangEdatMin=" + rangEdatMin + ", rangEdatMax=" + rangEdatMax + ", fumador=" + fumador + ", mascotes=" + mascotes + '}';
    }

}
