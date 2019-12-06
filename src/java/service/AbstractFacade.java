package service;

import autenticacio.credentialsClient;
import autenticacio.token;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import model.entities.Habitacio;
import model.entities.Llogater;
import javax.validation.ConstraintViolationException;
import javax.validation.ConstraintViolation;

/**
 * Classe pare de totes les API REST, conte el control del Entity Manager és a
 * dir tot el container.
 *
 * @author Cesc Ferré Tarrés
 * @author Aleix Sancho Pujals
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    /**
     * contructor
     *
     * @param entityClass classe entitat
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * getter del entity manager
     *
     * @return entity manager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Mètode que permet fer un POST (fascilita la persistencia d'un element a
     * la base de dades)
     *
     * @param entity entitat
     */
    public void create(T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }

    }

    /**
     * Mètode que permet la cerca de tots els elements d'una entitat
     *
     * @return llista d'elements
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Mètode que fascilita el POST (fa un update dels camps nous i els altres
     * els deix com estaven).
     *
     * @param entity entitat
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Mètode que permet esborrar un element persistit en la base de dades
     * (facilita el DELETE)
     *
     * @param entity element a eliminar
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Mètode que cerca un element persistit a la base dades
     *
     * @param id identificador de la entitat
     * @return element
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Cerca l'habitació amb el id passat per paràmetre
     *
     * @param id identificador de una habitacio
     * @return habitacio
     */
    public Habitacio findWithId(Long id) {

        TypedQuery<Habitacio> query = (TypedQuery<Habitacio>) getEntityManager().createNamedQuery("room.information").setParameter("id", id);
        return query.getSingleResult();
    }

    /**
     * Mètode que cerca totes les habitacions
     *
     * @return totes les habitacions
     */
    public List<Habitacio> findAllRooms() {
        TypedQuery<Habitacio> query = (TypedQuery<Habitacio>) getEntityManager().createNamedQuery("room.allRooms");
        return query.getResultList();
    }

    /**
     * Metode que retorna la habitacio de un llogater
     *
     * @param ll llogater
     * @return habitacio
     */
    public Habitacio returnHabitacioClient(Llogater ll) {
        try {
            Habitacio hab = new Habitacio();
            List<Habitacio> llistaHabitacions = findAllRooms();
            for (Habitacio h : llistaHabitacions) {
                if (h.getLlogater().equals(ll)) {
                    hab = h;
                    break;
                }
            }
            return hab;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Mètode que verifica si un llogater té una habitació o no.
     *
     * @param llistaHabitacions llista de totes les habitacions
     * @param ll llogater
     * @return cert o fals
     */
    public boolean isTenant(List<Habitacio> llistaHabitacions, Llogater ll) {
        try {
            if (llistaHabitacions.isEmpty()) {
                return false;
            }

            for (Habitacio h : llistaHabitacions) {
                if (h.getLlogater().getId().equals(ll.getId())) {
                    return true;
                }
            }

            return false;
        } catch (NullPointerException e) {
            return false;
        }

    }

    /**
     * cerca les habitacions i les retorna ASC o DESC en funcio del paràmetre
     * criteria
     *
     * @param criteria criteri de sort
     * @return les habitacions ordenades
     */
    public List<Habitacio> findRoomsWithCriteria(String criteria) {
        TypedQuery<Habitacio> query;
        System.out.println(criteria);
        if (criteria.toUpperCase().equals("ASC")) {
            query = (TypedQuery<Habitacio>) getEntityManager()
                    .createNamedQuery("room.allRoomsASC");
        } else if (criteria.toUpperCase().equals("DESC")) {
            query = (TypedQuery<Habitacio>) getEntityManager()
                    .createNamedQuery("room.allRoomsDESC");
        } else {
            return null;
        }
        return query.getResultList();
    }

    /**
     * Mètode que et retorna les habitacions ordenades en funció del paràmetre
     * criteria d'una localitzacio específica
     *
     * @param location ciutat
     * @param criteria ASC o DESC
     * @return llista de les habitacions
     */
    public List<Habitacio> findRoomsWithCityAndCriteria(String location, String criteria) {
        TypedQuery<Habitacio> query;
        if (criteria.toUpperCase().equals("ASC")) {
            query = (TypedQuery<Habitacio>) getEntityManager()
                    .createNamedQuery("room.findAllCondicionalASC")
                    .setParameter("location", location);
        } else if (criteria.toUpperCase().equals("DESC")) {
            query = (TypedQuery<Habitacio>) getEntityManager()
                    .createNamedQuery("room.findAllCondicionalDESC")
                    .setParameter("location", location);
        } else {
            return null;
        }
        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return query.getResultList();
        }

    }

    /**
     * Mètode que retorna la llista de tots els llogaters
     *
     * @return llista de llogaters
     */
    public List<Llogater> findAllTenants() {
        TypedQuery<Llogater> query = (TypedQuery<Llogater>) getEntityManager()
                .createNamedQuery("tenant.findAll");
        return query.getResultList();
    }

    /**
     * Mètode que retorna una llista de tots els clients autoritzats
     *
     * @return llista clients autoritzats
     */
    public List<credentialsClient> findAllClientsAutoritzats() {
        TypedQuery<credentialsClient> query = (TypedQuery<credentialsClient>) getEntityManager().createNamedQuery("credentialsClient.findAll");
        return query.getResultList();
    }

    /**
     * Mètode que et cerca un client autoritzat en concret
     *
     * @param username nom usuari
     * @return client
     */
    public credentialsClient findClientAutoritizat(String username) {
        TypedQuery<credentialsClient> query = (TypedQuery<credentialsClient>) getEntityManager().createNamedQuery("credentialsClient.matchUsername").setParameter("username", username);
        return query.getSingleResult();
    }

    /**
     * Mètode que passat el token et permet saber qui ha fet la petició
     *
     * @param token token
     * @return client web que ha fet la petició
     */
    public credentialsClient whoDoneThisPetition(token token) {
        credentialsClient c = new credentialsClient();
        List<credentialsClient> llistaClients = findAllClientsAutoritzats();
        for (credentialsClient cli : llistaClients) {
            if (cli.getTokenAutoritzacio() != null) {
                if (cli.getTokenAutoritzacio().compararTokens(token)) {
                    c = cli;
                    break;
                }
            }
        }
        return c;
    }

    /**
     * Mètode que verifica el token passat per paràmetre i et permetrà entrar o
     * no a fer el mètode
     *
     * @param token token
     * @return cert o fals
     */
    public boolean tokenVerificat(token token) {
        System.out.println("::token a verificar:" + token);
        if (token == null) {
            return false;
        }
        try {
            List<credentialsClient> llistatClientsAutenticats = findAllClientsAutoritzats();
            if (llistatClientsAutenticats.isEmpty()) {
                return false;
            }

            boolean trobat = false;
            for (credentialsClient cli : llistatClientsAutenticats) {
                System.out.println("::client: " + cli);

                if (cli.getTokenAutoritzacio() != null) {

                    if (cli.getTokenAutoritzacio().compararTokens(token)) {
                        System.out.println(":: token: " + cli.getTokenAutoritzacio());
                        trobat = true;
                        break;
                    }
                }

            }

            if (trobat) {
                if (!token.getTokenAutoritzacio().contains("-")) {
                    return false;
                }

            } else {
                return false;
            }

        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

}
