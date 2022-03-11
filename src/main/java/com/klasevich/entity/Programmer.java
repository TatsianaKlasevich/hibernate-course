package com.klasevich.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@PrimaryKeyJoinColumn(name = "id")
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;
//
//    @Builder
//    public Programmer(Long id, PersonalInfo personalInfo, String username, String info, Role role, Company company,
//                      Profile profile, List<UserChat> userChats, Language language) {
//        super(id, personalInfo, username, info, role, company, profile, userChats);
//        this.language = language;
//    }
}
