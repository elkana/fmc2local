package com.ppu.fmc.firepower.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "ip", "label"})
public class IpLocation {

	@JsonProperty
	private String ip;
	@JsonProperty
	private String label;

	public IpLocation() {} 

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "IpLocation [ip=" + ip + ", label=" + label + "]";
	}

}
