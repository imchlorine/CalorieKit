/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import caloriekit.CalorieUser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
@Path("calorieuser")
public class CalorieUserFacadeREST extends AbstractFacade<CalorieUser> {

    @PersistenceContext(unitName = "CalorieKitPU")
    private EntityManager em;

    public CalorieUserFacadeREST() {
        super(CalorieUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(CalorieUser entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, CalorieUser entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CalorieUser find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("findByFirstName/{firstName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByFirstName(@PathParam("firstName") String firstName) {      
        String query = "SELECT c FROM CalorieUser c WHERE lower(c.firstname) = lower(:firstname)";  
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("firstname", firstName);
        return q.getResultList();
    }
    
    @GET
    @Path("findByLastName/{lastName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByLastName(@PathParam("lastName") String lastName) {      
        String query = "SELECT c FROM CalorieUser c WHERE lower(c.lastname) = lower(:lastname)";  
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("lastname", lastName);
        return q.getResultList();
    }
    
    @GET
    @Path("findByEmail/{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByEmail(@PathParam("email") String email) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.email = :email"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("email", email);
        return q.getResultList();
    }
    
    @GET
    @Path("findByDOB/{dob}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByDOB(@PathParam("dob") String dob) throws ParseException {      
        String query = "SELECT c FROM CalorieUser c WHERE c.dob = :dob"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        q.setParameter("dob", d, TemporalType.DATE);
        return q.getResultList();
    }
    
    @GET
    @Path("findByHeight/{height}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByHeight(@PathParam("height") double height) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.height = :height";
         TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("height", height);
        return q.getResultList();
    }
    
    @GET
    @Path("findByWeight/{weight}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByWeight(@PathParam("weight") double weight) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.weight = :weight";  
         TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("weight", weight);
        return q.getResultList();
    }
    
    @GET
    @Path("findByGender/{gender}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByGender(@PathParam("gender") String gender) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.gender = :gender"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("gender", gender.charAt(0));
        return q.getResultList();
    }
    
    @GET
    @Path("findByAddress/{address}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByAddress(@PathParam("address") String address) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.address like :address";
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("address", "%" + address + "%");
        return q.getResultList();
    }
    
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByPostcode(@PathParam("postcode") String postcode) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.postcode = :postcode"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }
    
    @GET
    @Path("findByLoa/{loa}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByLoa(@PathParam("loa") String loa) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.loa = :loa";  
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("loa", loa);
        return q.getResultList();
    }
    
    @GET
    @Path("findBySpm/{spm}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findBySpm(@PathParam("spm") double spm) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.spm = :spm"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("spm", spm);
        return q.getResultList();
    }
    
    //Task 3 b
    @GET
    @Path("findByGenderAndPostCode/{gender}/{postcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CalorieUser> findByGenderAndPostCode(@PathParam("gender") String gender, @PathParam("postcode") String postcode) {      
        String query = "SELECT c FROM CalorieUser c WHERE c.gender = :gender AND c.postcode = :postcode"; 
        TypedQuery<CalorieUser> q = em.createQuery(query, CalorieUser.class);
        q.setParameter("gender", gender.charAt(0));
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }
    
    //Task 4 a
    @GET
    @Path("caloriesperstep/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCaloriesPerStep(@PathParam("userid") Integer userId){
        Double caloriesPerStep;
        CalorieUser user = find(userId);
        caloriesPerStep = (double) (user.getWeight() * 2.025 * 0.49 / user.getSpm());
        return String.valueOf(caloriesPerStep); 
    }
    
    
    // Task 4 b
    @GET
    @Path("bmr/{userid}")
    @Produces(MediaType.TEXT_PLAIN)      
    public String getBMR(@PathParam("userid") Integer userId){
        int bMR = 0;
        CalorieUser user = find(userId);
        int age = Toolkit.getAgeByDob(user.getDob());
//        Men: (13.75 × weight) + (5.003 × height) - (6.755 × age) + 66.5
//        Women: (9.563× weight) + (1.85 × height) - (4.676 × age) + 655.1
        if ('M' == user.getGender()){
            bMR = (int) ((13.75 * user.getWeight()) 
                    + (5.003 * user.getHeight())
                    - (6.755 * age)
                    + 66.5);
        }else if ('F' == user.getGender()) {
            bMR = (int) ((9.563 * user.getWeight()) 
                    + (1.85 * user.getHeight())
                    - (4.676 * age)
                    + 655.1);
        }
        return String.valueOf(bMR); 
    }
    
    //Task 4 c
    @GET
    @Path("bmrbyloa/{id}")
    @Produces(MediaType.TEXT_PLAIN)      
    public String getBMRByLoA(@PathParam("id") Integer id){
        int bMRByLoA = 0;
        int bMR = Integer.parseInt(getBMR(id));
        CalorieUser user = find(id);
//           1. Little/no exercise (sedentary): BMR * 1.2 = Total Calories
//           2. Lightly active (exercise/sports 1-3 days/week): BMR * 1.375 = Total Calories
//           3. Moderately active (exercise/sports 3-5 days/week): BMR * 1.55 = Total Calories
//           4. Very active (hard exercise/sports 6-7 days/wk): BMR * 1.725 = Total Calories
//           5. Extra active (very hard exercise/sports or training): BMR * 1.9 = Total Calories
      
        switch (user.getLoa()) {
            case "1": bMRByLoA = (int) (bMR * 1.2);
                      break;
            case "2": bMRByLoA = (int) (bMR * 1.375);
                      break;
            case "3": bMRByLoA = (int) (bMR * 1.55);
                      break;
            case "4": bMRByLoA = (int) (bMR * 1.725);
                      break;
            case "5": bMRByLoA = (int) (bMR * 1.9);
                      break;
            default: bMRByLoA = (int) 0.0;
         }
        return String.valueOf(bMRByLoA); 
    }
    
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(CalorieUser entity) {
        try {
            if (entity != null) {
                String email = entity.getEmail();
                
                if (findByEmail(email).isEmpty()) {
                     create(entity);
                     return Response.ok().build();
                }else{
                     return Response.status(Response.Status.FOUND).build();
                }
        }
            // Return the token on the response
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    @DELETE
    @Path("remove/{email}")
    public void removeIfUserNameExist(@PathParam("email") String email) {
        super.remove(findByEmail(email).get(0));
    }
}
