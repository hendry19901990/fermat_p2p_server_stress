/*
 * @#CLoudClientConfigurator.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.conf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;

import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;

import com.google.gson.JsonObject;


public class CLoudClientConfigurator extends ClientEndpointConfig.Configurator{
	
	  /*
     * Create a new temporal identity
     */
    public final static ECCKeyPair tempIdentity = new ECCKeyPair();

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {

         /*
         * Get json representation
         */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JsonAttNamesConstants.NAME_IDENTITY, tempIdentity.getPublicKey());

        /*
         * Add the att to the header
         */
        headers.put(JsonAttNamesConstants.HEADER_ATT_NAME_TI, Arrays.asList(jsonObject.toString()));
        //headers.put("Origin", Arrays.asList("myOrigin"));
    }

    @Override
    public void afterResponse(HandshakeResponse hr) {
    }

}
