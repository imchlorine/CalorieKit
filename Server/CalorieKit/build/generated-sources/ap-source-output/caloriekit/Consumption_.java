package caloriekit;

import caloriekit.CalorieUser;
import caloriekit.Food;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-04T03:17:54")
@StaticMetamodel(Consumption.class)
public class Consumption_ { 

    public static volatile SingularAttribute<Consumption, Integer> consid;
    public static volatile SingularAttribute<Consumption, Integer> quantity;
    public static volatile SingularAttribute<Consumption, Food> foodid;
    public static volatile SingularAttribute<Consumption, CalorieUser> userid;
    public static volatile SingularAttribute<Consumption, Date> consdate;

}