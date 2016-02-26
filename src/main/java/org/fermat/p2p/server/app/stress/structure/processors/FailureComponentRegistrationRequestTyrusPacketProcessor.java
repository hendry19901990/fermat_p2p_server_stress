/*
 * @#FailureComponentRegistrationRequestTyrusPacketProcessor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.processors;

import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientChannel;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FailureComponentRegistrationRequestTyrusPacketProcessor extends FermatTyrusPacketProcessor{
	
    /**
     * Represent the jsonParser
     */
    private JsonParser jsonParser;

    /**
     * Represent the gson
     */
    private Gson gson;

    /**
     * Constructor
     */
    public FailureComponentRegistrationRequestTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel){
        super(wsCommunicationsTyrusCloudClientChannel);
        jsonParser = new JsonParser();
        gson = new Gson();
    }
    
    @Override
    public void processingPackage(FermatPacket receiveFermatPacket){
    	
    	/*
         * Get the filters from the message content and decrypt
         */
        String messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPrivateKey());

        /*
         * Construct the json object
         */
        JsonObject packetContent = jsonParser.parse(messageContentJsonStringRepresentation).getAsJsonObject();
        NetworkServiceType networkServiceApplicant  = gson.fromJson(packetContent.get(JsonAttNamesConstants.NETWORK_SERVICE_TYPE).getAsString(), NetworkServiceType.class);
        PlatformComponentProfile platformComponentProfile = new PlatformComponentProfileCommunication().fromJson(packetContent.get(JsonAttNamesConstants.PROFILE_TO_REGISTER).getAsString());
        String errorMsj = packetContent.get(JsonAttNamesConstants.FAILURE_VPN_MSJ).getAsString();

        System.out.println("FailureComponentRegistrationRequestTyrusPacketProcessor - errorMsj "+errorMsj);
    	
    }
    
    @Override
    public FermatPacketType getFermatPacketType() {
        return FermatPacketType.FAILURE_COMPONENT_REGISTRATION_REQUEST;
    }

}
