/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import caloriekit.Report;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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

/**
 *
 * @author ylii0109
 */
@Stateless
@Path("report")
public class ReportFacadeREST extends AbstractFacade<Report> {

    @PersistenceContext(unitName = "CalorieKitPU")
    private EntityManager em;

    public ReportFacadeREST() {
        super(Report.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Report entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Report entity) {
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
    public Report find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    // Report
    @GET
    @Path("findByUserId/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findByUserId(@PathParam("userid") Integer userId) {
        String query = "SELECT c FROM Report r JOIN r.userid u WHERE u.userid = :userid";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        q.setParameter("userid", userId);
        return q.getResultList();
    }

    @GET
    @Path("findByCaloriesConsumed/{caloriesconsumed}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findByCaloriesConsumed(@PathParam("caloriesconsumed") Integer caloriesconsumed) {
        String query = "SELECT r FROM Report r WHERE r.calorieconsumed = :calorieconsumed";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        q.setParameter("calorieconsumed", caloriesconsumed);
        return q.getResultList();
    }

    @GET
    @Path("findByCaloriesBurned/{caloriesburned}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findByCaloriesBurned(@PathParam("caloriesburned") Integer caloriesBurned) {
        String query = "SELECT r FROM Report r WHERE r.caloriesburned = :caloriesburned";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        q.setParameter("caloriesburned", caloriesBurned);
        return q.getResultList();
    }

    @GET
    @Path("findByStepsTotal/{stepstotal}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findByStepsTotal(@PathParam("stepstotal") Integer stepsTotal) {
        String query = "SELECT r FROM Report r WHERE r.stepstotal = :stepstotal";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        q.setParameter("stepstotal", stepsTotal);
        return q.getResultList();
    }

    @GET
    @Path("findByCalorieGoals/{caloriegoals}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findByCalorieGoals(@PathParam("caloriegoals") Integer calorieGoals) {
        String query = "SELECT r FROM Report r WHERE r.caloriegoals = :caloriegoals";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        q.setParameter("caloriegoals", calorieGoals);
        return q.getResultList();
    }

    @GET
    @Path("findByReportDate/{reportdate}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findByCalories(@PathParam("reportdate") String reportDate) throws ParseException {
        String query = "SELECT r FROM Report r WHERE r.reportdate = :reportdate";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(reportDate);
        q.setParameter("reportdate", d, TemporalType.DATE);
        return q.getResultList();
    }

    //TODO 
    //Task 5 a 
    @GET
    @Path("findByUserAndDate/{userid}/{reportdate}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Object findByUserAndDate(@PathParam("userid") Integer userId, @PathParam("reportdate") String reportDate) throws ParseException {
        String query = "SELECT r FROM Report r WHERE r.userid.userid = :userid AND r.reportdate = :reportdate";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(reportDate);
        q.setParameter("reportdate", d, TemporalType.DATE);
        q.setParameter("userid", userId);
        List<Report> qList = q.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Report row : qList) {
            JsonObject personObject = Json.createObjectBuilder()
                    .add("totalconsumed", row.getCaloriesconsumed())
                    .add("totalBurned", row.getCaloriesburned())
                    .add("remaining", row.getCaloriesconsumed() - row.getCaloriegoals() - row.getCaloriesburned())
                    .build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }

    //Task 5 b
    @GET
    @Path("findByUserAndDateRange/{userid}/{startdate}/{enddate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Report> findByUserAndDateRange(@PathParam("userid") Integer userId, @PathParam("startdate") String startDate, @PathParam("enddate") String endDate) throws ParseException {
        String query = "SELECT r FROM Report r WHERE r.reportdate BETWEEN :startdate AND :enddate AND r.userid.userid = :userid";
        TypedQuery<Report> q = em.createQuery(query, Report.class);
        Date sd = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date ed = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        q.setParameter("startdate", sd, TemporalType.DATE);
        q.setParameter("enddate", ed, TemporalType.DATE);
        q.setParameter("userid", userId);
        List<Report> qList = q.getResultList();
//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        int totalCaloriesConsumed = 0;
//        int totalCaloriesBurned = 0;
//        int totalSteps = 0;
//        for (Report row : qList) {
//            totalCaloriesConsumed += row.getCaloriesconsumed();
//            totalCaloriesBurned += row.getCaloriesburned();
//            totalSteps += row.getStepstotal();
//        }
//        JsonObject personObject = Json.createObjectBuilder()
//                .add("totalconsumed", totalCaloriesConsumed)
//                .add("totalBurned", totalCaloriesBurned)
//                .add("totalSteps", totalSteps)
//                .build();
//        arrayBuilder.add(personObject);
//
//        JsonArray jArray = arrayBuilder.build();
        return qList;
    }

}
