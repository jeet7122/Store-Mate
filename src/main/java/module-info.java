module org.storemate.pos {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.core;
    requires jdk.httpserver;
    requires jakarta.mail;
    exports org.storemate.pos;
}