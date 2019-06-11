package com.example.demomongodb;


import com.mongodb.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
public class AbstractIntegrationTest {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    Mongo mongo;

    @Before
    public void setUp() throws IOException {

        System.out.println("mongoTemplate ==> "+mongoTemplate);
        System.out.println("mongo ==> "+mongo);

    }

    @Test
    public void test() {
        System.out.println(" ==> begin");
        DocumentEntity test = new DocumentEntity();
        test.setName("bill");
        mongoTemplate.save(test, "customer");
        System.out.println(" ==> "+mongoTemplate.findAll(DocumentEntity.class,"customer"));
    }
}
