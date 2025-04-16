package com.learning.spring_boot_library.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "History")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class History {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="checkout_date")
    private String checkoutDate;

    @Column(name="returned_date")
    private String returnedDate;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="description")
    private String description;

    @Column(name="img")
    private String img;
}
