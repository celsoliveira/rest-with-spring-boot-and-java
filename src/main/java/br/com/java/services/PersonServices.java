package br.com.java.services;

import br.com.java.exception.ResourceNotFoundException;
import br.com.java.model.Person;
import br.com.java.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;


    public Person findById(Long id){
        logger.info("Finding one Person.");

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));
        //mock
       /* Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro");
        person.setLastName("Costa");
        person.setAddress("Uberlandia - Minas Gerais - Brasil");
        person.setGender("Male");*/
    }

    public List<Person> findAll(){
        logger.info("Finding all People.");

        return repository.findAll();
        /*
        List<Person> persons = new ArrayList<>();
        for(var i = 0; i < 8; i++){
            Person person = MockPerson(i);
            persons.add(person);
        }
        return persons;*/
    }

    public Person create(Person person){
        logger.info("Creating one Person.");
        return repository.save(person);
    }

    public Person update(Person person){
        logger.info("Updating one Person.");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(entity);
    }

    public void delete(Long id){
        logger.info("Deleting one Person.");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        repository.delete(entity);
    }

    /*
    private Person MockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("FirstName " + i);
        person.setLastName("LastName " + i);
        person.setAddress("Some Address " + 1);
        person.setGender("Male");

        return person;
    }*/

}
