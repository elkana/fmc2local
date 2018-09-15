package com.ppu.fmc.local.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppu.fmc.local.domain.HostMACMap;

public interface IHostMACMapRepository extends JpaRepository<HostMACMap, String>{
}
