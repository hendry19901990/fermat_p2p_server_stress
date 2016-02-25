/*
 * @#FermatP2pServerStressPluginRoot.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;


public class FermatP2pServerStressPluginRoot extends AbstractJavaSamplerClient implements Serializable{

	private static final long serialVersionUID = 1L;

	
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
	
	
	
}
