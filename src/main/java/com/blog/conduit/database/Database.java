//package com.example.demo.demo.database;
//import models.com.blog.conduit.Product;
//import com.example.demo.demo.repositories.ProductRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class Database {
//    private static final Logger logger = LoggerFactory.getLogger(Database.class);
//    @Bean
//    CommandLineRunner initDatabase(ProductRepository productRepository){
//        //logger
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                Product productA = new Product("iphone",2020,2400.0,"");
//                Product productB = new Product("ipad",2021,2456.0,"");
//                logger.info("insert data "+productRepository.save(productA));
//                logger.info("insert data "+productRepository.save(productB));
//
//            }
//        };
//    }
//}
