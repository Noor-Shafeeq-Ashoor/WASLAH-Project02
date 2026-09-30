package com.ga.waslah.model;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String phone;
    @Column
    private String bio;
    @Column
    private String location;
    @Column
    private String profileImage;
}
