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

import com.ppu.fmc.util.LocalDateTimeAttributeConverter;

@Entity
@Table(name = "connection_log")
public class ConnectionLog {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length = 36)
	private String macaddr;
	
	@Column(length = 40)
	private String ipaddrhex;
	
	@Column(length = 15)
	private String ipaddr;
	
	@Column(length = 4096)
	private String url;

	@Column(length = 200)
	private String iplocation;
	
	private Long firstpacketsec;
	private Long lastpacketsec;
	
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fpsdate;

	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@CreatedDate
	private LocalDateTime createddate;
	
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime sentdate;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIplocation() {
		return iplocation;
	}

	public void setIplocation(String iplocation) {
		this.iplocation = iplocation;
	}

	public Long getFirstpacketsec() {
		return firstpacketsec;
	}

	public void setFirstpacketsec(Long firstpacketsec) {
		this.firstpacketsec = firstpacketsec;
	}

	public Long getLastpacketsec() {
		return lastpacketsec;
	}

	public void setLastpacketsec(Long lastpacketsec) {
		this.lastpacketsec = lastpacketsec;
	}

	public LocalDateTime getFpsdate() {
		return fpsdate;
	}

	public void setFpsdate(LocalDateTime fpsdate) {
		this.fpsdate = fpsdate;
	}

	public LocalDateTime getCreateddate() {
		return createddate;
	}

	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}

	public LocalDateTime getSentdate() {
		return sentdate;
	}

	public void setSentdate(LocalDateTime sentdate) {
		this.sentdate = sentdate;
	}

	@Override
	public String toString() {
		return "ConnectionLog [id=" + id + ", macaddr=" + macaddr + ", ipaddrhex=" + ipaddrhex + ", ipaddr=" + ipaddr
				+ ", url=" + url + ", iplocation=" + iplocation + ", firstpacketsec=" + firstpacketsec
				+ ", lastpacketsec=" + lastpacketsec + ", fpsdate=" + fpsdate + ", createddate=" + createddate
				+ ", sentdate=" + sentdate + "]";
	}
	
	
}
