package com.ppu.fmc.local.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.ppu.fmc.util.LocalDateTimeAttributeConverter;

@Entity
@Table(name = "macaddr")
public class MacAddr {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length = 36)
	private String macaddr;
	
	@Column(length = 40)
	private String ipaddrhex;
	
	@Column(length = 15)
	private String ipaddr;
	
	@Column(length = 200)
	private String location;
	
	private Long deltasec;
	
	private Long lowestsec;
	
	/**
	 * supaya ga perlu ngirim semuanya, akan diupdate setelah dikirim ke ovo ?
	 */
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime lastprocesseddate;

	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@LastModifiedDate
	private LocalDateTime updateddate;
	
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@CreatedDate
	private LocalDateTime createddate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMacaddr() {
		return macaddr;
	}
	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public Long getDeltasec() {
		return deltasec;
	}
	public void setDeltasec(Long deltasec) {
		this.deltasec = deltasec;
	}
	public Long getLowestsec() {
		return lowestsec;
	}
	public void setLowestsec(Long lowestsec) {
		this.lowestsec = lowestsec;
	}
	public LocalDateTime getLastprocesseddate() {
		return lastprocesseddate;
	}
	public void setLastprocesseddate(LocalDateTime lastprocesseddate) {
		this.lastprocesseddate = lastprocesseddate;
	}
	public LocalDateTime getUpdateddate() {
		return updateddate;
	}
	public void setUpdateddate(LocalDateTime updateddate) {
		this.updateddate = updateddate;
	}
	public LocalDateTime getCreateddate() {
		return createddate;
	}
	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}
	@Override
	public String toString() {
		return "MacAddr [id=" + id + ", macaddr=" + macaddr + ", ipaddrhex=" + ipaddrhex + ", ipaddr=" + ipaddr
				+ ", location=" + location + ", deltasec=" + deltasec + ", lowestsec=" + lowestsec
				+ ", lastprocesseddate=" + lastprocesseddate + ", updateddate=" + updateddate + ", createddate="
				+ createddate + "]";
	}
	
	

}
