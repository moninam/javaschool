package app.model;

public class Animal {
    private int id;
    private String name;
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
        return "name='" + name + '\'' +
                ", breed='" + breed + '\'';
    }
}
