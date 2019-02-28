package com.example.demoredis;

import com.example.demorestrepo.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRep pr;

    public List<Person> getPersons(){
        Iterable<Person> persons = pr.findAll();
        List<Person> datas = new ArrayList<>();
        for(Iterator<Person> it = persons.iterator(); it.hasNext();){
            Person p = it.next();
            datas.add(p);

        }
        return datas;
    }

    public void save(String name ){
        Person p = new Person();
        p.setAge(1);
        p.setName(name);
        pr.save(p);

    }
}
