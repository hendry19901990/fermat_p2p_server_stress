/*
 * @#LocationManager.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package org.fermat.p2p.server.app.stress.structure.interfaces;

import org.fermat.p2p.server.app.stress.structure.exceptions.FermatException;

public interface LocationManager {
	
	Location getLocation() throws FermatException;

}
