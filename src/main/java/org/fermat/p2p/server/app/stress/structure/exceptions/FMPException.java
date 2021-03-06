/*
 * @#JsonAttNamesConstants.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;

public abstract class FMPException extends FermatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8596468775669289882L;

	private static final String DEFAULT_MESSAGE = "THE FERMAT MESSAGING PROTOCOL HAS THROWN AN EXCEPTION";

	public FMPException(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}

}
