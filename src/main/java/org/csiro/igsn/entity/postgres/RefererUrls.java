package org.csiro.igsn.entity.postgres;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Entity
@Table(name = "registrant_referer_urls")
@NamedQueries(
        @NamedQuery(
                name="RefererUrls.searchByRefererUrl",
                query="SELECT r FROM RefererUrls r where lower(r.referer_url) = lower(:referer_url)"
        ))
public class RefererUrls implements java.io.Serializable {
    private int id;
    private int registrantid;
    private String referer_url;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "registrantid")
    public int getRegistrantid() {
        return registrantid;
    }

    public void setRegistrantid(int registrantid) {
        this.registrantid = registrantid;
    }
    @Column(name = "referer_url")
    public String getReferer_url() {
        return referer_url;
    }

    public void setReferer_url(String referer_url) {
        this.referer_url = referer_url;
    }

// TODO: from tomorrow
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "registrant", joinColumns = { @JoinColumn(name = "registrantid", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "registrant", nullable = false, updatable = false) })
//    public Set<Registrant> getRegistrants() {
//        return this.registrants;
//    }
//
//    public void setRegistrants(Set<Registrant> registrants) {
//        this.registrants = registrants;
//    }


}