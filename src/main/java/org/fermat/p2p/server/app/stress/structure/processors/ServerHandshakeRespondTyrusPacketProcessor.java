/*
 * @#ServerHandshakeRespondTyrusPacketProcessor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.processors;

import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientChannel;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketCommunicationFactory;
import org.fermat.p2p.server.app.stress.structure.commons.contents.FermatPacketEncoder;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.enums.NetworkServiceType;
import org.fermat.p2p.server.app.stress.structure.enums.PlatformComponentType;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerHandshakeRespondTyrusPacketProcessor extends FermatTyrusPacketProcessor{
	
	   /**
     * Constructor
     *
     * @param wsCommunicationsTyrusCloudClientChannel
     */
    public ServerHandshakeRespondTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel) {
        super(wsCommunicationsTyrusCloudClientChannel);
    }
    
    @Override
    public void processingPackage(final FermatPacket receiveFermatPacket) {

        //System.out.println(" --------------------------------------------------------------------- ");
        //System.out.println("ServerHandshakeRespondTyrusPacketProcessor - processingPackage");

        /* -----------------------------------------------------------------------------------------
         * IMPORTANT: This Message Content of this packet come encrypted with the temporal identity public key
         * and contain the server identity whit the communications cloud client that
         * have to use to talk with the server.
         * -----------------------------------------------------------------------------------------
         */

        /*
         * Decrypt the message content
         */
        String jsonRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getTemporalIdentity().getPrivateKey());

        /*
         * Construct the json object
         */
        JsonParser parser = new JsonParser();
        JsonObject serverIdentity = parser.parse(jsonRepresentation).getAsJsonObject();

        /*
         * Get the server identity and set into the communication cloud client
         */
        getWsCommunicationsTyrusCloudClientChannel().setServerIdentity(serverIdentity.get(JsonAttNamesConstants.SERVER_IDENTITY).getAsString());


        //System.out.println("ServerHandshakeRespondTyrusPacketProcessor - ServerIdentity = "+ getWsCommunicationsTyrusCloudClientChannel().getServerIdentity());

        /*
         * Construct a Communications Cloud Client Profile for this component and send and fermat packet type FermatPacketType.COMPONENT_REGISTRATION_REQUEST
         */
        //PlatformComponentProfile communicationsCloudClientProfile = getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().constructPlatformComponentProfileFactory(getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPublicKey(), "WsCommunicationsCloudClientChannel",  "Web Socket Communications Cloud Client", NetworkServiceType.UNDEFINED, PlatformComponentType.COMMUNICATION_CLOUD_CLIENT, null);
        //getWsCommunicationsTyrusCloudClientChannel().setPlatformComponentProfile(communicationsCloudClientProfile); 
        PlatformComponentProfile communicationsCloudClientProfile = getWsCommunicationsTyrusCloudClientChannel().getPlatformComponentProfile();
        

        /* ------------------------------------
         * IMPORTANT: At this moment the server only
         * know the temporal identity of the client
         * the packet has construct with this identity
         * --------------------------------------
         */

        /*
         * Construc the jsonObject
         */
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JsonAttNamesConstants.NETWORK_SERVICE_TYPE, NetworkServiceType.UNDEFINED.toString());
        jsonObject.addProperty(JsonAttNamesConstants.PROFILE_TO_REGISTER, communicationsCloudClientProfile.toJson());

        /*
         * Construct a fermat packet whit the server identity
         */
        FermatPacket fermatPacketRespond = FermatPacketCommunicationFactory.constructFermatPacketEncryptedAndSinged(getWsCommunicationsTyrusCloudClientChannel().getServerIdentity(),                    //Destination
                                                                                                                    getWsCommunicationsTyrusCloudClientChannel().getTemporalIdentity().getPublicKey(),   //Sender
                                                                                                                    gson.toJson(jsonObject),                                      //Message Content
                                                                                                                    FermatPacketType.COMPONENT_REGISTRATION_REQUEST,                                //Packet type
                                                                                                                    getWsCommunicationsTyrusCloudClientChannel().getTemporalIdentity().getPrivateKey()); //Sender private key


        /*
         * Send the encode packet to the server
         */
        getWsCommunicationsTyrusCloudClientChannel().sendMessage(FermatPacketEncoder.encode(fermatPacketRespond));

    }

    /**
     * (no-javadoc)
     * @see FermatTyrusPacketProcessor#getFermatPacketType()
     */
    @Override
    public FermatPacketType getFermatPacketType() {
        return FermatPacketType.SERVER_HANDSHAKE_RESPOND;
    }

}
