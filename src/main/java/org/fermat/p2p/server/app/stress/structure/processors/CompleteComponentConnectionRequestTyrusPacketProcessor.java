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

public class CompleteComponentConnectionRequestTyrusPacketProcessor extends FermatTyrusPacketProcessor{
	
   /*
    * Constructor
    *
    * @param wsCommunicationsTyrusCloudClientChannel
    */
    public CompleteComponentConnectionRequestTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel) {
        super(wsCommunicationsTyrusCloudClientChannel);
    }
    
    @Override
    public void processingPackage(FermatPacket receiveFermatPacket){

        System.out.println("CompleteComponentConnectionRequestTyrusPacketProcessor - processingPackage = ");

        /*
         * Get the filters from the message content and decrypt
         */
        String messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPrivateKey());

       // System.out.println("CompleteComponentConnectionRequestTyrusPacketProcessor - messageContentJsonStringRepresentation = "+messageContentJsonStringRepresentation);

        /*
         * Construct the json object
         */
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject respond = parser.parse(messageContentJsonStringRepresentation).getAsJsonObject();

        NetworkServiceType networkServiceType       = gson.fromJson(respond.get(JsonAttNamesConstants.NETWORK_SERVICE_TYPE), NetworkServiceType.class);
        PlatformComponentProfile applicantComponent = gson.fromJson(respond.get(JsonAttNamesConstants.APPLICANT_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);
        PlatformComponentProfile remoteComponent    = gson.fromJson(respond.get(JsonAttNamesConstants.REMOTE_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);

        System.out.println("CompleteComponentConnectionRequestTyrusPacketProcessor - networkServiceType = "+networkServiceType);
        System.out.println("CompleteComponentConnectionRequestTyrusPacketProcessor - applicantComponent = "+applicantComponent.getAlias() + "["+applicantComponent.getIdentityPublicKey() +"]");
        System.out.println("CompleteComponentConnectionRequestTyrusPacketProcessor - remoteComponent    = "+remoteComponent.getAlias()    + "["+remoteComponent.getIdentityPublicKey()    +"]");


       /*
        * Storage the Request Success connection 
        */
        getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().storagelistOfRequestConnectSuccess(networkServiceType, remoteComponent);

    }

    /**
     * (no-javadoc)
     * @see FermatTyrusPacketProcessor#getFermatPacketType()
     */
    @Override
    public FermatPacketType getFermatPacketType() {
        return FermatPacketType.COMPLETE_COMPONENT_CONNECTION_REQUEST;
    }

}
