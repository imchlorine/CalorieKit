/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import caloriekit.CalorieUser;
import caloriekit.Credential;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ylii0109
 */
@Stateless
@Path("credential")
public class CredentialFacadeREST extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "CalorieKitPU")
    private EntityManager em;

    public CredentialFacadeREST() {
        super(Credential.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Credential entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Credential entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Credential find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credential credential) {

        String username = credential.getUsername();
        String password = credential.getPassword();
        try {
            String query = "SELECT c FROM Credential c WHERE c.username = :username AND c.password = :password";
            // Authenticate the user using the credentials provided
            TypedQuery<Credential> q = em.createQuery(query, Credential.class);
            q.setParameter("username", username);
            q.setParameter("password", password);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                return Response.ok().build();
            }
            // Return the token on the response
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        // Authenticate the user, issue a token and return a response
    }

    // Credential 
    //Tasl 3 a
    @GET
    @Path("findByUserId/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findByUserId(@PathParam("userid") Integer userId) {
        String query = "SELECT c FROM Credential c JOIN c.userid u WHERE u.userid = :userid";
        TypedQuery<Credential> q = em.createQuery(query, Credential.class);
        q.setParameter("userid", userId);
        return q.getResultList();
    }

    @GET
    @Path("findByUserName/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findByUserName(@PathParam("username") String userName) {
        String query = "SELECT c FROM Credential c WHERE c.username = :username";
        TypedQuery<Credential> q = em.createQuery(query, Credential.class);
        q.setParameter("username", userName);
        return q.getResultList();
    }

    @GET
    @Path("findBySignUpDate/{signupdate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findBySignUpDate(@PathParam("signupdate") String signUpDate) throws ParseException {
        String query = "SELECT c FROM Credential c WHERE c.signupdate = :signupdate";
        TypedQuery<Credential> q = em.createQuery(query, Credential.class);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(signUpDate);
        q.setParameter("signupdate", d, TemporalType.DATE);
        return q.getResultList();
    }

    @POST
    @Path("createCredential")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCredential(Credential entity) {
        try {
            if (entity != null) {
                if (findByUserName(entity.getUsername()).isEmpty()) {
                    create(entity);
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.FOUND).build();
                }
            }
            // Return the token on the response
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
