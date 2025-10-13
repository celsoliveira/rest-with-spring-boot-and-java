package br.com.java.services;

import br.com.java.controllers.PersonController;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        var dto = parseObject(entity, PersonDTO.class);

        //Adicionando Hateoas
        addHateoasLinks(dto);
        return dto;
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

        var persons = parseListObjects(repository.findAll(), PersonDTO.class);
        persons.forEach(this::addHateoasLinks);
        //persons.forEach(p -> addHateoasLinks(p));
        return persons;
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

        var dto = parseObject(repository.save(entity), PersonDTO.class);

        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO person){
        logger.info("Updating one PersonDTO.");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting one PersonDTO.");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foun for this ID."));

        repository.delete(entity);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete"));
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
