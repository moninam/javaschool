package app.model;

import framework.annotation.Entity;
import framework.annotation.Field;
import framework.annotation.JsonIgnore;

@Entity
public class Animal {
    @JsonIgnore
    private int id;
    @Field(name="Nombre")
    private String name;
    @Field(name="Raza")
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
