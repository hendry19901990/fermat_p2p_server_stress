/*
 * @#FermatP2pServerStressPluginRoot.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientConnection;
import org.fermat.p2p.server.app.stress.structure.conf.ServerConf;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;


public class FermatP2pServerStressPluginRoot extends AbstractJavaSamplerClient implements Serializable{

	private static final long serialVersionUID = 1L;

	static URI uri;
	static WsCommunicationsTyrusCloudClientConnection wsCommunicationsTyrusCloudClientConnection;
	
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void setupTest(JavaSamplerContext context) {
		
	}
    
	
	@Override
    public Arguments getDefaultParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String args[]) throws Exception{
		
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		
		 uri = new URI(ServerConf.WS_PROTOCOL + "52.35.64.221" + ":" + ServerConf.DEFAULT_PORT + ServerConf.WEB_SOCKET_CONTEXT_PATH);
		 ECCKeyPair par = new ECCKeyPair();
		 
		 wsCommunicationsTyrusCloudClientConnection = new WsCommunicationsTyrusCloudClientConnection(uri, par);
		 wsCommunicationsTyrusCloudClientConnection.initializeAndConnect();
		 
		 
	    try {

		   /*
		    * Wait 1 second to avoid that the Network Services are Initialized completely
		    */
		           TimeUnit.SECONDS.sleep(35);
		} catch (InterruptedException e) {
		   e.printStackTrace();
		}
		 wsCommunicationsTyrusCloudClientConnection.CloseConnection();
		 
		 time_end = System.currentTimeMillis();
		 
		 int totalToRegister = wsCommunicationsTyrusCloudClientConnection.getTotalProfileToRegister();
		 
		 int totalRegisteredSuccess = wsCommunicationsTyrusCloudClientConnection.getTotalProfileRegisteredSuccess();
		 
		 System.out.println("*********************** RESULTS  *************************");
		 System.out.println("totalToRegister "+totalToRegister+" totalRegisteredSuccess "+totalRegisteredSuccess);
		 
		 System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
	}
	
	
	
}
