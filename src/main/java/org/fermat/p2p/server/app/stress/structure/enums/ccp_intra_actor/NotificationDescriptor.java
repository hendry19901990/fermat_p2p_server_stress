/*
 * @#NotificationDescriptor.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;



public enum NotificationDescriptor {
	
	 ASKFORACCEPTANCE("ASK"),
	    INTRA_USER_NOT_FOUND("IUNF"),
	    ACCEPTED("ACP"),
	    CANCEL("CAN"),
	    DISCONNECTED("DIS"),
	    RECEIVED("REC"),
	    DENIED("DEN");

	    private String code;

	    NotificationDescriptor(String code){

	        this.code=code;

	    }

	    public String getCode(){

	        return this.code;

	    }

	    public static NotificationDescriptor getByCode(String code)throws InvalidParameterException{

	        switch (code){

	            case "ASK":
	                return NotificationDescriptor.ASKFORACCEPTANCE;
	            case "CAN":
	                return NotificationDescriptor.CANCEL;
	            case "ACP":
	                return NotificationDescriptor.ACCEPTED;
	            case "DIS":
	                return NotificationDescriptor.DISCONNECTED;
	            case "REC":
	                return NotificationDescriptor.RECEIVED;
	            case "DEN":
	                return NotificationDescriptor.DENIED;
	            case "IUNF":
	                return NotificationDescriptor.INTRA_USER_NOT_FOUND;
	            default:
	                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the NotificationDescriptor enum");


	        }

	    }

}
