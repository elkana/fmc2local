package com.ppu.fmc.local.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ppu.fmc.local.domain.MacAddr;

public interface IMacAddrRepository extends JpaRepository<MacAddr, Long>{

	List<MacAddr> findByMacaddr(String macAddress);
	List<MacAddr> findByIpaddrhex(String ipaddrhex);
	List<MacAddr> findByIpaddrhexIsNotLike(String ipaddrhex);
	
	@Transactional
	@Modifying
	Long deleteByMacaddr(String macAddr);
}
