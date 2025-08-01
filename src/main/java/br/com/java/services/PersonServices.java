package br.com.java.services;

import br.com.java.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById(String id){
        logger.info("Finding one Person.");

        //mock
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro");
        person.setLastName("Costa");
        person.setAddress("Uberlandia - Minas Gerais - Brasil");
        person.setGender("Male");

        return person;
    }

    public List<Person> findAll(){
        logger.info("Finding all People.");

        List<Person> persons = new ArrayList<>();
        for(var i = 0; i < 8; i++){
            Person person = MockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person create(Person person){
        logger.info("Creating one Person.");
        return person;
    }

    public Person update(Person person){
        logger.info("Updating one Person.");
        return person;
    }

    public void delete(String id){
        logger.info("Deleting one Person.");
    }

    private Person MockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("FirstName " + i);
        person.setLastName("LastName " + i);
        person.setAddress("Some Address " + 1);
        person.setGender("Male");

        return person;
    }

}
