package main.java.model;

import main.java.annotation.Column;
import main.java.annotation.Entity;
import main.java.annotation.JsonIgnore;

@Entity
public class Animal {
    @JsonIgnore
    private int id;
    @Column(name="Nombre")
    private String name;
    @Column(name="Raza")
    private String breed;

    public Animal(){}

    public Animal(String name, String breed) {
        this.name = name;
        this.breed = breed;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Name: " + name + '\'' +
                ", Breed: '" + breed + '\'';
    }
}