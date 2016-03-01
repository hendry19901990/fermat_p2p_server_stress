/*
 * @#ComponentConnectionRespondTyrusPacketProcessor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.processors;

import org.fermat.p2p.server.app.stress.structure.channel.WsCommunicationsTyrusCloudClientChannel;
import org.fermat.p2p.server.app.stress.structure.channel.vpn.WsCommunicationTyrusVPNClientManagerAgent;
import org.fermat.p2p.server.app.stress.structure.commons.components.PlatformComponentProfileCommunication;
import org.fermat.p2p.server.app.stress.structure.conf.ServerConf;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.AsymmetricCryptography;
import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatPacket;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;


public class ComponentConnectionRespondTyrusPacketProcessor extends FermatTyrusPacketProcessor{

	
	public ComponentConnectionRespondTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel) {
	  super(wsCommunicationsTyrusCloudClientChannel);
	}

	@Override
	public void processingPackage(FermatPacket receiveFermatPacket) {
		
	   /*
        * Get the message content and decrypt
        */
        String messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPrivateKey());

        try {

            /*
             * Construct the json object
             */
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject respond = parser.parse(messageContentJsonStringRepresentation).getAsJsonObject();

            //Get all values
            URI vpnServerUri = new URI(respond.get(JsonAttNamesConstants.VPN_URI).getAsString());
            String vpnServerIdentity = respond.get(JsonAttNamesConstants.VPN_SERVER_IDENTITY).getAsString();
            PlatformComponentProfile participantVpn = gson.fromJson(respond.get(JsonAttNamesConstants.APPLICANT_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);
            PlatformComponentProfile remotePlatformComponentProfile = gson.fromJson(respond.get(JsonAttNamesConstants.REMOTE_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);
            PlatformComponentProfile remoteNsPlatformComponentProfile = gson.fromJson(respond.get(JsonAttNamesConstants.REMOTE_PARTICIPANT_NS_VPN).getAsString(), PlatformComponentProfileCommunication.class);

            /*
             * get the Server ip and port
             */
            String ServerIp = getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getServerIp();
            Integer ServerPort = getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getServerPort();

            vpnServerUri = new URI(ServerConf.WS_PROTOCOL + ServerIp + ":" + ServerPort + vpnServerUri);

            System.out.println("ComponentConnectionRespondTyrusPacketProcessor - vpnServerUri to connect = "+vpnServerUri);

            /*
             * Get the  wsCommunicationVPNClientManagerAgent
             */
            WsCommunicationTyrusVPNClientManagerAgent wsCommunicationTyrusVPNClientManagerAgent = WsCommunicationTyrusVPNClientManagerAgent.getInstance();

            /*
             * Create a new VPN client
             */
            wsCommunicationTyrusVPNClientManagerAgent.createNewWsCommunicationVPNClient(vpnServerUri, vpnServerIdentity, participantVpn, remotePlatformComponentProfile, remoteNsPlatformComponentProfile, getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection());

        } catch (Exception e) {
           throw new RuntimeException(e);
        }
		
	}

	@Override
	public FermatPacketType getFermatPacketType() {
		return FermatPacketType.COMPONENT_CONNECTION_RESPOND;
	}

}
