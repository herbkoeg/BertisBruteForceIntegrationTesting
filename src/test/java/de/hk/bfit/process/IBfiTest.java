package de.hk.bfit.process;

import de.hk.bfit.db.PostgresDBConnector;

import java.sql.Connection;
import java.sql.SQLException;

public interface IBfiTest {
    String BASE_PATH = "src/test/resources/";
    String BASE_PATH_GENERATED = BASE_PATH + "generated/";
    String BASE_PATH_TESTCASES = BASE_PATH + "testcases/";
    String JDBC_POSTGRESQL_MY_DB = "jdbc:postgresql://localhost:5432/bertisDB";
    String DB_USER = "berti";
    String DB_PASSWORD = "eXtreme";

}
