package com.ppu.fmc.local.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppu.fmc.local.domain.HostIPMap;

public interface IHostIPMapRepository extends JpaRepository<HostIPMap, String>{
}
