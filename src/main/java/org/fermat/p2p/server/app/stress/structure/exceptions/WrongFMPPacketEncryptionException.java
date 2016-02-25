/*
 * @#FMPPacket.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;

public class WrongFMPPacketEncryptionException extends FMPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2845188917623252517L;

	public static final String DEFAULT_MESSAGE = "WRONG FMP PACKET ENCRYPTION";

	public WrongFMPPacketEncryptionException(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}
	
}
