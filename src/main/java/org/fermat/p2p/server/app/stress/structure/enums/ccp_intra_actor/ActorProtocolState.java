/*
 * @#ActorProtocolState.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums.ccp_intra_actor;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatEnum;

public enum ActorProtocolState implements FermatEnum {

	DONE               ("DON"), // final state of request.
    PENDING_ACTION     ("PEA"), // pending local action, is given after raise a crypto payment request event.
    PROCESSING_RECEIVE ("PCR"), // when an action from the network service is needed receiving.
    PROCESSING_SEND    ("PCS"), // when an action from the network service is needed sending.
    WAITING_RESPONSE   ("WRE"),  // waiting response from the counterpart.
    SENT("SENT"),
    DELIVERY("DELIVERY");

    private String code;

    ActorProtocolState(String code){
        this.code = code;
    }

    public static ActorProtocolState getByCode(String code) throws InvalidParameterException {

        switch (code){

            case "DON": return DONE               ;
            case "PEA": return PENDING_ACTION     ;
            case "PCR": return PROCESSING_RECEIVE ;
            case "PCS": return PROCESSING_SEND    ;
            case "WRE": return WAITING_RESPONSE   ;
            case "SENT": return SENT;
            case "DELIVERY": return DELIVERY;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "This code is not valid for the RequestProtocolState enum"
                );
        }
    }

    @Override
    public String getCode(){
        return this.code;
    }
}
