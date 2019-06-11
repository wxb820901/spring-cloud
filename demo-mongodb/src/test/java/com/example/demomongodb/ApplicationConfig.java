package com.example.demomongodb;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

@Configuration
@ComponentScan
@EnableMongoRepositories
public class ApplicationConfig extends AbstractMongoConfiguration {

    @Autowired
    private List<Converter<?, ?>> converters;

    public Mongo mongo() throws Exception {

        Mongo mongo = new Mongo();
        mongo.setWriteConcern(WriteConcern.SAFE);

        return mongo;
    }

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
//    private MongoClient mongo;
//    private DB db;
//    private MongodProcess mongod;
    @Override
    public MongoClient mongoClient() {
        MongoClient mongo = null;
        try {
            MongodExecutable mongodExecutable = starter.prepare(new MongodConfigBuilder() .version(Version.Main.PRODUCTION)
                    .net(new Net(12345, Network.localhostIsIPv6())).build());

            mongodExecutable.start();
            mongo = new MongoClient("localhost", 12345);
//            db = mongo.getDB("mydatabase");
        } catch (IOException e) {
            e.printStackTrace();
        }



        return mongo;
    }

    @Override
    protected String getDatabaseName() {
        return "e-store";
    }


}