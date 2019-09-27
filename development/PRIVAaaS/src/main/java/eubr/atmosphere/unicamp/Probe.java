package eubr.atmosphere.unicamp;


import eu.atmosphere.tmaf.monitor.client.SynchronousClient;
import eu.atmosphere.tmaf.monitor.message.Data;
import eu.atmosphere.tmaf.monitor.message.Message;
import eu.atmosphere.tmaf.monitor.message.Observation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.ws.rs.InternalServerErrorException;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








public class Probe {

    /* ********************************************************************* */
    /* DEFINES                                                               */
    /* ********************************************************************* */
    public static final int PUBLISH_ERROR = -3;
    public static final int SOCKETL_ERROR = -2;
    public static final int TIMEOUT_ERROR = -1;
    
    public static final int SOCKET_TIME_OUT = 60000;

    private final static Logger log = LoggerFactory.getLogger(Probe.class);

    /* ********************************************************************* */
    /* ATTRIBUTES                                                            */
    /* ********************************************************************* */
    int __socketPort = 0;
    
    
    
    /* ********************************************************************* */
    /* PUBLIC METHODS                                                        */
    /* ********************************************************************* */
    /*
    * BRIEF: publish the message to monitor.
    * --------------------------------------------------------------------------
    *
    */
    public Map<String, String> publish_message(String endpoint,
                               long timeStamp,
                               int resourceId,
                               int probeId,
                               int dId1,
                               int dId2,
                               int dId3,
                               int dId4,
                               int dId5,
                               int dId6,
                               int k,
                               double riskP,
                               double riskJ, 
                               double riskM,
                               double loss,
                               int max_loop,
                               int socketPort) throws IOException,
                                                      ClassNotFoundException {

	/* Dictionay to store the result: */
        Map<String, String> dictionary = new HashMap<String, String>();

        long timeStampSeg = timeStamp/1000;
        int  count        = 0;
        this.__socketPort = socketPort;
        
        SynchronousClient client = new SynchronousClient(endpoint);

        boolean valret = client.authenticate(probeId, "pass".getBytes());

        /* Create a new message to send to monitor: */
        Message message = client.createMessage();
            
        message.setProbeId(probeId);
            
        /* Set the message ID. */      
        message.setResourceId(resourceId);
            
        Observation observation1 = new Observation(timeStampSeg, k    );
        Observation observation2 = new Observation(timeStampSeg, riskP);
        Observation observation3 = new Observation(timeStampSeg, riskJ);
        Observation observation4 = new Observation(timeStampSeg, riskM);
        Observation observation5 = new Observation(timeStampSeg, loss);
        
        Data data1 = new Data(Data.Type.MEASUREMENT, dId1, observation1);
        Data data2 = new Data(Data.Type.MEASUREMENT, dId2, observation2);
        Data data3 = new Data(Data.Type.MEASUREMENT, dId3, observation3);
        Data data4 = new Data(Data.Type.MEASUREMENT, dId4, observation4);
        Data data5 = new Data(Data.Type.MEASUREMENT, dId5, observation5);

        message.addData(data1);
        message.addData(data2);
        message.addData(data3);
        message.addData(data4);
        message.addData(data5);

	dictionary.put("ACTION", "MAX_LOOP");

        while (count != max_loop) {                                
            try {
                
		/* Send the result to monitor. */
                client.send(message);

                /* Case the client work listen the socket. */
                dictionary = this.__listen_socket();
		break;

            } catch (InternalServerErrorException e) {
		dictionary.put("ACTION", "PUBLISH_ERROR");
            } catch (Exception e) {
		dictionary.put("ACTION", "PUBLISH_ERROR");
            }
            count = count + 1;
        }
        return dictionary;
    }
    
    
    /* ********************************************************************* */
    /* PRIVATE METHODS                                                       */
    /* ********************************************************************* */
    /*
    * BRIEF: listen return from actuator.
    * --------------------------------------------------------------------------
    */
    private Map<String, String> __listen_socket() throws IOException,
	                                                ClassNotFoundException {
        
	/* Dictionay to store the result: */
        Map<String, String> dictionary = new HashMap<String, String>();

        try (ServerSocket server = new ServerSocket(this.__socketPort)) {
            try { 
                /* Non block: timeout after 120 seconds: */
                server.setSoTimeout(SOCKET_TIME_OUT);
                
                Socket client = server.accept();

                DataInputStream is = new DataInputStream(
                              new BufferedInputStream(client.getInputStream()));
                
                /* Parse: this time only ACTION to update or not the k value; */
	        dictionary.put("ACTION", is.readLine());
                client.close();
            }
            catch (SocketException e) {
                dictionary.put("ACTION", "TIMEOUT_ERROR");
            }
            catch(IOException e     ) {
                dictionary.put("ACTION", "SOCKETL_ERROR");
            }
            finally {
                server.close();
            }
        }
        catch(Exception e) {
            dictionary.put("ACTION", "SOCKETL_ERROR");
	}
        return dictionary;
    }
}

/* EOF */
