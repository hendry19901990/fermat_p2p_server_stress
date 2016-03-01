/*
 * @#CantEstablishConnectionException.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;


public class CantEstablishConnectionException extends CommunicationException {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_MESSAGE = "CAN'T ESTABLISH THE CONNECTION";

	public CantEstablishConnectionException(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}
	
	
}
