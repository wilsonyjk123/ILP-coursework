package uk.ac.ed.inf;

import java.sql.SQLException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException {
//        String day = args[0];
//        String month = args[1];
//        String year = args[2];
//        String webPort = args[3];
//        String databasePort = args[4];
        Database database = new Database("1527","11","4", "2022");
        database.readOrdersFromDatabase();
    }
}
