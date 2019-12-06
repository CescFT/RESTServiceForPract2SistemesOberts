package model.entities;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe entitat que representa una habitacio i emmagatzema les dades que es
 * demanen a l'enunciat
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "room.findAllCondicionalASC", query = "SELECT r FROM Habitacio r WHERE r.ciutat =:location ORDER BY r.preuMes ASC")
    , //criterion ha de ser ASC o DESC
    @NamedQuery(name = "room.findAllCondicionalDESC", query = "SELECT r FROM Habitacio r WHERE r.ciutat =:location ORDER BY r.preuMes DESC")
    , //criterion ha de ser ASC o DESC
    //@NamedQuery(name="room.findAll", query="SELECT r FROM Habitacio r ORDER BY r.preuMes :criterion"),
    @NamedQuery(name = "room.information", query = "SELECT r FROM Habitacio r WHERE r.idHabitacio = :id")
    ,
    @NamedQuery(name = "room.updateInfo", query = "SELECT r FROM Habitacio r WHERE r.idHabitacio = :id")
    ,
    @NamedQuery(name = "room.deleteInfo", query = "SELECT r FROM Habitacio r WHERE r.idHabitacio = :id")
    ,
    @NamedQuery(name = "room.allRooms", query = "SELECT r FROM Habitacio r")
    ,
    @NamedQuery(name = "room.allRoomsASC", query = "SELECT r FROM Habitacio r ORDER BY r.preuMes ASC")
    ,
    @NamedQuery(name = "room.allRoomsDESC", query = "SELECT r FROM Habitacio r ORDER BY r.preuMes DESC")
})
public class Habitacio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "HABITACIO_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Habitacio_Gen")
    private Long idHabitacio;
    @Size(max = 500)
    @Column(name = "DESCRIPCIO")
    private String descripcio;
    @Column(name = "ADREÇA")
    @Size(max = 500)
    private String adresa;
    @Column(name = "CIUTAT")
    @Size(max = 500)
    private String ciutat;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPUS")
    private TipusHabitacio tipusHabitacio;

    @Column(name = "PREU_MES")
    private float preuMes;

    @Embedded
    private Requeriment requeriment;

    @OneToOne
    private Llogater llogater;

    /**
     * getter del tipus habitacio
     *
     * @return tipus habitacio
     */
    public TipusHabitacio getTipusHabitacio() {
        return tipusHabitacio;
    }

    /**
     * setter del tipus habitacio
     *
     * @param tipusHabitacio tipus habitacio
     */
    public void setTipusHabitacio(TipusHabitacio tipusHabitacio) {
        this.tipusHabitacio = tipusHabitacio;
    }

    /**
     * getter del llogater
     *
     * @return llogater
     */
    public Llogater getLlogater() {
        return llogater;
    }

    /**
     * setter del llogater
     *
     * @param llogater llogater
     */
    public void setLlogater(Llogater llogater) {
        this.llogater = llogater;
    }

    /**
     * contructor buit
     */
    public Habitacio() {

    }

    /**
     * getter del id
     *
     * @return identificador de l'habitacio
     */
    public Long getIdHabitacio() {
        return idHabitacio;
    }

    /**
     * setter del id de l'habitacio
     *
     * @param idHabitacio id habitacio
     */
    public void setIdHabitacio(Long idHabitacio) {
        this.idHabitacio = idHabitacio;
    }

    /**
     * getter de la descripcio
     *
     * @return descripcio
     */
    public String getDescripcio() {
        return descripcio;
    }

    /**
     * setter de la descripcio
     *
     * @param descripcio descripcio habitacio
     */
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    /**
     * getter de la adresa
     *
     * @return adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * setter de la adresa
     *
     * @param adresa adresa
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * getter de la ciutat
     *
     * @return ciutat
     */
    public String getCiutat() {
        return ciutat;
    }

    /**
     * setter de la ciutat
     *
     * @param ciutat ciutat
     */
    public void setCiutat(String ciutat) {
        this.ciutat = ciutat;
    }

    /**
     * getter del preu per mes
     *
     * @return preu per mes
     */
    public float getPreuMes() {
        return preuMes;
    }

    /**
     * setter del preu per mes
     *
     * @param preuMes preu per mes
     */
    public void setPreuMes(float preuMes) {
        this.preuMes = preuMes;
    }

    /**
     * getter del requeriment
     *
     * @return requeriment
     */
    public Requeriment getRequeriment() {
        return requeriment;
    }

    /**
     * setter del requeriment
     *
     * @param requeriment requeriment
     */
    public void setRequeriment(Requeriment requeriment) {
        this.requeriment = requeriment;
    }

    /**
     * override de equals
     *
     * @param object objecte a comparar
     * @return cert si iguals, fals si no son iguals
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Habitacio)) {
            return false;
        }
        Habitacio other = (Habitacio) object;
        if ((this.idHabitacio == null && other.idHabitacio != null) || (this.idHabitacio != null && !this.idHabitacio.equals(other.idHabitacio))) {
            return false;
        }
        return true;
    }

    /**
     * calcul del hashcode
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHabitacio != null ? idHabitacio.hashCode() : 0);
        return hash;
    }

    /**
     * tostring
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Habitacio{" + "idHabitacio=" + idHabitacio + ", descripcio=" + descripcio + ", adresa=" + adresa + ", ciutat=" + ciutat + ", tipusHabitacio=" + tipusHabitacio + ", preuMes=" + preuMes + ", requeriment=" + requeriment + ", llogater=" + llogater + '}';
    }

}
