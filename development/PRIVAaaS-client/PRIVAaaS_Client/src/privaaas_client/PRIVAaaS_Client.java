/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privaaas_client;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import javax.json.*;

import br.unicamp.ft.arx.service.*;


/**
 *
 * @author marcio
 */
public class PRIVAaaS_Client {
    
    /**************************************************************************
     * ATTRTIBUTES
     *************************************************************************/


   /**************************************************************************
     * METHODS
     *************************************************************************/
    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) throws Exception, IOException {
                                                         
        String datadbFile = "";
        String policyFile = "";

        /* Excute the arguments parse (six arguments -name value). */
        if (args.length == 4) {

            for (int i = 0; i < args.length; i++) {

                if (args[i].equals("-p")) {
                    policyFile = args[i+1];
                }
                if (args[i].equals("-d")) {
                    datadbFile = args[i+1];
                }
            }

            /**/
            JsonObject policyJsonObj = null;
            
            try (JsonReader jsonReader =
                               Json.createReader(new FileReader(policyFile))) {
                
                policyJsonObj = jsonReader.readObject();
                jsonReader.close();
                
            } catch (JsonException e) {
                System.err.println("Error: " + e + "\nTrace:");
                System.exit(-1);
            }           
            
            /*
            *********************
            The inputstream test:
            *********************
            */
            InputStream csvStream = new FileInputStream(new File(datadbFile));
            
            /*
            *********************
            Anonymize:
            *********************
            */
            Anonymizer obj = new Anonymizer();

            /* Prepare and execute the anonimization. */
            obj.prepare_source(csvStream, policyJsonObj);
            obj.run();
    
            /* Get anonymized result. */
            JsonArray jsonArrayResult = obj.get_json_anonymized();
            
            /*
            *********************
            Show result:
            *********************
            */
            for(int i = 0; i < jsonArrayResult.size(); i++) {
                JsonObject jObj = jsonArrayResult.getJsonObject(i);
                
                for (String key: jObj.keySet()) {
                    System.out.print("\""+key+"\":\""+jObj.getString(key)+"\"");
                }
                System.out.println("");
            }   
        }
    }
}