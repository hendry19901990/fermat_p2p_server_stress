/*
 * @#InvalidParameterException.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.exceptions;



public class InvalidParameterException extends FermatException{

	    private static final long serialVersionUID = 1L;
		
	    public static final String DEFAULT_MESSAGE = "INVALID PARAMETER";

	    public InvalidParameterException(final String message, final Exception cause, final String context, final String possibleReason) {
	        super(message, cause, context, possibleReason);
	    }

	    public InvalidParameterException(final Exception cause, final String context, final String possibleReason) {
	        super(DEFAULT_MESSAGE, cause, context, possibleReason);
	    }

	    public InvalidParameterException(final String message, final Exception cause) {
	        this(message, cause, "", "");
	    }

	    public InvalidParameterException(final String context, final String possibleReason) {
	        this(DEFAULT_MESSAGE, null, context, possibleReason);
	    }

	    public InvalidParameterException(final String message) {
	        this(message, null, null, null);
	    }

	    public InvalidParameterException(final Exception exception) {
	        this(exception.getMessage());
	        setStackTrace(exception.getStackTrace());
	    }

	    public InvalidParameterException() {
	        this(DEFAULT_MESSAGE);
	    }
	
	
}
