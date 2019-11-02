package eubr.atmosphere.tma.actuator;

import java.util.Properties;
import java.net.*;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/privaaas")
public class Privaaas implements Actuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Privaaas.class);


    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    private int     __port = 6000;
    private String  __addr = "localhost";


    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
    BRIEF: act endpoint.
    ----------------------------------------------------------------------------
    @PARAM actuatorPayload == message payload from planning.
    */
    @RequestMapping("/act")
    public void act(@RequestBody ActuatorPayload actuatorPayload) {

        LOGGER.info("New! message: " + actuatorPayload.toString());
        String action = actuatorPayload.getAction();

        switch (action) {
            case "update":
                this.__send_action_probe("update");
                break;

            case "none":
                this.__send_action_probe("none");
                break;

            default:
                LOGGER.info("Action not suported!!");
                break;
        }
    }


    /* ********************************************************************* */
    /* PROTECTED METHODS                                                     */
    /* ********************************************************************* */
    /*
    BRIEF: send message to probe.
    ----------------------------------------------------------------------------
    @PARAM message == message to send.
    */
    private void __send_action_probe(String message) {

        LOGGER.info("Send message: " + message);

        try (Socket socket = new Socket(this.__addr, this.__port)) {
            DataOutputStream dOut=new DataOutputStream(socket.getOutputStream());

            dOut.writeUTF(message);
            dOut.flush();
            dOut.close();

            LOGGER.info("Ok, messagen sent!");

        } catch (UnknownHostException ex) {
            LOGGER.info("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
            LOGGER.info("I/O error: " + ex.getMessage());
        }
    }
}

/* EOF */
