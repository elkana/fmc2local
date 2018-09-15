package com.ppu.fmc.local.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.ppu.fmc.util.LocalDateTimeAttributeConverter;

@Entity
@Table(name = "rna_host_ip_map")
public class HostIPMap {
	@Id 
	@Column(length = 40)
	private String hostidhex;

	@Column(length = 40)
	private String ipaddrhex;
	
	@Column(length = 15)
	private String ipaddr;

	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@CreatedDate
	private LocalDateTime createddate;

	public String getHostidhex() {
		return hostidhex;
	}

	public void setHostidhex(String hostidhex) {
		this.hostidhex = hostidhex;
	}

	public String getIpaddrhex() {
		return ipaddrhex;
	}

	public void setIpaddrhex(String ipaddrhex) {
		this.ipaddrhex = ipaddrhex;
	}
	
	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public LocalDateTime getCreateddate() {
		return createddate;
	}

	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}

	@Override
	public String toString() {
		return "HostIPMap [hostidhex=" + hostidhex + ", ipaddrhex=" + ipaddrhex + ", ipaddr=" + ipaddr
				+ ", createddate=" + createddate + "]";
	}

	
}
