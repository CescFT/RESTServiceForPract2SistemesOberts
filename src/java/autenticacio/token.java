package autenticacio;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe embedded que persisteix la informació sobre el token
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Embeddable
@XmlRootElement
public class token implements Serializable {

    @Column(name = "TOKEN")
    @Size(max = 500)
    private String tokenAutoritzacio;

    /**
     * contructor buit
     */
    public token() {

    }

    /**
     * contructor
     *
     * @param token token
     */
    public token(String token) {
        this.tokenAutoritzacio = token;
    }

    /**
     * getter del token
     *
     * @return retorna el token
     */
    public String getTokenAutoritzacio() {
        return tokenAutoritzacio;
    }

    /**
     * setter del token
     *
     * @param tokenAutoritzacio token
     */
    public void setTokenAutoritzacio(String tokenAutoritzacio) {
        this.tokenAutoritzacio = tokenAutoritzacio;
    }

    /**
     * tostring
     *
     * @return cadena amb la informació del token
     */
    @Override
    public String toString() {
        return "token{" + "tokenAutoritzacio=" + tokenAutoritzacio + '}';
    }

    /**
     * Compara token i verifica que siguin iguals
     *
     * @param tk token a comparar
     * @return cert si son iguals i fals sino ho son
     */
    public boolean compararTokens(token tk) {
        return this.tokenAutoritzacio.equals(tk.getTokenAutoritzacio());
    }

}
