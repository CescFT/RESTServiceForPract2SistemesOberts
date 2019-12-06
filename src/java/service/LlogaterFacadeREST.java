package service;

import autenticacio.credentialsClient;
import autenticacio.token;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.entities.Habitacio;
import model.entities.Llogater;
import model.entities.Requeriment;
import model.entities.SexeLlogater;
import model.entities.informacioLlogater;

/**
 * API REST per al llogaters
 *
 * @author Cesc Ferrés Tarrés
 * @author Aleix Sancho Pujals
 */
@Stateless
@Path("tenant")
public class LlogaterFacadeREST extends AbstractFacade<Llogater> {

    @PersistenceContext(unitName = "Homework1PU")

    private EntityManager em;
    private token token;
    private credentialsClient client;

    /**
     * constructor classe
     */
    public LlogaterFacadeREST() {
        super(Llogater.class);

    }

    /**
     * setter del client
     *
     * @param client client
     */
    public void setClient(credentialsClient client) {
        this.client = client;
    }

    /**
     * getter del client
     *
     * @return client
     */
    public credentialsClient getClient() {
        return this.client;
    }

    /**
     * getter del token
     *
     * @return token
     */
    private token getToken() {
        return this.token;
    }

    /**
     * setter del token
     *
     * @param token token
     */
    private void setToken(token token) {
        this.token = token;
    }

    /**
     * Mètode HTTP POST que permet lligar la autenticació amb la API REST pels
     * llogaters. Emmagatzema el token per a poder executar els altres mètodes,
     * s'executa quan la url és: /webresources/tenant/processarToken i es passa
     * en json el token
     *
     * @param json token
     * @return token emmagatzemat correctament o no si es invalid
     */
    @POST
    @Path("/processarToken")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response processamentProva(token json) {

        System.out.println("::dada entrada" + json);
        try {
            if (super.tokenVerificat(json)) {
                this.setToken(json);
                this.setClient(super.whoDoneThisPetition(this.getToken()));
                return Response.ok().entity("Token emmagatzemat correctament:\n\n" + this.getToken() + "\nUsuari:" + this.getClient().getUsername()).build();
            } else {
                return Response.status(Response.Status.NO_CONTENT).entity("El token no es correcte").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Hi ha hagut algun error al processar el token.").build();
        }
    }

    /**
     * Mètode HTTP POST que permet fer el renting d'un llogater a una habitació.
     * S'executa quan la url és: /webresources/tenant/id/rent
     *
     * @param hab habitacio a llogar
     * @param id identifiador del llogater
     * @return la habitacio llogada
     */
    @POST
    @Path("{id}/rent")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rentingRoom(Habitacio hab, @PathParam("id") Integer id) {

        if (token != null) {
            if (hab == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("Per a fer anar aquest mètode has de passar una habitacio en JSON!").build();
            }
            if (id == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("ID nul, no es pot fer renting").build();
            }
            Llogater llogater = super.find(Long.valueOf(id));
            if (llogater == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("El llogater amb id: " + id + " no es troba a la base de dades.").build();
            } else {
                if (comprovarRequeriments(hab, llogater)) {
                    Habitacio hab1 = super.findWithId(hab.getIdHabitacio());
                    hab1.setLlogater(llogater);

                    getEntityManager().merge(hab1);
                    return Response.status(Response.Status.CREATED).entity(hab1 + "\n\nHa estat llogada per: " + llogater + "\n\nOperació finalitada.").build();
                } else {
                    return Response.status(Response.Status.NO_CONTENT).entity("No compleix els requisits.").build();
                }
            }

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }
    }

    /**
     * Mètode privat que verifica si el tenant compleix els requeriments de la
     * habitació i la pot llogar
     *
     * @param h habitacio
     * @param ll llogater
     * @return cert o fals
     */
    private boolean comprovarRequeriments(Habitacio h, Llogater ll) {
        informacioLlogater infoLlogater = ll.getInfo();
        Requeriment reqHab = h.getRequeriment();

        boolean compleix = true;

        if ((reqHab.isMascotes() == false) && (infoLlogater.isTeMascotes() == true)) {
            if ((reqHab.isFumador() == false) && (infoLlogater.isFumador() == true)) {
                if (infoLlogater.getEdat() < reqHab.getRangEdatMin() || infoLlogater.getEdat() > reqHab.getRangEdatMax()) {
                    compleix = false;
                }
            }
        }

        if (!compleix) {
            return false;
        } else {
            if (reqHab.getSexe() == SexeLlogater.UNISEX) {
                return true;
            } else if (infoLlogater.getSexe() == reqHab.getSexe()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mètode HTTP POST que crea un nou llogater a la base de dades. S'executa
     * quan la url és: /webresources/tenant
     *
     * @param entity llogater en JSON
     * @return llogater
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createLlogater(Llogater entity) {

        if (token != null) {
            if (entity == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("No es pot generar perquè no hi ha un JSON vàlid o informat").build();
            } else {
                super.create(entity);
                return Response.status(Response.Status.CREATED).entity("Llogater:\n " + entity + "\ncreat amb èxit.").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }

    }

    /**
     * Mètode HTTP PUT que actualitza la informació de un llogater, es crida
     * quan la url és: /webresources/tenant/id
     *
     * @param entity llogater en JSON
     * @return el llogater actualitzat
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editLlogater(@PathParam("id") Integer id, Llogater entity) {

        if (token != null) {
            Llogater ll = super.find(Long.valueOf(id));
            if (entity == null || ll == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("No hi ha JSON informat o és invàlid").build();
            } else {
                super.edit(entity);
                return Response.ok().entity("Llogater " + entity + "\n\n modificat correctament.").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }

    }

    /**
     * Mètode HTTP DELETE que allibera la habitacio i elimina el tenant.
     * S'executa quan la url és: /webresources/tenant/alliberarHabitacio/id
     *
     * @param id identificador del tenant
     * @return eliminacio del llogater de la habitacio i la eliminacio del
     * tenant
     */
    @DELETE
    @Path("alliberarHabitacio/{id}")
    public Response alliberarHabitacio(@PathParam("id") Integer id) {

        if (token != null) {
            Llogater tenant = super.find(Long.valueOf(id));
            if (tenant != null) {
                if (super.isTenant(super.findAllRooms(), tenant)) {
                    Habitacio hab = super.returnHabitacioClient(tenant);
                    hab.setLlogater(null);
                    getEntityManager().merge(hab);
                    super.remove(tenant);
                    return Response.ok().entity("Llogater eliminat correctament i la habitacio ara es lliure.").build();
                }
            }
            return Response.status(Response.Status.NO_CONTENT).entity(id + " no disponible").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }
    }

    /**
     * Mètode HTTP DELETE que elimina el llogater de la base de dades, s'executa
     * quan la url és: /webresources/tenant/id
     *
     * @param id identificador del tenant
     * @return eliminacio del llogater
     */
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {

        if (token != null) {
            Llogater tenant = super.find(Long.valueOf(id));
            if (tenant != null) {
                if (super.isTenant(super.findAllRooms(), tenant)) {
                    return Response.status(Response.Status.PARTIAL_CONTENT).entity("No es pot esborrar perque té una habitacio.").build();
                } else {
                    super.remove(tenant);
                    return Response.ok().entity("Llogater eliminat.").build();
                }

            }
            return Response.status(Response.Status.NO_CONTENT).entity(id + " no disponible").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }

    }

    /**
     * Mètode HTTP GET que cerca la informació del llogater amb el id passat per
     * paràmetre. Es crida quan la url és : /webresources/tenant/id
     *
     * @param id identificador del llogater
     * @return llogater
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Integer id) {
        try {
            if (token != null) {
                Llogater tenant = super.find(Long.valueOf(id));
                System.out.println(tenant);
                if (tenant != null) {
                    System.out.println("OK");
                    return Response.ok().entity((Llogater) tenant).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Id no correcte").build();
                }
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();

        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No s'ha trobat cap llogater amb aquesta ID.").build();
        }
        //return Response.ok().entity("hola").build();
    }

    /**
     * Mètode HTTP GET que retorna el llistat de totes les habitacions,
     * s'executa quan la url és: /webresources/tenant
     *
     * @return llistat de les habitacions
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response listOfTenants() {

        if (token != null) {
            try {
                List<Llogater> llogaters = super.findAllTenants();

                if (llogaters.isEmpty()) {
                    return Response.status(Response.Status.NO_CONTENT).entity("No hi ha llogaters.").build();
                } else {
                    GenericEntity<List<Llogater>> llista = new GenericEntity<List<Llogater>>(llogaters) {
                    };
                    return Response.ok().entity(llista).build();
                }
            } catch (NullPointerException e) {
                return Response.status(Response.Status.NO_CONTENT).entity("No hi ha llogaters, llista nula").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No t'has autenticat :(").build();
        }

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
