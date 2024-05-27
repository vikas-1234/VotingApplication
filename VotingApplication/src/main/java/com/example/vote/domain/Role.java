package com.example.vote.domain;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data

public class Role{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "username")
    private String userName;
//    @Column(name = "votecount")
//    private int voteCount;
    @Column(name = "userrole")
    private String userRole;
    
    @ManyToMany(mappedBy = "roles")
//    @Column(name = "users")
    private Set<User> users;
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userRole);
    }

    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }


}
