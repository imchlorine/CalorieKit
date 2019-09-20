package caloriekit;

import caloriekit.CalorieUser;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-04T03:17:54")
@StaticMetamodel(Report.class)
public class Report_ { 

    public static volatile SingularAttribute<Report, Integer> reportid;
    public static volatile SingularAttribute<Report, Integer> caloriesburned;
    public static volatile SingularAttribute<Report, Date> reportdate;
    public static volatile SingularAttribute<Report, Integer> stepstotal;
    public static volatile SingularAttribute<Report, Integer> caloriesconsumed;
    public static volatile SingularAttribute<Report, Integer> caloriegoals;
    public static volatile SingularAttribute<Report, CalorieUser> userid;

}