module site.pokemons.edpproject {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires jakarta.persistence;
    requires static lombok;

    opens site.pokemons.edpproject to javafx.fxml;
    exports site.pokemons.edpproject;

    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.fasterxml.jackson.databind;
    requires jakarta.mail;
    requires org.json;
    requires javafx.web;
    requires java.net.http;
    requires org.apache.commons.configuration2;
    requires com.google.common;
    opens site.pokemons.edpproject.model to org.hibernate.orm.core;
    exports site.pokemons.edpproject.controller;
    exports site.pokemons.edpproject.event;
    opens site.pokemons.edpproject.controller to javafx.fxml;
    exports site.pokemons.edpproject.service;
    opens site.pokemons.edpproject.service to javafx.fxml;

    exports site.pokemons.edpproject.model.tmdbApiDto to com.fasterxml.jackson.databind;
    exports site.pokemons.edpproject.model.dbDto;
    exports site.pokemons.edpproject.model;
    exports site.pokemons.edpproject.model.db;
    opens site.pokemons.edpproject.model.db to javafx.fxml;
    exports site.pokemons.edpproject.service.webApi;
    opens site.pokemons.edpproject.service.webApi to javafx.fxml;
    exports site.pokemons.edpproject.controller.component;
    opens site.pokemons.edpproject.controller.component to javafx.fxml;
}