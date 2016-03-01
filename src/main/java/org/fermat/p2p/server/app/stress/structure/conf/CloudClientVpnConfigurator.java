/*
 * @#CloudClientVpnConfigurator.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.conf;


import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;
import org.fermat.p2p.server.app.stress.structure.crypto.asymmetric.ECCKeyPair;
import org.fermat.p2p.server.app.stress.structure.enums.JsonAttNamesConstants;
import org.fermat.p2p.server.app.stress.structure.interfaces.PlatformComponentProfile;


public class CloudClientVpnConfigurator extends ClientEndpointConfig.Configurator {

    /**
     * Represent the vpnClientIdentity
     */
    private ECCKeyPair vpnClientIdentity;

    /**
     * Represent the remoteParticipantNetworkService
     */
    private PlatformComponentProfile remoteParticipantNetworkService;

    /**
     * Represent the registerParticipant
     */
    private PlatformComponentProfile registerParticipant;

    /**
     * Represent the remotePlatformComponentProfile
     */
    private  PlatformComponentProfile remotePlatformComponentProfile;

    /**
     * Constructor with parameters
     *
     * @param vpnClientIdentity
     * @param remoteParticipantNetworkService
     * @param registerParticipant
     * @param remotePlatformComponentProfile
     */
    public CloudClientVpnConfigurator(ECCKeyPair vpnClientIdentity, PlatformComponentProfile remoteParticipantNetworkService, PlatformComponentProfile registerParticipant, PlatformComponentProfile remotePlatformComponentProfile) {
        this.vpnClientIdentity = vpnClientIdentity;
        this.remoteParticipantNetworkService = remoteParticipantNetworkService;
        this.registerParticipant = registerParticipant;
        this.remotePlatformComponentProfile = remotePlatformComponentProfile;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {

        /*
         * Get json representation
         */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JsonAttNamesConstants.NETWORK_SERVICE_TYPE, remoteParticipantNetworkService.getNetworkServiceType().toString());
        jsonObject.addProperty(JsonAttNamesConstants.CLIENT_IDENTITY_VPN, vpnClientIdentity.getPublicKey());
        jsonObject.addProperty(JsonAttNamesConstants.APPLICANT_PARTICIPANT_VPN, registerParticipant.toJson());
        jsonObject.addProperty(JsonAttNamesConstants.REMOTE_PARTICIPANT_VPN, remotePlatformComponentProfile.getIdentityPublicKey());

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
