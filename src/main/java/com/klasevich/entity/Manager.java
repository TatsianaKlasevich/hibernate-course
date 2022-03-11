package com.klasevich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {

    private String projectName;

//    @Builder
//    public Manager(Long id, PersonalInfo personalInfo, String username, String info, Role role, Company company,
//                   Profile profile, List<UserChat> userChats, String projectName) {
//        super(id, personalInfo, username, info, role, company, profile, userChats);
//        this.projectName = projectName;
//    }

}
