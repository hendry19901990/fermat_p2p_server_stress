/*
 * @#FermatPacket.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;

import java.util.UUID;

import org.fermat.p2p.server.app.stress.structure.enums.FermatPacketType;

public interface FermatPacket {
	
	/**
     * Represent the PACKET_MAX_BYTE_SIZE = 1024
     */
	public static final int PACKET_MAX_BYTE_SIZE = 1024;

	/**
	 * Get the id of the package
	 */
	public UUID getId();

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
	public FermatPacketType getFermatPacketType();

    /**
     * Get the message content of the packet
     *
     * @return String
     */
	public String getMessageContent();

    /**
     * Get the signature of the packet
     *
     * @return String
     */
	public String getSignature();

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
     * @return FermatPacket
     */
    public FermatPacket fromJson(String json);

}
