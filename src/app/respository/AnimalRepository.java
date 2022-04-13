package app.respository;

import app.model.Animal;

import java.util.ArrayList;

public class AnimalRepository {
    private ArrayList<Animal> animals = new ArrayList<>();

    public AnimalRepository(){
        Animal animal1 = new Animal("Bob","Perro");
        animal1.setId(1);
        Animal animal2 = new Animal("Silvestre","Gato");
        animal2.setId(2);
        Animal animal3 = new Animal("Piolin","Pajaro");
        animal3.setId(3);
        Animal animal4 = new Animal("Marvin","Zebra");
        animal4.setId(4);
        Animal animal5 =  new Animal("Skipper","Pinguino");
        animal5.setId(5);

        animals.add(animal1);
        animals.add(animal2);
        animals.add(animal3);
        animals.add(animal4);
        animals.add(animal5);
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
        Animal last = animals.get(animals.size() - 1);
        int id = last.getId() + 1;
        animal.setId(id);
        animals.add(animal);
        return animal;
    }
    public Animal editAnimal(Animal anim,int index){
        String name = anim.getName();
        String breed = anim.getBreed();

        for(int i = 0; i < animals.size() ; i++){
            if(animals.get(i).getId() == index){
                Animal temp = animals.get(i);
                temp.setName(name);
                temp.setBreed(breed);
                animals.set(i,temp);
                return temp;

            }
        }

        return  null;
    }
    public Animal removeAnimal(int index){
        for(int i = 0; i < animals.size(); i++){
            if(animals.get(i).getId() == index){
                Animal temp = new Animal(animals.get(i).getName(),animals.get(i).getBreed());
                animals.remove(i);
                return temp;
            }
        }
        return null;
    }
}
