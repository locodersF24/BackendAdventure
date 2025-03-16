package org.example.backendadventure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerates id
    private int id;
    @Column(unique=true)
    private String name;
    @Column(nullable = false)
    private int maxNumberOfPeople;
    @Column(nullable = false)
    private int ageLimit;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    @JsonBackReference
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    @JsonBackReference
    private Set<Equipment> equipments = new HashSet<>();

    public Activity() {}

    public Activity(String name, int maxNumberOfPeople, int ageLimit) {
        this.name = name;
        this.maxNumberOfPeople = maxNumberOfPeople;
        this.ageLimit = ageLimit;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumberOfPeople() {
        return maxNumberOfPeople;
    }

    public void setMaxNumberOfPeople(int maxNumberOfPeople) {
        this.maxNumberOfPeople = maxNumberOfPeople;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }
}
