plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.storemate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.storemate.pos")
    mainClass.set("org.storemate.pos.HelloApplication")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.graphics")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.6")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.7")

    implementation("com.mysql:mysql-connector-j:9.4.0")

// https://mvnrepository.com/artifact/com.sun.mail/jakarta.mail
    implementation("com.sun.mail:jakarta.mail:2.0.2")

    implementation("org.mindrot:jbcrypt:0.4")

    // Ikonli core for JavaFX
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
// Material Design Icons pack
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
// AntDesign Icons pack
    implementation("org.kordamp.ikonli:ikonli-antdesignicons-pack:12.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
