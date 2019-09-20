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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maclee
 */
@Entity
@Table(name = "CREDENTIAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credential.findAll", query = "SELECT c FROM Credential c")
    , @NamedQuery(name = "Credential.findByCredid", query = "SELECT c FROM Credential c WHERE c.credid = :credid")
    , @NamedQuery(name = "Credential.findByUsername", query = "SELECT c FROM Credential c WHERE c.username = :username")
    , @NamedQuery(name = "Credential.findByPassword", query = "SELECT c FROM Credential c WHERE c.password = :password")
    , @NamedQuery(name = "Credential.findBySignupdate", query = "SELECT c FROM Credential c WHERE c.signupdate = :signupdate")})
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CREDID")
    private Integer credid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SIGNUPDATE")
    @Temporal(TemporalType.DATE)
    private Date signupdate;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private CalorieUser userid;

    public Credential() {
    }

    public Credential(Integer credid) {
        this.credid = credid;
    }

    public Credential(Integer credid, String username, String password, Date signupdate) {
        this.credid = credid;
        this.username = username;
        this.password = password;
        this.signupdate = signupdate;
    }

    public Integer getCredid() {
        return credid;
    }

    public void setCredid(Integer credid) {
        this.credid = credid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
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
        hash += (credid != null ? credid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credential)) {
            return false;
        }
        Credential other = (Credential) object;
        if ((this.credid == null && other.credid != null) || (this.credid != null && !this.credid.equals(other.credid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "caloriekit.Credential[ credid=" + credid + " ]";
    }
    
}
