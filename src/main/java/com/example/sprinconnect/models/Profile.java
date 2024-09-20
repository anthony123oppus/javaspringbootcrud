package com.example.sprinconnect.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Permanent Address must not be blank")
    private String permanentAddress;

    @Column(nullable = false)
    @NotBlank(message = "Phone Number must not be blank")
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank(message = "Current Address must not be blank")
    private String currentAddress;

    @OneToOne(mappedBy = "userProfile")
    @JsonBackReference //para dili mag infinite loop
    private User user;
}
