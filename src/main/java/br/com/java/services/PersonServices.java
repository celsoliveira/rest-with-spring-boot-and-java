package br.com.java.services;

import br.com.java.data.dto.PersonDTO;
import br.com.java.exception.ResourceNotFoundException;
import static br.com.java.mapper.ObjectMapper.parseObject;
import static br.com.java.mapper.ObjectMapper.parseListObjects;
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


    public PersonDTO findById(Long id){
        logger.info("Finding one PersonDTO.");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        return parseObject(entity, PersonDTO.class);

        //mock
       /* PersonDTO PersonDTO = new PersonDTO();
        PersonDTO.setId(counter.incrementAndGet());
        PersonDTO.setFirstName("Leandro");
        PersonDTO.setLastName("Costa");
        PersonDTO.setAddress("Uberlandia - Minas Gerais - Brasil");
        PersonDTO.setGender("Male");*/
    }

    public List<PersonDTO> findAll(){
        logger.info("Finding all People.");

        return parseListObjects(repository.findAll(), PersonDTO.class);
        /*
        List<PersonDTO> PersonDTOs = new ArrayList<>();
        for(var i = 0; i < 8; i++){
            PersonDTO PersonDTO = MockPersonDTO(i);
            PersonDTOs.add(PersonDTO);
        }
        return PersonDTOs;*/
    }

    public PersonDTO create(PersonDTO person){
        logger.info("Creating one PersonDTO.");

        var entity = parseObject(person, Person.class);

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person){
        logger.info("Updating one PersonDTO.");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id){
        logger.info("Deleting one PersonDTO.");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        repository.delete(entity);
    }

    /*
    private PersonDTO MockPersonDTO(int i) {
        PersonDTO PersonDTO = new PersonDTO();
        PersonDTO.setId(counter.incrementAndGet());
        PersonDTO.setFirstName("FirstName " + i);
        PersonDTO.setLastName("LastName " + i);
        PersonDTO.setAddress("Some Address " + 1);
        PersonDTO.setGender("Male");

        return PersonDTO;
    }*/

}
