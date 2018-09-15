package com.ppu.fmc.firepower.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="network_discovery_event")
public class NetworkDiscoveryEvent {
	@Id
	private Long event_id;
	private Long event_time_sec;
	private Long event_time_usec;
	@Column(length = 64)
	private String event_type;
	@Column(length = 255)
	private String ip_address;
	private byte[] ipaddr;
	private byte[] mac_address;
	@Column(length = 127)
	private String mac_vendor;
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public Long getEvent_time_sec() {
		return event_time_sec;
	}
	public void setEvent_time_sec(Long event_time_sec) {
		this.event_time_sec = event_time_sec;
	}
	public Long getEvent_time_usec() {
		return event_time_usec;
	}
	public void setEvent_time_usec(Long event_time_usec) {
		this.event_time_usec = event_time_usec;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public byte[] getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(byte[] ipaddr) {
		this.ipaddr = ipaddr;
	}
	public byte[] getMac_address() {
		return mac_address;
	}
	public void setMac_address(byte[] mac_address) {
		this.mac_address = mac_address;
	}
	public String getMac_vendor() {
		return mac_vendor;
	}
	public void setMac_vendor(String mac_vendor) {
		this.mac_vendor = mac_vendor;
	}
	@Override
	public String toString() {
		return "NetworkDiscoveryEvent [event_id=" + event_id + ", event_time_sec=" + event_time_sec
				+ ", event_time_usec=" + event_time_usec + ", event_type=" + event_type + ", ip_address=" + ip_address
				+ ", ipaddr=" + Arrays.toString(ipaddr) + ", mac_address=" + Arrays.toString(mac_address)
				+ ", mac_vendor=" + mac_vendor + "]";
	}

}
