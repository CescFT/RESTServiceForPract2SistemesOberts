package autenticacio;

import java.io.Serializable;

import java.util.Base64;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe entitat per a persistir les credencials del client web per a poder
 * entrar als mètodes REST del llogater. Emmagatzema el username, password i el
 * correu electronic.
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "credentialsClient.findAll", query = "SELECT c FROM credentialsClient c")
    ,
    @NamedQuery(name = "credentialsClient.matchUsername", query = "SELECT c FROM credentialsClient c WHERE c.username= :username")
})

public class credentialsClient implements Serializable {

    @Column(name = "USERNAME")
    @Size(max = 500)
    private String username;

    @Id
    @Column(name = "PASSWORD")
    @Size(max = 500)
    private String password;

    @Column(name = "E_MAIL")
    @Size(max = 500)
    private String email;

    @Embedded
    private token tokenAutoritzacio;

    /**
     * Contructor buit
     */
    public credentialsClient() {
    }

    /**
     * Contructor amb paràmetres
     *
     * @param username nom usuari
     * @param password contrassenya
     * @param email correu electronic
     */
    public credentialsClient(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * getter del token
     *
     * @return token
     */
    public token getTokenAutoritzacio() {

        return tokenAutoritzacio;
    }

    /**
     * setter del token
     *
     * @param tokenAutoritzacio token
     */
    public void setTokenAutoritzacio(token tokenAutoritzacio) {
        this.tokenAutoritzacio = tokenAutoritzacio;
    }

    /**
     * getter del mail
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter del mail
     *
     * @param email mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter que et retorna la password xifrada
     *
     * @return retorna la password xifrada
     */
    public String getCryptedPassword() {
        return password;
    }

    /**
     * getter del nom usuari
     *
     * @return nom usuari
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter del nom usuari
     *
     * @param username nom usuari
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter de la password desxifrada
     *
     * @return la password desxifrada
     */
    public String getPassword() {
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        return new String(decodedBytes);
    }

    /**
     * setter de la password, la xifra
     *
     * @param password emmagatzema la password xifrada
     */
    public void setPassword(String password) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    /**
     * tostring
     *
     * @return string amb les dades de les credencials
     */
    @Override
    public String toString() {
        return "credentialsClient{" + "username=" + username + ", password=" + password + ", email=" + email + ", tokenAutoritzacio=" + tokenAutoritzacio + '}';
    }

}
