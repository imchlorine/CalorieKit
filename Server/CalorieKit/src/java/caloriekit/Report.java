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
@Table(name = "REPORT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
    , @NamedQuery(name = "Report.findByReportid", query = "SELECT r FROM Report r WHERE r.reportid = :reportid")
    , @NamedQuery(name = "Report.findByCaloriesconsumed", query = "SELECT r FROM Report r WHERE r.caloriesconsumed = :caloriesconsumed")
    , @NamedQuery(name = "Report.findByCaloriesburned", query = "SELECT r FROM Report r WHERE r.caloriesburned = :caloriesburned")
    , @NamedQuery(name = "Report.findByStepstotal", query = "SELECT r FROM Report r WHERE r.stepstotal = :stepstotal")
    , @NamedQuery(name = "Report.findByCaloriegoals", query = "SELECT r FROM Report r WHERE r.caloriegoals = :caloriegoals")
    , @NamedQuery(name = "Report.findByReportdate", query = "SELECT r FROM Report r WHERE r.reportdate = :reportdate")})
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REPORTID")
    private Integer reportid;
    @Column(name = "CALORIESCONSUMED")
    private Integer caloriesconsumed;
    @Column(name = "CALORIESBURNED")
    private Integer caloriesburned;
    @Column(name = "STEPSTOTAL")
    private Integer stepstotal;
    @Column(name = "CALORIEGOALS")
    private Integer caloriegoals;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REPORTDATE")
    @Temporal(TemporalType.DATE)
    private Date reportdate;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private CalorieUser userid;

    public Report() {
    }

    public Report(Integer reportid) {
        this.reportid = reportid;
    }

    public Report(Integer reportid, Date reportdate) {
        this.reportid = reportid;
        this.reportdate = reportdate;
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public Integer getCaloriesconsumed() {
        return caloriesconsumed;
    }

    public void setCaloriesconsumed(Integer caloriesconsumed) {
        this.caloriesconsumed = caloriesconsumed;
    }

    public Integer getCaloriesburned() {
        return caloriesburned;
    }

    public void setCaloriesburned(Integer caloriesburned) {
        this.caloriesburned = caloriesburned;
    }

    public Integer getStepstotal() {
        return stepstotal;
    }

    public void setStepstotal(Integer stepstotal) {
        this.stepstotal = stepstotal;
    }

    public Integer getCaloriegoals() {
        return caloriegoals;
    }

    public void setCaloriegoals(Integer caloriegoals) {
        this.caloriegoals = caloriegoals;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

    public CalorieUser getUserid() {
        return userid;
    }

    public void setUserid(CalorieUser userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportid != null ? reportid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.reportid == null && other.reportid != null) || (this.reportid != null && !this.reportid.equals(other.reportid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "caloriekit.Report[ reportid=" + reportid + " ]";
    }
    
}
