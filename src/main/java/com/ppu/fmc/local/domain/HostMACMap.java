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
@Table(name = "rna_host_mac_map")
public class HostMACMap {
	@Id 
	@Column(length = 40)
	private String hostidhex;

	@Column(length = 36)
	private String macaddr;
	
	@Column(length = 127)
	private String macvendor;

	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@CreatedDate
	private LocalDateTime createddate;

	public String getHostidhex() {
		return hostidhex;
	}

	public void setHostidhex(String hostidhex) {
		this.hostidhex = hostidhex;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getMacvendor() {
		return macvendor;
	}

	public void setMacvendor(String macvendor) {
		this.macvendor = macvendor;
	}

	public LocalDateTime getCreateddate() {
		return createddate;
	}

	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}

	@Override
	public String toString() {
		return "HostMACMap [hostidhex=" + hostidhex + ", macaddr=" + macaddr + ", macvendor=" + macvendor
				+ ", createddate=" + createddate + "]";
	}


}
