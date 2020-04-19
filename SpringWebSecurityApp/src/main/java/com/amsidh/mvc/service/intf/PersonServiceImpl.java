package com.amsidh.mvc.service.intf;

import com.amsidh.mvc.model.Person;
import com.amsidh.mvc.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private static final List<Person> persons = new ArrayList<>();

    static {
        persons.add(new Person(1, "Amsidh Lokhande", 37));
        persons.add(new Person(2, "Anjali Lokhande", 33));
        persons.add(new Person(3, "Adithi Lokhande", 10));
        persons.add(new Person(4, "Aditya Lokhande", 6));
        persons.add(new Person(5, "Babu Lokhande", 72));
    }

    @Override
    public List<Person> getAllPerson() {
        return persons;
    }
}
