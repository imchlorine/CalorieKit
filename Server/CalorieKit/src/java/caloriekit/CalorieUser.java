/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caloriekit;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author maclee
 */
@Entity
@Table(name = "CALORIE_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalorieUser.findAll", query = "SELECT c FROM CalorieUser c")
    , @NamedQuery(name = "CalorieUser.findByUserid", query = "SELECT c FROM CalorieUser c WHERE c.userid = :userid")
    , @NamedQuery(name = "CalorieUser.findByFirstname", query = "SELECT c FROM CalorieUser c WHERE c.firstname = :firstname")
    , @NamedQuery(name = "CalorieUser.findByLastname", query = "SELECT c FROM CalorieUser c WHERE c.lastname = :lastname")
    , @NamedQuery(name = "CalorieUser.findByEmail", query = "SELECT c FROM CalorieUser c WHERE c.email = :email")
    , @NamedQuery(name = "CalorieUser.findByDob", query = "SELECT c FROM CalorieUser c WHERE c.dob = :dob")
    , @NamedQuery(name = "CalorieUser.findByHeight", query = "SELECT c FROM CalorieUser c WHERE c.height = :height")
    , @NamedQuery(name = "CalorieUser.findByWeight", query = "SELECT c FROM CalorieUser c WHERE c.weight = :weight")
    , @NamedQuery(name = "CalorieUser.findByGender", query = "SELECT c FROM CalorieUser c WHERE c.gender = :gender")
    , @NamedQuery(name = "CalorieUser.findByAddress", query = "SELECT c FROM CalorieUser c WHERE c.address = :address")
    , @NamedQuery(name = "CalorieUser.findByPostcode", query = "SELECT c FROM CalorieUser c WHERE c.postcode = :postcode")
    , @NamedQuery(name = "CalorieUser.findByLoa", query = "SELECT c FROM CalorieUser c WHERE c.loa = :loa")
    , @NamedQuery(name = "CalorieUser.findBySpm", query = "SELECT c FROM CalorieUser c WHERE c.spm = :spm")})
public class CalorieUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USERID")
    private Integer userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LASTNAME")
    private String lastname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HEIGHT")
    private double height;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WEIGHT")
    private double weight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GENDER")
    private Character gender;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "POSTCODE")
    private String postcode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "LOA")
    private String loa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SPM")
    private int spm;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Credential> credentialCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Report> reportCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Consumption> consumptionCollection;

    public CalorieUser() {
    }

    public CalorieUser(Integer userid) {
        this.userid = userid;
    }

    public CalorieUser(Integer userid, String firstname, String lastname, String email, Date dob, double height, double weight, Character gender, String address, String postcode, String loa, int spm) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.loa = loa;
        this.spm = spm;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLoa() {
        return loa;
    }

    public void setLoa(String loa) {
        this.loa = loa;
    }

    public int getSpm() {
        return spm;
    }

    public void setSpm(int spm) {
        this.spm = spm;
    }

    @XmlTransient
    public Collection<Credential> getCredentialCollection() {
        return credentialCollection;
    }

    public void setCredentialCollection(Collection<Credential> credentialCollection) {
        this.credentialCollection = credentialCollection;
    }

    @XmlTransient
    public Collection<Report> getReportCollection() {
        return reportCollection;
    }

    public void setReportCollection(Collection<Report> reportCollection) {
        this.reportCollection = reportCollection;
    }

    @XmlTransient
    public Collection<Consumption> getConsumptionCollection() {
        return consumptionCollection;
    }

    public void setConsumptionCollection(Collection<Consumption> consumptionCollection) {
        this.consumptionCollection = consumptionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalorieUser)) {
            return false;
        }
        CalorieUser other = (CalorieUser) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "caloriekit.CalorieUser[ userid=" + userid + " ]";
    }
    
}
