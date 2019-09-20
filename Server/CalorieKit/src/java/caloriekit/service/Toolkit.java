/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit.service;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author maclee
 */
public class Toolkit {
    public static int getAgeByDob(Date dob){
       Calendar currentDate = Calendar.getInstance();
       Long time= currentDate.getTime().getTime() / 1000 - dob.getTime() / 1000;
       int years = Math.round(time) / 31536000;
       //int months = Math.round(time - years * 31536000) / 2628000;
       return years;
    }
}
