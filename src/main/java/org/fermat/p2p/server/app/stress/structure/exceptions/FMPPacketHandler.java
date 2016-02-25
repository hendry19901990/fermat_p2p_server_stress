package org.fermat.p2p.server.app.stress.structure.exceptions;

public interface FMPPacketHandler {
	
	public void handleConnectionRequest(final FMPPacket packet) throws FMPException; 
	public void handleConnectionAccept(final FMPPacket packet) throws FMPException;
	public void handleConnectionAcceptForward(final FMPPacket packet) throws FMPException;
	public void handleConnectionDeny(final FMPPacket packet) throws FMPException;
	public void handleConnectionRegister(final FMPPacket packet) throws FMPException;
	public void handleConnectionDeregister(final FMPPacket packet) throws FMPException;
	public void handleConnectionEnd(final FMPPacket packet) throws FMPException;
	public void handleDataTransmit(final FMPPacket packet) throws FMPException;

}
