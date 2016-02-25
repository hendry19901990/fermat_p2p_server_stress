/*
 * @#FMPPacket.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;

import org.fermat.p2p.server.app.stress.structure.enums.NetworkServices;



public interface FMPPacket {
	
	/**
     * Represent the PACKET_MAX_BYTE_SIZE = 1024
     */
	public static final int PACKET_MAX_BYTE_SIZE = 1024;

    /**
     * Get the sender of the packet
     *
     * @return String
     */
	public String getSender();

    /**
     * Get the destination of the packet
     *
     * @return String
     */
	public String getDestination();

    /**
     * Get the type of the packet
     *
     * @return FermatPacketType
     */
	public FMPPacketType getType();

    /**
     * Get the message of the packet
     *
     * @return String
     */
	public String getMessage();

    /**
     * Get the signature of the packet
     *
     * @return String
     */
	public String getSignature();

    /**
     * Get the network service type of the packet
     *
     * @return NetworkServices
     */
	public NetworkServices getNetworkServices();

    /**
     * Set the network service type of the packet
     *
     * @param networkServicesType
     */
	public void setNetworkServices(NetworkServices networkServicesType);

    /**
     * Convert this object to json string
     *
     * @return String json
     */
    public String toJson();

    /**
     * Convert to FermatPacketCommunication from json
     *
     * @param json string object
     * @return FermatPacketCommunication
     */
    public FMPPacket fromJson(String json);


    /**
     * Package type definitions
     */
	enum FMPPacketType {
		CONNECTION_REQUEST,
		CONNECTION_ACCEPT,
		CONNECTION_ACCEPT_FORWARD,
		CONNECTION_DENY,
		CONNECTION_REGISTER,
		CONNECTION_DEREGISTER,
		CONNECTION_END,
		DATA_TRANSMIT,
		REGISTER_NETWORK_SERVICES_LIST_REQUEST
	}

}
