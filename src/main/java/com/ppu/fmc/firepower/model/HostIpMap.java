package com.ppu.fmc.firepower.model;

public class HostIpMap {
	
	private String hostIdInHexa;
	private String ipAddressInHexa;
	
	public String getHostIdInHexa() {
		return hostIdInHexa;
	}
	public void setHostIdInHexa(String hostIdInHexa) {
		this.hostIdInHexa = hostIdInHexa;
	}
	public String getIpAddressInHexa() {
		return ipAddressInHexa;
	}
	public void setIpAddressInHexa(String ipAddressInHexa) {
		this.ipAddressInHexa = ipAddressInHexa;
	}
	@Override
	public String toString() {
		return "HostIpMap [hostIdInHexa=" + hostIdInHexa + ", ipAddressInHexa=" + ipAddressInHexa + "]";
	}
	
	
}
