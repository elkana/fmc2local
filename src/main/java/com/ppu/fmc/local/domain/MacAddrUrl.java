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
@Table(name = "macaddrurl")
public class MacAddrUrl {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 36)
	private String macaddr;
	
	@Column(length = 15)
	private String ipaddr;

	@Column(length = 200)
	private String iplocation;

	@Column(length = 4096)
	private String url;
	private Long firstpacketsec;
	private Long lastpacketsec;
	
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime sentdate;
	
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
	
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	public String getIplocation() {
		return iplocation;
	}
	public void setIplocation(String iplocation) {
		this.iplocation = iplocation;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	
	public LocalDateTime getSentdate() {
		return sentdate;
	}
	public void setSentdate(LocalDateTime sentdate) {
		this.sentdate = sentdate;
	}
	public LocalDateTime getCreateddate() {
		return createddate;
	}
	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}
	@Override
	public String toString() {
		return "MacAddrUrl [id=" + id + ", macaddr=" + macaddr + ", ipaddr=" + ipaddr + ", iplocation=" + iplocation
				+ ", url=" + url + ", firstpacketsec=" + firstpacketsec + ", lastpacketsec=" + lastpacketsec
				+ ", sentdate=" + sentdate + ", createddate=" + createddate + "]";
	}

	

}
