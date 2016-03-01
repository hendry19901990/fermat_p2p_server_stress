/*
 * @#LocationProvider.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.enums;

import org.fermat.p2p.server.app.stress.structure.exceptions.InvalidParameterException;
import org.fermat.p2p.server.app.stress.structure.interfaces.FermatEnum;

public enum LocationProvider implements FermatEnum {

    GPS    ("GPS"),
    NETWORK("NET"),

    ;

    private final String code;

    LocationProvider(final String code){

        this.code = code;
    }

    public static LocationProvider getByCode(String code) throws InvalidParameterException {

        switch (code){

            case "GPS": return GPS;
            case "NET": return NETWORK;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "This Code Is Not Valid for the LocationProvider enum"
                );
        }
    }

    @Override
    public String getCode(){
        return this.code;
    }
    
}
