/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import caloriekit.Food;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("food")
public class FoodFacadeREST extends AbstractFacade<Food> {

    @PersistenceContext(unitName = "CalorieKitPU")
    private EntityManager em;

    public FoodFacadeREST() {
        super(Food.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Food entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Food entity) {
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
    public Food find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    
    // Food 
    @GET
    @Path("findByFoodName/{foodname}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findByFoodName(@PathParam("foodname") String foodName) {      
        String query = "SELECT f FROM Food f WHERE lower(f.foodname) like lower(:foodname)";          
        TypedQuery<Food> q = em.createQuery(query, Food.class);
        q.setParameter("foodname", "%" + foodName + "%");
        return q.getResultList();
    }
    
    @GET
    @Path("findByCategory/{category}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findByCategory(@PathParam("category") String category) {      
        String query = "SELECT f FROM Food f WHERE f.category = :category"; 
        TypedQuery<Food> q = em.createQuery(query, Food.class);
        q.setParameter("category", category);
        return q.getResultList();
    }
    
    @GET
    @Path("findByServingUnit/{servingunit}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findByServingUnit(@PathParam("servingunit") String servingUnit) {      
        String query = "SELECT f FROM Food f WHERE f.servingunit = :servingunit"; 
        TypedQuery<Food> q = em.createQuery(query, Food.class);
        q.setParameter("servingunit", servingUnit);
        return q.getResultList();
    }
    
    @GET
    @Path("findByServingAmount/{servingamount}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findByServingAmount(@PathParam("servingamount") Double servingAmount) {      
        String query = "SELECT f FROM Food f WHERE f.servingamount = :servingamount"; 
        TypedQuery<Food> q = em.createQuery(query, Food.class);
        q.setParameter("servingamount", servingAmount);
        return q.getResultList();
    }
    
    @GET
    @Path("findByFat/{fat}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Food> findByFat(@PathParam("fat") Integer fat) {      
        String query = "SELECT f FROM Food f WHERE f.fat = :fat"; 
        TypedQuery<Food> q = em.createQuery(query, Food.class);
        q.setParameter("fat", fat);
        return q.getResultList();
    }
    
    
    @POST
    @Path("addFood")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addFood(Food food){
        create(food);
        return  Response.ok().build();
    }
}
