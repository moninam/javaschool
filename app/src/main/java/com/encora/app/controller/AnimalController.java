package com.encora.app.controller;

import com.encora.framework.annotation.*;
import com.encora.app.model.Animal;
import com.encora.app.repository.AnimalRepository;

import java.util.List;

@RestController("/animals")
public class AnimalController {

    @Autowire
    private AnimalRepository repository;

    @GET
    public List<Animal> get(){
        return repository.getAnimals();
    }
    @GET("/{id}")
    public Animal get(@PathParam("id") int id){
        return repository.getAnimal(id);
    }
    @POST
    public void add(@Body Animal animal){
        repository.addAnimal(animal);
    }
    @PUT("/{id}")
    public void update(@Body Animal animal){
        repository.editAnimal(animal, animal.getId());
    }
    @DELETE("/{id}")
    public void delete(@PathParam("id") int id){
        repository.removeAnimal(id);
    }
}