package caloriekit;

import caloriekit.Consumption;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-04T03:17:54")
@StaticMetamodel(Food.class)
public class Food_ { 

    public static volatile SingularAttribute<Food, String> foodname;
    public static volatile CollectionAttribute<Food, Consumption> consumptionCollection;
    public static volatile SingularAttribute<Food, Double> servingamount;
    public static volatile SingularAttribute<Food, Integer> foodid;
    public static volatile SingularAttribute<Food, Integer> fat;
    public static volatile SingularAttribute<Food, Integer> calories;
    public static volatile SingularAttribute<Food, String> category;
    public static volatile SingularAttribute<Food, String> servingunit;

}