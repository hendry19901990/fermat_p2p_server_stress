/*
 * @#MalformedFMPPacketException.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;

public class MalformedFMPPacketException extends FMPException{
	
	/**
	 * autogenerated serialVersionUID
	 */
	private static final long serialVersionUID = -1102140907777389730L;

	public static final String DEFAULT_MESSAGE = "MALFORMED FMP PACKET";

	public MalformedFMPPacketException(final String message, final Exception cause, final String context, final String possibleReason){
		super(message, cause, context, possibleReason);
	}

}
