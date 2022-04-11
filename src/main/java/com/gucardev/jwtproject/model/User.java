package com.gucardev.jwtproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.CascadeType.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(max = 50, min = 3)
    private String username;

    @NotEmpty
    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 50, min = 3)
    private String name;

    @Size(max = 50, min = 3)
    private String surname;

    @NotEmpty
    @Column(length = 60)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @CreatedDate
    @CreationTimestamp
    private LocalDate createdDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {MERGE, PERSIST, REFRESH, DETACH})
    private Collection<Role> roles = new ArrayList<>();


}
