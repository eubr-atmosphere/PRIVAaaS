/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privaaas_client;

import java.io.IOException;
import java.sql.*;

import br.unicamp.ft.arx.service.*;


/**
 *
 * @author marcio
 */
public class PRIVAaaS_Client {
    
    /**************************************************************************
     * ATTRTIBUTES
     *************************************************************************/

    /* JDBC driver name to be used and database URL (address and db name) */
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL      = "jdbc:mysql://localhost/PRIVaaS";

    /*  Database credentials: user and password. */
    static final String USER = "root";
    static final String PASS = "password";


   /**************************************************************************
     * METHODS
     *************************************************************************/
    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) throws Exception, IOException, 
                                                             SQLException {
        
        ResultSetMetaData receivedMd = null;
        
        ResultSet receivedRs = null;
        ResultSet returnedRs = null;
        
        Connection conn = null;
        Statement  stmt = null;
        
        String policyFile = args[1];

        
        /**/
        Anonymizer anonymizer = new Anonymizer();
        
        try {
            /* Iniatializing -- register JDBC driver to be used in the code. */
            Class.forName("com.mysql.jdbc.Driver");

            /* Open a connection with database. Use the URL and DB password. */
            conn = DriverManager.getConnection(DB_URL, USER,PASS);
            conn.setAutoCommit(false);

            /* Creating statement: */
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                                        ResultSet.CONCUR_UPDATABLE);

            /* Execute the query, return the resultset interface and get the ta
               ble metadata. */
            receivedRs = stmt.executeQuery("SELECT * FROM "+args[0]);
            receivedMd = receivedRs.getMetaData();
                
            /* Get column number from resultset metadata (contain info about the
               table. */
            int columnsNumber = receivedMd.getColumnCount();
                  
            try {
                /* ------------- */
                /* ANONYMIZER !  */
                /* ------------- */
                anonymizer.prepare_source(receivedRs);
                returnedRs = anonymizer.run(policyFile, 300, "");
            }
            catch(Exception e) {
                e.printStackTrace();        
            }
                
            /* Print the returned resultSet: */
            while (returnedRs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print("| ");
                    String columnValue = returnedRs.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }
            
            stmt.close();
            conn.close();
            
        } catch(SQLException e) {
            e.printStackTrace();

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            /* Finally block used to close resources. */
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                   conn.close();
            }
        }
    }
}
