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
            return Response.status(Response.Status.OK).entity(hab).build();
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
     * Mètode HTTP GET per a retornar totes les habitacions disponibles.
     * @param criterion criteri (asc o desc)
     * @return totes les habitacions
     */
    @GET
    @Path("allRooms")
    @Produces({"application/json", "application/xml"})
    public Response allHabitacions(@QueryParam("sort") String criterion){
        List<Habitacio> llistaHabitacions = new ArrayList<Habitacio>();
        if(criterion == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("No hi ha criteri").build();
        try{
            llistaHabitacions = super.findRoomsWithCriteria(criterion);
            if(llistaHabitacions!= null){
                GenericEntity<List<Habitacio>> llista = new GenericEntity<List<Habitacio>>(llistaHabitacions) {
                };
                return Response.ok().entity(llista).build();
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity("No hi ha habitacions.").build();
            }
        }catch(NullPointerException e){
            return Response.status(Response.Status.NO_CONTENT).entity("null "+ criterion).build();
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
