package com.ppu.fmc.local.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppu.fmc.local.domain.ConnectionLog;

public interface IConnectionLogRepository extends JpaRepository<ConnectionLog, Long>{

	List<ConnectionLog> findByUrlAndFirstpacketsecAndIpaddrhex(String url, long fps, String ipAddrHex);
	
}
