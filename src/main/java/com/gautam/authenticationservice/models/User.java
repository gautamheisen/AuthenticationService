package com.gautam.authenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends BaseModel {
   private String email;
   private String password;
   private String fullName;
   @ManyToMany(fetch= FetchType.EAGER)
   private Set<Role> roles = new HashSet<>();

}
