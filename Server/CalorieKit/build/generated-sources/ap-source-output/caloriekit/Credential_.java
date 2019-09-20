package caloriekit;

import caloriekit.CalorieUser;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-04T03:17:54")
@StaticMetamodel(Credential.class)
public class Credential_ { 

    public static volatile SingularAttribute<Credential, Date> signupdate;
    public static volatile SingularAttribute<Credential, String> password;
    public static volatile SingularAttribute<Credential, Integer> credid;
    public static volatile SingularAttribute<Credential, CalorieUser> userid;
    public static volatile SingularAttribute<Credential, String> username;

}