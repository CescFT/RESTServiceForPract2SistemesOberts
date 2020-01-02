/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import autenticacio.credentialsClient;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import autenticacio.token;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API REST per l'autenticació.
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 *
 * Per tal de autenticar, quan executem el install.jsp, ens hem de quedar amb la
 * password i el username que ens dona, aixo es clau per a que ens retorni el
 * token (únic per a cada sessió i persistent). Recordem que la contrassenya es
 * xifra i es guarda a la bd, si la volem recuperar hi ha un metode que ens la
 * recupera, però es imprescindible el token.
 */
@Stateless
@Path("/autenticacio")
public class autenticacioClientWeb extends AbstractFacade<credentialsClient> {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    /**
     * constructor classe
     */
    public autenticacioClientWeb() {
        super(credentialsClient.class);
    }

    /**
     * Aixo es fer un reset password
     *
     * @param client
     * @return
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/modifyPassword")
    public Response modifyPassword(credentialsClient client) {
        if (client == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha informació").build();
        }
        if (client.getUsername().equals("") || null == client.getUsername()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha el nom d'usuari.").build();
        }

        if (client.getPassword().equals("") || null == client.getPassword()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha la contrassenya.").build();
        }

        try {
            credentialsClient clientW = super.findClientAutoritizat(client.getUsername());
            if (client.getPassword().length() < 8) {
                return Response.status(Response.Status.BAD_REQUEST).entity("La contrassenya ha de tenir una llargada superior a 8 caracters").build();
            }
            clientW.setPassword(client.getPassword());
            super.edit(clientW);
            return Response.status(Response.Status.OK).entity(clientW).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha cap usuari amb aquest username.").build();
        }

    }

    /**
     * Aixo es fer un registre en la pagina web
     *
     * @param client
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/newClient")
    public Response createClientWeb(credentialsClient client) {
        if (client == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha informació.").build();
        }

        if (client.getUsername().equals("") || null == client.getUsername()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha el nom d'usuari.").build();
        }

        if (client.getPassword().equals("") || null == client.getPassword()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha la contrassenya.").build();
        }

        if (client.getEmail() == null || client.getEmail().equals("")) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha correu electronic.").build();
        }

        try {
            credentialsClient clientW = super.findClientAutoritizat(client.getUsername());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Ja existeix un usuari amb aquest nom").build();
        } catch (Exception e) {
            if (client.getPassword().length() < 8) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("La contrassenya no es segura. Més de 8 caracters.").build();
            }
            client.setAutenticat(Boolean.FALSE);
            super.create(client);
        }

        return Response.status(Response.Status.CREATED).entity(client).build();
        //return Response.status(Response.Status.OK).entity(client).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("logoutUser")
    public Response logoutUser(credentialsClient client) {
        //return Response.status(Response.Status.OK).entity("hola").build();
        if (client == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("client null").build();
        }
        try{
            List<credentialsClient> llistaClients = super.findAllClientsAutoritzats();
            for(credentialsClient c : llistaClients){
                if(c.getUsername().equals(client.getUsername())){
                    if(c.getAutenticat() == true){
                        c.setAutenticat(Boolean.FALSE);
                        c.setTokenAutoritzacio(null);
                        super.edit(c);
                        return Response.status(Response.Status.OK).entity(c).build();
                    }
                }
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("no autenticat").build();
        }catch(NullPointerException e){
            return Response.status(Response.Status.FORBIDDEN).entity("Algun error ha succeit.").build();
        }
        /*try {
            List<credentialsClient> llistaClients = super.findAllClientsAutoritzats();
            for (credentialsClient c : llistaClients) {
                if ((c.getAutenticat() == true) && (c.getUsername().equals(client.getUsername()))) {
                    c.setAutenticat(Boolean.FALSE);
                    c.setTokenAutoritzacio(null);
                    super.edit(c);
                    return Response.status(Response.Status.OK).entity(client).build();
                }
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("no autenticat").build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.FORBIDDEN).entity("ha hagut algun error").build();
        }*/
    }

    /**
     * Mètode HTTP POST que permet l'autenticació amb els clients. Comprova que
     * la contrassenya sigui correcte i coincideixi amb la del client. Si és
     * així retorna el token del client per continuar amb l'autenticació. Es
     * crida quan la url és : /webresources/autenticacio ES FER UN LOGIN
     *
     * @param client nom del client
     * @return token del client
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticationClient(credentialsClient client) {
        if (client == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("client null").build();
        }
        if (client.getUsername() == null || client.getPassword() == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Falta usuari o contrassenya").build();
        }
        try {
            //Autenticació de l'usuari fent servir les credencials donades
            if (authenticateClient(client.getUsername(), client.getPassword())) {
                String token = getToken(client.getUsername());
                credentialsClient c = super.findClientAutoritizat(client.getUsername());
                if (c.getTokenAutoritzacio() == null) {
                    c.setTokenAutoritzacio(new token(token));
                    c.setAutenticat(Boolean.TRUE);
                    super.edit(c);
                    return Response.status(Response.Status.OK).entity(c.getTokenAutoritzacio()).build();
                } else {
                    return Response.status(Response.Status.OK).entity(c.getTokenAutoritzacio()).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No ets un client autoritzat.").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    /**
     * Mètode HTTP GET que cerca si el client està autenticat i retorna el seu
     * token. Es crida quan la url és : /webresources/autenticacio/{username}
     *
     * @param username nom del client
     * @return token del client si esta autenticat
     */
    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTokenOfUsername(@PathParam("username") String username) {
        credentialsClient c = super.findClientAutoritizat(username);
        if (c == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No es troba aquest usuari web").build();
        } else {
            return Response.ok().entity(c).build();
        }
    }

    /**
     * Mètode HTTP GET cerca si el client esta autenticat i si el token es
     * correcte. Si es així retorna la contrassenya del client. Es crida quan la
     * url és : /webresources/autenticacio/{username}/{token}
     *
     * @param username nom del client
     * @param token token del client
     * @return la contrassenya del client en clar
     */
    @GET
    @Path("{username}/{token}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMevaContrassenya(@PathParam("username") String username, @PathParam("token") String token) {

        credentialsClient c = super.findClientAutoritizat(username);
        if (token == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha token").build();
        } else {
            String substring = "-";
            if (!token.contains(substring)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Token invàlid. No té una forma autoritzada.").build();
            } else {
                if (username == null) {
                    return Response.status(Response.Status.NOT_FOUND).entity("Username no informat.").build();
                }
                if (c == null) {
                    return Response.status(Response.Status.NOT_FOUND).entity("No hi ha un usuari amb aquest username: " + username).build();
                }

                token tk = new token();
                tk.setTokenAutoritzacio(token);
                if (!c.getTokenAutoritzacio().compararTokens(tk)) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("El username " + username + " no té aquest token.").build();
                }
            }
        }
        return Response.ok().entity(c.getPassword()).build();
    }

    /**
     * Mètode HTTP GET que cerca si el client està autenticat i el retorna.
     *
     * @param username nom del client
     * @return tot el client.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getClientAutenticate(@QueryParam("username") String username) {
        if (username == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("El nom d'usuari és null o buit.").build();
        }

        credentialsClient c = super.findClientAutoritizat(username);
        if (c != null) {
            return Response.ok().entity(c).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No s'ha trobat cap client amb aquest username").build();
        }
    }

    /**
     * Mètode HTTP GET que cerca tots els clients autenticats i els retorna.
     *
     * @return tots els clients autenticats
     */
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllClientsAutoritzats() {
        List<credentialsClient> llista = new ArrayList<credentialsClient>();

        try {
            llista = super.findAllClientsAutoritzats();
            if (llista.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No hi ha clients autoritzats").build();
            } else {
                GenericEntity<List<credentialsClient>> llistacli = new GenericEntity<List<credentialsClient>>(llista) {
                };
                return Response.ok().entity(llistacli).build();
            }
        } catch (NullPointerException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No hi ha clients autoritzats").build();
        }

    }

    /**
     * Mètode que comprova si el client està autenticat i per tant si el seu nom
     * apareix a la base de dades i les contrassenyes coincideixen.
     *
     * @param username nom del client
     * @param passwd contrassenya del client
     * @return true si el client està autenticat i false si no ho està
     * @throws Exception si el client no existeix
     */
    private boolean authenticateClient(String username, String passwd) throws Exception {
        credentialsClient c = super.findClientAutoritizat(username);
        System.out.println(c);
        if (c == null) {
            return false;
        } else {
            if (c.getPassword().equals(passwd)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Mètode que a partir del nom del client crea un token aleatòri assignat a
     * aquest
     *
     * @param username nom del client
     * @return token del client
     */
    private String getToken(String username) {
        String alphaNumericString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int cmpt = 0;
        StringBuilder stringb = new StringBuilder();
        while (cmpt < 32) {
            int posRand = (int) (Math.random() * alphaNumericString.length());
            stringb.append(alphaNumericString.charAt(posRand));
            cmpt++;
        }

        return username + "-" + stringb.toString();
    }

    /**
     * getter del entity manager
     *
     * @return entity manager
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
