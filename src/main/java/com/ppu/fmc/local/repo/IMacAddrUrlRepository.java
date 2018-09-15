package com.ppu.fmc.local.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppu.fmc.local.domain.MacAddrUrl;

public interface IMacAddrUrlRepository extends JpaRepository<MacAddrUrl, Long> {

	List<MacAddrUrl> findByMacaddrOrIpaddr(String macAddress, String ipAddress);

}
