package respository;

import model.Animal;

import java.util.ArrayList;

public class AnimalRepository {
    private ArrayList<Animal> animals = new ArrayList<>();

    public AnimalRepository(){
       animals.add(new Animal("Bob","Perro"));
       animals.add(new Animal("Silvestre","Gato"));
       animals.add(new Animal("Piolin","Pajaro"));
       animals.add(new Animal("Marvin","Zebra"));
       animals.add(new Animal("Skipper","Pinguino"));
    }
    public ArrayList<Animal> getAnimals(){
        return animals;
    }
    public Animal getAnimal(int i){
        if(i >= 0 && i < animals.size()){
            return animals.get(i);
        }
        return null;
    }
    public Animal addAnimal(Animal animal){
        animals.add(animal);
        return animal;
    }
    public Animal editAnimal(Animal anim,int i){
        String name = anim.getName();
        String breed = anim.getBreed();

        if(i >= 0 & i< animals.size()){
            Animal animal = animals.get(i);
            animal.setName(name);
            animal.setBreed(breed);
            animals.set(i,animal);
            return animal;
        }
        return  null;
    }
    public Animal removeAnimal(int i){
        if(i>= 0 & i< animals.size()){
            Animal animal = animals.get(i);
            if(animal != null){
                animals.remove(i);
                return animal;
            }
        }
        return null;
    }
}
