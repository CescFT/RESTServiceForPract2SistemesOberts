package service;

import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.entities.Habitacio;

/**
 * API REST per a les habitacions, hereta de Abstract facade
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
@Stateless
@Path("/room")
public class HabitacioFacadeREST extends AbstractFacade<Habitacio> {

    @PersistenceContext(unitName = "Homework1PU")

    private EntityManager em;

    /**
     * Constructor de la habitacio
     */
    public HabitacioFacadeREST() {
        super(Habitacio.class);
    }

    /**
     * Mètode HTTP POST per a generar una nova habitacio en base a un JSON, es
     * crida quan la url és: /webresources/room
     *
     * @param entity habitacio
     * @return la habitacio emmagatzemada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createHabitacio(Habitacio entity) {
        if (entity == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No ve un JSON informat.").build();
        } else {
            //mirar els camps q s'omplen buits i omplir-los jo!
            super.create(entity);
            return Response.status(Response.Status.CREATED).entity("Nova entrada\n" + entity + "\nAfegida correctament.").build();
        }
    }

    /**
     * Mètode HTTP PUT que fa un update de la habitacio amb id passada pel path.
     * Es crida quan es fa: /webresources/room/id
     *
     * @param entity habitacio
     * @return la habitacio modificada
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editHabitacio(@PathParam("id") Integer id, Habitacio entity) {
        if (id == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No hi ha un id informat").build();
        }
        Habitacio h = super.find(Long.valueOf(id));
        if (entity == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("No ve un JSON informat").build();
        } else {
            if (h == null) {
                return Response.status(Response.Status.NO_CONTENT).entity("No hi ha cap habitació amb aquest id").build();
            }
            super.edit(entity);
            return Response.ok().entity(entity + "\nha estat modificada correctament.").build();
        }
    }

    /**
     * Mètode HTTP DELETE que elimina la habitació passada per paràmetre, es
     * crida quan la url és: /webresources/room/id
     *
     * @param id identificador
     * @return eliminacio de la habitacio
     */
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("Id no informat").build();
        }
        Habitacio hab = super.find(Long.valueOf(id));
        if (hab != null) {
            super.remove(hab);
            return Response.ok().entity("Habitacio esborrada correctament!").build();
        }

        return Response.status(Response.Status.NO_CONTENT).entity("Id no disponible").build();
    }

    /**
     * Mètode HTTP GET que s'executa quan la url és: /webresources/room/id
     * Retorna tota la informació de la habitació amb id passada pel path param
     *
     * @param id identificador habitacio
     * @return la informacio de tota la habitacio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Habitacio hab = super.find(Long.valueOf(id));
        if (hab != null) {
            return Response.ok(hab, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID: " + id + " no disponible.").build();
        }

    }

    /**
     * Mètode HTTP GET executat quan la URL es:
     * /webresources/room?location=Vallsandsort=asc
     * /webresources/room?locationandsort=asc Retorna les habitacions d'una
     * ciutat ordenades ascendentment o descendentment en funció del segon
     * paràmetre. El query param obligatori és el sort.
     *
     * @param location ciutat
     * @param criterion sort
     * @return llistat de habitacions de la ciutat passada per parametre o totes
     * si no esta
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@QueryParam("location") String location, @QueryParam("sort") String criterion) {
        List<Habitacio> llistaHabitacions = new ArrayList<Habitacio>();
        if (criterion == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Falta criteri.").build();
        }

        try {
            if (location != null && criterion != null) {
                //retornar les habitacions que siguin duna ciutat i el criteri de ordenacio
                llistaHabitacions = super.findRoomsWithCityAndCriteria(location, criterion);

            } else if (criterion != null) {
                //retornar totes les habitacions
                llistaHabitacions = super.findRoomsWithCriteria(criterion);

            }
            if (llistaHabitacions != null) {
                System.out.println(llistaHabitacions);
                GenericEntity<List<Habitacio>> llista = new GenericEntity<List<Habitacio>>(llistaHabitacions) {
                };
                return Response.ok().entity(llista).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Cal introduir una location vàlida o un criteri vàlid(asc/desc).").build();
            }

        } catch (NullPointerException e) {
            return Response.status(Response.Status.NO_CONTENT).entity("null " + criterion + " " + location).build();
        }

    }

    /**
     * Mètode privat que et retorna totes les habitacions
     *
     * @return llistat de totes les habitacions
     */
    private List<Habitacio> totesHabitacions() {
        return super.findAll();
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
