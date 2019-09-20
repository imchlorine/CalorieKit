/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maclee
 */
@Entity
@Table(name = "CONSUMPTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consumption.findAll", query = "SELECT c FROM Consumption c")
    , @NamedQuery(name = "Consumption.findByConsid", query = "SELECT c FROM Consumption c WHERE c.consid = :consid")
    , @NamedQuery(name = "Consumption.findByConsdate", query = "SELECT c FROM Consumption c WHERE c.consdate = :consdate")
    , @NamedQuery(name = "Consumption.findByQuantity", query = "SELECT c FROM Consumption c WHERE c.quantity = :quantity")
    , @NamedQuery(name = "Consumption.findByFoodCategoryAndFirstName", query = "SELECT c FROM Consumption c WHERE c.foodid.category = :category AND lower(c.userid.firstname) = lower(:firstname)")})
public class Consumption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONSID")
    private Integer consid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONSDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date consdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private CalorieUser userid;
    @JoinColumn(name = "FOODID", referencedColumnName = "FOODID")
    @ManyToOne(optional = false)
    private Food foodid;

    public Consumption() {
    }

    public Consumption(Integer consid) {
        this.consid = consid;
    }

    public Consumption(Integer consid, Date consdate, int quantity) {
        this.consid = consid;
        this.consdate = consdate;
        this.quantity = quantity;
    }

    public Integer getConsid() {
        return consid;
    }

    public void setConsid(Integer consid) {
        this.consid = consid;
    }

    public Date getConsdate() {
        return consdate;
    }

    public void setConsdate(Date consdate) {
        this.consdate = consdate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CalorieUser getUserid() {
        return userid;
    }

    public void setUserid(CalorieUser userid) {
        this.userid = userid;
    }

    public Food getFoodid() {
        return foodid;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consid != null ? consid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consumption)) {
            return false;
        }
        Consumption other = (Consumption) object;
        if ((this.consid == null && other.consid != null) || (this.consid != null && !this.consid.equals(other.consid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "caloriekit.Consumption[ consid=" + consid + " ]";
    }
    
}
