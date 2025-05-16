module site.pokemons.edpproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires jakarta.persistence;
    requires static lombok;

    opens site.pokemons.edpproject to javafx.fxml;
    exports site.pokemons.edpproject;

    requires org.hibernate.orm.core;
    requires java.sql;
    requires jbcrypt;
    opens site.pokemons.edpproject.model to org.hibernate.orm.core;
    exports site.pokemons.edpproject.controller;
    opens site.pokemons.edpproject.controller to javafx.fxml;
    exports site.pokemons.edpproject.service;
    opens site.pokemons.edpproject.service to javafx.fxml;
}