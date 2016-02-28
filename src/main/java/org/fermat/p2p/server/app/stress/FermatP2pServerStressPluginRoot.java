/*
 * @#FermatP2pServerStressPluginRoot.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsCloudClientSupervisorConnectionAgent;
import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientConnection;
import org.fermat.p2p.server.app.stress.structure.conf.ServerConf;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;


public class FermatP2pServerStressPluginRoot extends AbstractJavaSamplerClient implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		
		URI uri;
		WsCommunicationsTyrusCloudClientConnection wsCommunicationsTyrusCloudClientConnection = null;
		ECCKeyPair par = new ECCKeyPair();
		/*
	     * Represent the executor
	     */
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		
		/*
		 *  Start the plugin JMeter
		 */
		 SampleResult rv = new SampleResult();
	     rv.sampleStart();
	     
	 	 try {
			 
	 		/*
	 		 * Construct the URI to connect to Cloud Server
	 		 */
			uri = new URI(ServerConf.WS_PROTOCOL + ServerConf.SERVER_IP_DEVELOPER_LOCAL + ":" + ServerConf.DEFAULT_PORT + ServerConf.WEB_SOCKET_CONTEXT_PATH);
			 
			/*
             * Try to connect whit the cloud server
             */
			wsCommunicationsTyrusCloudClientConnection = new WsCommunicationsTyrusCloudClientConnection(uri, par);
			wsCommunicationsTyrusCloudClientConnection.initializeAndConnect();
			
	         /*
	          * Scheduled the reconnection agent
	          */
	        scheduledExecutorService.scheduleAtFixedRate(new WsCommunicationsCloudClientSupervisorConnectionAgent(wsCommunicationsTyrusCloudClientConnection), 10, 20, TimeUnit.SECONDS);
			 
			
			/*
			 * wait 3 minutes to complete All the work of the Network Services
			 */
			 TimeUnit.MINUTES.sleep(3);
			 
			 /*
			  * Close Connection after complete All the work of the Network Services
			  */
			 wsCommunicationsTyrusCloudClientConnection.CloseConnection();
			 
			 /*
			  * get the total Profile To Register
			  */
			 int totalProfileToRegister = wsCommunicationsTyrusCloudClientConnection.getTotalProfileToRegister();
			 
			 /*
			  * get the total Profile To Registered Success
			  */
			 int totalProfileRegisteredSuccess = wsCommunicationsTyrusCloudClientConnection.getTotalProfileRegisteredSuccess();
			 
			 /*
			  * set true and the Response Code 200, the work has been OK
			  */
			 rv.setSuccessful(true);
			 rv.setResponseCode("200");
			 
			 /*
			  * Calculate % of Profile Registered Success
			  */
			 //float porcentaje = (totalProfileRegisteredSuccess * 100) / totalProfileToRegister;
			 
			 String resultSamplerData = " TotalProfileToRegister " + totalProfileToRegister + 
					 " totalProfileRegisteredSuccess " + totalProfileRegisteredSuccess ;
			 
			 
			 rv.setSamplerData(resultSamplerData);
			 rv.setResponseMessage(resultSamplerData);
			 
			 /*
			  * stop the scheduledExecutorService
			  */
			 scheduledExecutorService.shutdownNow();
			 
		  } catch (Exception e) {
			  
			  /*
			   * set true and the Response Code 500, the work has been BAD
			   */
			  rv.setSuccessful(false);
			  rv.setResponseCode("500");
			  rv.setResponseMessage("Exception: " + e);
			
			  try {
				  if(wsCommunicationsTyrusCloudClientConnection!=null)
					  wsCommunicationsTyrusCloudClientConnection.CloseConnection();
			  } catch (IOException e1) {
				//e1.printStackTrace();
			  }
			
			//e.printStackTrace();
		  }
	 	 
	 	rv.sampleEnd();
		  	     
	        
		return rv;
	}
	
	
	@Override
	public void setupTest(JavaSamplerContext context) {
		
	}
    
	
	@Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("URI", ServerConf.WS_PROTOCOL + ServerConf.SERVER_IP_DEVELOPER_LOCAL + ":" + ServerConf.DEFAULT_PORT + ServerConf.WEB_SOCKET_CONTEXT_PATH);
        return params;
	}
	
 
   public static void main(String args[]) throws Exception{
		
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		
		 /*
	     * Represent the executor
	     */
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		
		URI uri = new URI(ServerConf.WS_PROTOCOL + ServerConf.SERVER_IP_DEVELOPER_LOCAL + ":" + ServerConf.DEFAULT_PORT + ServerConf.WEB_SOCKET_CONTEXT_PATH);
		 ECCKeyPair par = new ECCKeyPair();
		 
		WsCommunicationsTyrusCloudClientConnection wsCommunicationsTyrusCloudClientConnection = new WsCommunicationsTyrusCloudClientConnection(uri, par);
		 wsCommunicationsTyrusCloudClientConnection.initializeAndConnect();
		 

         /*
          * Scheduled the reconnection agent
          */
         scheduledExecutorService.scheduleAtFixedRate(new WsCommunicationsCloudClientSupervisorConnectionAgent(wsCommunicationsTyrusCloudClientConnection), 10, 20, TimeUnit.SECONDS);
		 
	    try {

		   TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
		   e.printStackTrace();
		}
	    
		 /*
		  * stop the scheduledExecutorService
		  */
		 scheduledExecutorService.shutdownNow();
		 
		 wsCommunicationsTyrusCloudClientConnection.CloseConnection();
		 
		 time_end = System.currentTimeMillis();
		 
		 int totalToRegister = wsCommunicationsTyrusCloudClientConnection.getTotalProfileToRegister();
		 
		 int totalRegisteredSuccess = wsCommunicationsTyrusCloudClientConnection.getTotalProfileRegisteredSuccess();
		 
		 System.out.println("*********************** RESULTS  *************************");
		 System.out.println("totalToRegister "+totalToRegister+" totalRegisteredSuccess "+totalRegisteredSuccess);
		 System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
	}
 
	
	
}
