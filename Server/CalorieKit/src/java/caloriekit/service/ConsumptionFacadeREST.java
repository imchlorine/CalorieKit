/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import caloriekit.Consumption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

/**
 *
 * @author ylii0109
 */
@Stateless
@Path("consumption")
public class ConsumptionFacadeREST extends AbstractFacade<Consumption> {

    @PersistenceContext(unitName = "CalorieKitPU")
    private EntityManager em;

    public ConsumptionFacadeREST() {
        super(Consumption.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Consumption entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Consumption entity) {
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
    public Consumption find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
   
    //Task 3 a
    @GET
    @Path("findByUserID/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findByUserId(@PathParam("userid") Integer userId) {      
        String query = "SELECT c FROM Consumption c JOIN c.userid u WHERE u.userid = :userid"; 
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        q.setParameter("userid", userId);
        return q.getResultList();
    }
    
    @GET
    @Path("findByFoodID/{foodid}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findByFoodId(@PathParam("foodid") Integer foodId) {      
        String query = "SELECT c FROM Consumption c JOIN c.foodid f WHERE f.foodid = :foodid";  
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        q.setParameter("foodid", foodId);
        return q.getResultList();
    }
    
    @GET
    @Path("findByConsumptionDate/{consdate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findByConsDate(@PathParam("consdate") String consDate) throws ParseException {      
        String query = "SELECT c FROM Consumption c WHERE c.consdate = :consdate";
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        Date d =new SimpleDateFormat("yyyy-MM-dd").parse(consDate);  
        q.setParameter("consdate", d, TemporalType.DATE);
        return q.getResultList();
    }
    
    @GET
    @Path("findByquantity/{quantity}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findByQuantity(@PathParam("quantity") Integer quantity) {      
        String query = "SELECT c FROM Consumption c WHERE c.quantity = :quantity";    
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        q.setParameter("quantity", quantity);
        return q.getResultList();
    }
    
    //Task 3c
    @GET
    @Path("findByFoodNameAndFirstName/{foodname}/{firstname}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Consumption> findByFoodNameAndFirstName(@PathParam("foodname") String foodname,@PathParam("firstname") String firstname) {      
        String query = "SELECT c FROM Consumption c WHERE lower(c.foodid.foodname) like lower(:foodname) AND lower(c.userid.firstname) = lower(:firstname)";  
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        q.setParameter("foodname", "%" + foodname + "%");
        q.setParameter("firstname", firstname);
        return q.getResultList();
    }
    
    //Task 3 d
    @GET
    @Path("findByFoodCategoryAndFirstName/{category}/{firstname}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Consumption> findByFoodCategoryAndFirstName(@PathParam("category") String foodcategory,@PathParam("firstname") String firstname) {      
        Query query = em.createNamedQuery("Consumption.findByFoodCategoryAndFirstName");
        query.setParameter("category", foodcategory);
        query.setParameter("firstname", firstname);
        return query.getResultList();
    }
    
    
    //Task 4 d
    @GET
    @Path("totalcaloriesconsumed/{userid}/{consdate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTotalCaConsumedByDate(@PathParam("userid") Integer userId, @PathParam("consdate") String consDate) throws ParseException{
        int totalCaConsumedByDate = 0;       
        String query = "SELECT c FROM Consumption c WHERE c.userid.userid = :userid AND c.consdate = :consdate";  
        TypedQuery<Consumption> q = em.createQuery(query, Consumption.class);
        Date d =new SimpleDateFormat("yyyy-MM-dd").parse(consDate);  
        q.setParameter("userid", userId);
        q.setParameter("consdate", d, TemporalType.DATE);
        List<Consumption> consumptions = q.getResultList();
        for (Consumption cons : consumptions) {
            totalCaConsumedByDate = totalCaConsumedByDate 
                    + cons.getFoodid().getCalories() * cons.getQuantity();
        }
        return String.valueOf(totalCaConsumedByDate);
    }
    
}
