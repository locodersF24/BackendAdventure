package org.example.backendadventure.model;

import jakarta.persistence.*;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "activity", referencedColumnName = "id")
    private Activity activity;

    private String name;
    private int quantity;
    private int peoplePerUnit;
    private int maxPeople;

    public Equipment(Activity activity, String name, int quantity, int peoplePerUnit, int maxPeople) {
        this.activity = activity;
        this.name = name;
        this.quantity = quantity;
        this.peoplePerUnit = peoplePerUnit;
        this.maxPeople = maxPeople;
    }

    public Equipment() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPeoplePerUnit() {
        return peoplePerUnit;
    }

    public void setPeoplePerUnit(int peoplePerUnit) {
        this.peoplePerUnit = peoplePerUnit;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }
}
