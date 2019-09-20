package caloriekit;

import caloriekit.Consumption;
import caloriekit.Credential;
import caloriekit.Report;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-04T03:17:54")
@StaticMetamodel(CalorieUser.class)
public class CalorieUser_ { 

    public static volatile SingularAttribute<CalorieUser, String> firstname;
    public static volatile SingularAttribute<CalorieUser, String> address;
    public static volatile SingularAttribute<CalorieUser, Character> gender;
    public static volatile CollectionAttribute<CalorieUser, Consumption> consumptionCollection;
    public static volatile SingularAttribute<CalorieUser, String> postcode;
    public static volatile SingularAttribute<CalorieUser, Double> weight;
    public static volatile SingularAttribute<CalorieUser, Integer> userid;
    public static volatile SingularAttribute<CalorieUser, String> lastname;
    public static volatile SingularAttribute<CalorieUser, Integer> spm;
    public static volatile CollectionAttribute<CalorieUser, Report> reportCollection;
    public static volatile SingularAttribute<CalorieUser, Date> dob;
    public static volatile CollectionAttribute<CalorieUser, Credential> credentialCollection;
    public static volatile SingularAttribute<CalorieUser, String> email;
    public static volatile SingularAttribute<CalorieUser, Double> height;
    public static volatile SingularAttribute<CalorieUser, String> loa;

}