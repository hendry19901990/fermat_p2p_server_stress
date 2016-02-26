/*
 * @#CommunicationException.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;

public class CommunicationException extends FermatException{
	
	private static final long serialVersionUID = 6617598263430521619L;

	public static final String DEFAULT_MESSAGE = "THE COMMUNICATION LAYER HAS TRIGGERED AN EXCEPTION";

	public CommunicationException(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}

	public CommunicationException(final String message, final Exception cause) {
		this(message, cause, "", "");
	}

	public CommunicationException(final String message) {
		this(message, null);
	}

	public CommunicationException(final Exception exception) {
		this(exception.getMessage());
		setStackTrace(exception.getStackTrace());
	}

	public CommunicationException() {
		this(DEFAULT_MESSAGE);
	}

}
