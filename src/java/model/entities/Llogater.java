package model.entities;

import javax.persistence.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe entitat que representa el llogater
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "tenant.findAll", query = "SELECT r FROM Llogater r")
    ,
    @NamedQuery(name = "tenant.information", query = "SELECT r FROM Llogater r WHERE r.id = :id"), //@NamedQuery(name="tenant.updateInfo", query="SELECT r FROM Llogater r WHERE r.id = :id"),
//@NamedQuery(name="tenant.deleteInfo", query="SELECT r FROM Llogater r WHERE r.id = :id")
})
public class Llogater implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Llogater_Gen")
    private Long id;

    @Embedded
    private informacioLlogater info;

    /**
     * getter del identificador
     *
     * @return identificador
     */
    public Long getId() {
        return id;
    }

    /**
     * setter del identificador
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter de la informacio del llogater
     *
     * @return informacio del llogater
     */
    public informacioLlogater getInfo() {
        return info;
    }

    /**
     * setter de la informacio del llogater
     *
     * @param info informacio del llogater
     */
    public void setInfo(informacioLlogater info) {
        this.info = info;
    }

    /**
     * override del equals
     *
     * @param object objecte a comparar
     * @return cert si igual, fals si no son iguals
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Llogater)) {
            return false;
        }
        Llogater other = (Llogater) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * tostring
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Llogater{" + "id=" + id + ", info=" + info + '}';
    }

}
