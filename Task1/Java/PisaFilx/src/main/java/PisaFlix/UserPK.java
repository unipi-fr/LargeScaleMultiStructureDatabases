/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PisaFlix;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author andre
 */
@Embeddable
public class UserPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "User")
    private String user;
    @Basic(optional = false)
    @Column(name = "idUser")
    private int idUser;

    public UserPK() {
    }

    public UserPK(String user, int idUser) {
        this.user = user;
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (user != null ? user.hashCode() : 0);
        hash += (int) idUser;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPK)) {
            return false;
        }
        UserPK other = (UserPK) object;
        if ((this.user == null && other.user != null) || (this.user != null && !this.user.equals(other.user))) {
            return false;
        }
        if (this.idUser != other.idUser) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.UserPK[ user=" + user + ", idUser=" + idUser + " ]";
    }
    
}
