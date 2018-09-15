package com.ppu.fmc.firepower.handler;

/**
 * https://docs.spring.io/spring-data/jpa/docs/1.11.14.RELEASE/reference/html/
 */

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ppu.fmc.exception.IpLocationNotFoundException;
import com.ppu.fmc.firepower.model.HostIpMap;
import com.ppu.fmc.firepower.model.HostMacMap;
import com.ppu.fmc.firepower.model.IpLocation;
import com.ppu.fmc.local.domain.ConnectionLog;
import com.ppu.fmc.local.domain.HostIPMap;
import com.ppu.fmc.local.domain.HostMACMap;
import com.ppu.fmc.local.domain.MacAddr;
import com.ppu.fmc.local.repo.IConnectionLogRepository;
import com.ppu.fmc.local.repo.IHostIPMapRepository;
import com.ppu.fmc.local.repo.IHostMACMapRepository;
import com.ppu.fmc.local.repo.IMacAddrRepository;
import com.ppu.fmc.local.repo.IMacAddrUrlRepository;
import com.ppu.fmc.util.StopWatch;
import com.ppu.fmc.util.StringUtils;
import com.ppu.fmc.util.Utils;

@Component
public class FMCToLocal {
	static Logger log = LoggerFactory.getLogger(FMCToLocal.class);

	static String lastSentEventId = null;

	static long lastSentFirstPacketSec = 0;

	@Value("${fmc.fetch.rows:5}")
	private int fmcFetchRows;

	@Value("${local.data.ipclientmap}")
	private String ipclientmap;

	@Value("${local.data.mac.keep.days:4}")
	private int keepOldMacAddrDays;

	@Value("${local.data.mac.duplicate.keep:true}")
	private boolean keepDuplicateMacAddr;

	@Autowired
	@Qualifier("fmcEntityManagerFactory")
	EntityManager em;

	@Autowired
	IMacAddrRepository macAddrRepo;

	@Autowired
	IMacAddrUrlRepository macAddrUrlRepo;

	@Autowired
	IConnectionLogRepository connLogRepo;
	@Autowired
	IHostIPMapRepository hostIPMapRepo;
	@Autowired
	IHostMACMapRepository hostMACMapRepo;

	@SuppressWarnings("rawtypes")
	public static List loadIpLocationCsv(String csvFilename) throws JsonProcessingException, IOException {
		CsvSchema csvSchema = new CsvMapper().typedSchemaFor(IpLocation.class).withHeader();
		List list = new CsvMapper().readerFor(IpLocation.class)
				.with(csvSchema.withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR))
				.readValues(new File(csvFilename)).readAll();

		/*
		 * for (int i = 0; i < list.size(); i++) { IpLocation _obj = (IpLocation)
		 * list.get(i);
		 * 
		 * System.out.println(_obj); }
		 */

		return list;

	}

	public static String getLocation(List<?> list, String ipAddress) throws IpLocationNotFoundException {
		if (StringUtils.isEmpty(ipAddress) || list == null || list.size() < 1)
			return "";

		// diambil
		StringTokenizer token = new StringTokenizer(ipAddress, ".");

		if (token.countTokens() != 4) {
			throw new IllegalArgumentException("IP address must be in the format 'xxx.xxx.xxx.xxx'");
		}

		int dots = 0;
		String byte1 = "";
		String byte2 = "";
		String byte3 = "";
		while (token.hasMoreTokens()) {
			++dots;

			if (dots == 1) {
				byte1 = token.nextToken();
			} else if (dots == 2) {
				byte2 = token.nextToken();
			} else if (dots == 3) {
				byte3 = token.nextToken();
			} else
				break;
		}

		String client3 = byte1 + "." + byte2 + "." + byte3;

		for (int i = 0; i < list.size(); i++) {
			IpLocation _obj = (IpLocation) list.get(i);

			StringTokenizer _token = new StringTokenizer(_obj.getIp(), ".");

			// segmen class A handler
			if (_token.countTokens() == 3) {
				if (_obj.getIp().equals(client3)) {
					return _obj.getLabel();
				}
			} else if (_token.countTokens() == 4) {
				if (_obj.getIp().equals(ipAddress)) {
					return _obj.getLabel();
				}
			}

		}

		throw new IpLocationNotFoundException(ipAddress);
	}
	
	private void method6() {
		
		List listIpLocation = null;
		try {
			listIpLocation = loadIpLocationCsv(ipclientmap);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		cleanUpOldData();
		
		updatePreRequisitesTables();

		List<Object[]> connLogRows = findLastConnectionLogItem(lastSentFirstPacketSec);

		while (true) {
			log.debug("--> findLastConnectionLogItem = " + (connLogRows.size() < 1 ? "0"
					: connLogRows.size() + " rows --> " + StringUtils.objectsToString(connLogRows.get(0), ", ")));

			if (connLogRows.size() < 1)
				break;

			// jumlah hostIps dan hostMacs belum tentu sama dengan jumlah di connectionlogs
			// krn hanya simpan yg unik saja

			List<String> ipAddressesInHexa = new ArrayList<String>();

			// a. first, collect all ipaddress in hexa
			for (int i = 0; i < connLogRows.size(); i++) {
				Object[] fields = connLogRows.get(i);

				long firstPacketSec = Long.parseLong(String.valueOf(fields[0]));
				String url = String.valueOf(fields[1]);
				String ipAddressInHexa = String.valueOf(fields[2]);
				String ipAddress = StringUtils.fixIPAddress(String.valueOf(fields[3]));

				boolean found = false;

				for (String _s : ipAddressesInHexa) {
					if (_s.equals(ipAddressInHexa)) {
						found = true;
						break;
					}
				}

				if (!found)
					ipAddressesInHexa.add(ipAddressInHexa);
			}

			// b. collect all host id
			List<HostIpMap> hostIPAddresses = findHostIPAddresses(ipAddressesInHexa);
			// last see//n here

			// c. collect all mac
			List<HostMacMap> hostMacAddresses = findHostMacAddresses(hostIPAddresses);

			// final construct
			StopWatch sw = StopWatch.AutoStart();

			for (int i = 0; i < connLogRows.size(); i++) {

				Object[] fields = connLogRows.get(i);

				log.debug("row[{}/{}, fps>{}({})] = " + StringUtils.objectsToString(fields, ", "), (i + 1), connLogRows.size(), lastSentFirstPacketSec, Utils.converToLDT(lastSentFirstPacketSec));

				long firstPacketSec = Long.parseLong(String.valueOf(fields[0]));
				lastSentFirstPacketSec = firstPacketSec;

				String url = String.valueOf(fields[1]);
				String ipAddressInHexa = String.valueOf(fields[2]);
				String ipAddress = StringUtils.fixIPAddress(String.valueOf(fields[3]));
				String macAddress = "";
				String iplocation = "";
				try {
					iplocation = getLocation(listIpLocation, ipAddress);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

				String hostIdInHexa = null;
				for (HostIpMap him : hostIPAddresses) {
					if (him.getIpAddressInHexa().equals(ipAddressInHexa)) {
						hostIdInHexa = him.getHostIdInHexa();
						break;
					}
				}

				if (hostIdInHexa != null)
					for (HostMacMap hmm : hostMacAddresses) {
						if (hmm.getHostIdInHexa().equals(hostIdInHexa)) {
							macAddress = Utils.convertHexToMacAddress(hmm.getMacAddressInHexa());
							break;
						}
					}

				// cari dulu di local,tp hanya yg ada macaddress yg akan disimpan
				if (StringUtils.isEmpty(macAddress)) {
					log.error("SKIPPED null macaddress for IP {}", ipAddress);
					continue;
				}
				
				// DUMP isi table connectionlog terbaru ke local
				List<ConnectionLog> _existingConnLog = connLogRepo.findByUrlAndFirstpacketsecAndIpaddrhex(url, firstPacketSec, ipAddressInHexa);
				if (_existingConnLog.isEmpty()) {
					ConnectionLog _connLog = new ConnectionLog();
					_connLog.setFirstpacketsec(firstPacketSec);
					_connLog.setCreateddate(LocalDateTime.now());
					_connLog.setIpaddr(ipAddress);
					_connLog.setIpaddrhex(ipAddressInHexa);
					_connLog.setIplocation(iplocation);
//					_connLog.setLastpacketsec(lastpacketsec);
					_connLog.setMacaddr(macAddress);
					_connLog.setUrl(url);
					_connLog.setFpsdate(Utils.converToLDT(firstPacketSec));

					connLogRepo.save(_connLog);
				}
				

				MacAddr macAddr;
				List<MacAddr> rows = macAddrRepo.findByMacaddr(macAddress);

				if (rows.isEmpty()) {
					macAddr = new MacAddr();
					macAddr.setMacaddr(macAddress);
					macAddr.setLastprocesseddate(LocalDateTime.now());	//akan diupdate lg oleh local2fmc
					macAddr.setCreateddate(LocalDateTime.now());

				} else {
					macAddr = rows.get(0);
					macAddr.setUpdateddate(LocalDateTime.now());
					
					if (!macAddr.getIpaddrhex().trim().equals(ipAddressInHexa)) {
						log.warn("IP CHANGED from {} to {} for mac {}.", macAddr.getIpaddr(), ipAddress, macAddr.getMacaddr());
						
						// cek apakah ipnya udah ada yg punya ?
						List<MacAddr> prevIPOwners = macAddrRepo.findByIpaddrhex(ipAddressInHexa);
						
						if (prevIPOwners.size() > 1) {
							log.warn("FOUND DUPLICATE IP {}. Used by {} MacAddresses", ipAddress, prevIPOwners.size());
							
							// TODO delete old mac here ? just remove inactive mac
							List<MacAddr> _others = macAddrRepo.findByIpaddrhexIsNotLike(ipAddressInHexa);
							for (int k = 0; k < _others.size(); k++) {
								log.warn("DUPLICATED MAC[{}/{}]={}", k+1, _others.size(), _others.get(k));
								
								if (!keepDuplicateMacAddr && macAddrRepo.deleteByMacaddr(_others.get(k).getMacaddr()) > 0) {
									log.info("MACADDRESS {} REMOVED", _others.get(k).getMacaddr());
								}
							}
						}
					}

				}

				//mungkin saja ipnya udah ganti
				macAddr.setIpaddrhex(ipAddressInHexa);
				macAddr.setIpaddr(ipAddress);
				macAddr.setLocation(iplocation);
				
				macAddrRepo.save(macAddr);

				// macaddrurl tidak taruh disini spy ga ngelock
			
			}

			log.info("Elapsed time: {}", sw.stopAndGetAsString());

			connLogRows = findNextConnectionLogs(lastSentFirstPacketSec, fmcFetchRows);

		}
		
	}

	
	private void cleanUpOldData() {
		// TODO Auto-generated method stub
		
	}

	private List<HostIpMap> findHostIPAddresses(List<String> ipAddrInHexa) {

		List<HostIpMap> rows = new ArrayList<>();

		if (ipAddrInHexa.size() < 1)
			return rows;

		// construct list into csv
		StringBuffer _csv = new StringBuffer("'" + ipAddrInHexa.get(0) + "'");
		for (int i = 1; i < ipAddrInHexa.size(); i++) {
			_csv.append(",'").append(ipAddrInHexa.get(i)).append("'");
		}

		StringBuffer sb = new StringBuffer("SELECT hex(b.host_id), hex(b.ipaddr) FROM rna_host_ip_map b");
		sb.append(" WHERE hex(b.ipaddr) in (").append(_csv.toString()).append(")");

		Query q = em.createNativeQuery(sb.toString());

		try {
			List resultList = q.getResultList();

			if (resultList.size() < 1)
				return rows;

			for (int i = 0; i < resultList.size(); i++) {
				Object[] _fields = (Object[]) resultList.get(i);

				HostIpMap _obj = new HostIpMap();
				_obj.setHostIdInHexa(String.valueOf(_fields[0]));
				_obj.setIpAddressInHexa(String.valueOf(_fields[1]));

				rows.add(_obj);

			}

			log.debug("findHostIPAddresses return {} rows", resultList.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return rows;

	}

	private List<HostMacMap> findHostMacAddresses(List<HostIpMap> hostIdsInHexa) {

		List<HostMacMap> rows = new ArrayList<>();

		if (hostIdsInHexa.size() < 1)
			return rows;

		// construct list into csv
		StringBuffer _csv = new StringBuffer("'" + hostIdsInHexa.get(0).getHostIdInHexa() + "'");
		for (int i = 1; i < hostIdsInHexa.size(); i++) {
			_csv.append(",'").append(hostIdsInHexa.get(i).getHostIdInHexa()).append("'");
		}

		StringBuffer sb = new StringBuffer("SELECT hex(c.host_id), hex(c.mac_address) FROM rna_host_mac_map c");
		sb.append(" WHERE hex(c.host_id) in (").append(_csv.toString()).append(")");

		Query q = em.createNativeQuery(sb.toString());

		try {
			List resultList = q.getResultList();

			if (resultList.size() < 1)
				return rows;

			for (int i = 0; i < resultList.size(); i++) {
				Object[] _fields = (Object[]) resultList.get(i);

				HostMacMap _obj = new HostMacMap();
				_obj.setHostIdInHexa(String.valueOf(_fields[0]));
				_obj.setMacAddressInHexa(String.valueOf(_fields[1]));

				rows.add(_obj);
			}

			log.debug("findHostMacAddresses return {} rows", resultList.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return rows;

	}

	private List<Object[]> findNextConnectionLogs(long fromLastFirstPacketSec, int rowCount) {

		List<Object[]> rows = new ArrayList<>();

		StringBuffer sb = new StringBuffer(
				"SELECT a.first_packet_sec, a.url, hex(a.initiator_ipaddr), inet6_ntoa(a.initiator_ipaddr) FROM connection_log a");
		sb.append(" WHERE a.url <> ''");
		// sb.append(" WHERE char_length(a.url) <> 0");
		sb.append(" AND a.first_packet_sec > ").append(fromLastFirstPacketSec);
		// sb.append(" AND a.url <> '").append(fromLastUrl).append("'");
		// sb.append(" AND hex(a.initiator_ipaddr) <>
		// '").append(fromLastIpAddrInHexa).append("'");
		// sb.append(" ORDER BY a.first_packet_sec LIMIT ").append(rowCount);
		sb.append(" ORDER BY a.first_packet_sec ASC");
		if (rowCount > 0)
			sb.append(" LIMIT ").append(rowCount);

		Query q = em.createNativeQuery(sb.toString());
//		"select a.first_packet_sec, a.url, hex(a.initiator_ipaddr) from connection_log a where char_length(a.url) <> 0 and a.first_packet_sec > 1531987347 and a.url <> 'http://batsavcdn.ksmobile.net/bsi' order by a.first_packet_sec limit 5";

		try {
			List resultList = q.getResultList();

			if (resultList.size() < 1)
				return rows;

//		Object[] author = (Object[]) q.getSingleResult();

			rows.addAll(resultList);

			log.debug("findNextConnectionLogs return {} rows", resultList.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return rows;

	}

	private List<Object[]> findLastConnectionLogItem(long lastFirstPacketId) {

		List<Object[]> rows = new ArrayList<>();

		StringBuffer sb = new StringBuffer(
				"select a.first_packet_sec, a.url, hex(a.initiator_ipaddr), inet6_ntoa(a.initiator_ipaddr) from connection_log a");
		sb.append(" where a.url <> ''");

		if (lastFirstPacketId > 0)
			sb.append(" and a.first_packet_sec = ").append(lastFirstPacketId);

		sb.append(" order by a.first_packet_sec DESC limit 1");

		// Query q = em.createNativeQuery("select a.first_packet_sec, a.url,
		// hex(a.initiator_ipaddr), inet6_ntoa(a.initiator_ipaddr) from connection_log a
		// where char_length(a.url) <> 0 order by a.first_packet_sec DESC limit 1");
		Query q = em.createNativeQuery(sb.toString());

		try {
			List resultList = q.getResultList();

			if (resultList.size() < 1)
				return rows;

//		Object[] author = (Object[]) q.getSingleResult();

			rows.addAll(resultList);

			log.debug("findLastConnectionLogItem return {} rows", resultList.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return rows;

	}

//	@Transactional(readOnly = true)
//	@Override
	public boolean execute() throws Exception {

		log.debug("fmc.fetch.rows :: {}", fmcFetchRows);
		log.debug("local.data.ipclientmap :: {}", ipclientmap);
		log.debug("local.data.mac.keep.days :: {}", keepOldMacAddrDays);
		log.debug("local.data.mac.duplicate.keep :: {}", keepDuplicateMacAddr);
		
		log.debug("lastSentFirstPacketSec :: {} -> {}", lastSentFirstPacketSec, Utils.converToLDT(lastSentFirstPacketSec));
		
		try {
			method6();
		} catch (Exception e) {
			throw e;
		}

		return true;
	}

	private void updatePreRequisitesTables() {
		Query q = em.createNativeQuery("SELECT hex(b.host_id), hex(b.ipaddr), inet6_ntoa(b.ipaddr) FROM rna_host_ip_map b");
		try {
			List resultList = q.getResultList();

			if (resultList.size() < 1)
				return;

			for (int i = 0; i < resultList.size(); i++) {
				Object[] _fields = (Object[]) resultList.get(i);

				HostIPMap _obj = new HostIPMap();
				_obj.setHostidhex(String.valueOf(_fields[0]));
				_obj.setIpaddrhex(String.valueOf(_fields[1]));
				_obj.setIpaddr(StringUtils.fixIPAddress(String.valueOf(_fields[2])));
				_obj.setCreateddate(LocalDateTime.now());

//				if (hostIPMapRepo.findOne(_obj.getHostidhex()) == null)
					hostIPMapRepo.save(_obj);

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		q = em.createNativeQuery("SELECT hex(c.host_id), hex(c.mac_address), c.mac_vendor FROM rna_host_mac_map c");
		try {
			List resultList = q.getResultList();
			
			if (resultList.size() < 1)
				return;
			
			for (int i = 0; i < resultList.size(); i++) {
				Object[] _fields = (Object[]) resultList.get(i);
				
				HostMACMap _obj = new HostMACMap();
				_obj.setHostidhex(String.valueOf(_fields[0]));
				_obj.setMacaddr(String.valueOf(_fields[1]));
				_obj.setMacvendor(String.valueOf(_fields[2]));
				_obj.setCreateddate(LocalDateTime.now());
				
				if (hostMACMapRepo.findOne(_obj.getHostidhex()) == null)
					hostMACMapRepo.save(_obj);
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
