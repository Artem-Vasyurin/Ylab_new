module myApp {
    requires com.fasterxml.jackson.databind;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires java.compiler;
    requires java.sql;
    requires jjwt.api;
    requires liquibase.core;
    requires static lombok;
    requires org.apache.tomcat.embed.core;
    requires org.mapstruct;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires spring.webmvc;
    requires aspectsModule;
    requires org.slf4j;
    requires org.aspectj.weaver;

    exports vasyurin.work.services;
    exports vasyurin.work.dto;

}