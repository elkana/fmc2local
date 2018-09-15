package com.ppu.fmc;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ppu.fmc.firepower.handler.FMCToLocal;
import com.ppu.fmc.util.Utils;

@Component
@PropertySources({
	@PropertySource("classpath:config.properties"),
})
public class TaskRunner {
	private static final Logger log = LoggerFactory.getLogger(TaskRunner.class);
	
	@Value("${developer:false}")
	private boolean developer;
	
	@Value("${scheduler.disable:false}")
	private boolean disableJob;

	@Autowired
	FMCToLocal fmc2ovo;
	
	@Scheduled(initialDelay = 1000, fixedDelayString = "${fmc.schedule.delay.seconds:10}000")
	public void run() throws Exception{
		
		if (disableJob) {
			log.warn("Scheduled Job is currently disabled. Please run the program again or set scheduler.disable=false");
			System.exit(0);
			
			return;
		}
		
		log.debug("Developer mode  is :: {}", developer);
		
		try {
			boolean result = fmc2ovo.execute();
			log.info(FMCToLocal.class.getSimpleName() + " return " + result + "\n");
//		}catch(SQLGrammerException e) {
//			log.error(e.getMessage(), e);
//			System.exit(0);
//			throw e;
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		displayStat();
	}
	
	private void displayStat() {
		StringBuilder sbAppInfo = new StringBuilder();
		
		try {
			Properties p = new Properties();
			InputStream is = Fmc2LocalApplication.class.getResourceAsStream(Fmc2LocalApplication.POM_LOCATION);
			if (is != null) {
				p.load(is);
				sbAppInfo.append("artifactId=").append(p.getProperty("artifactId", ""));
				
				sbAppInfo.append(", version=").append(p.getProperty("version", ""));
			}
			is.close();
			is = null;
		} catch (Exception e) {
			// ignore
		}

		Utils.displayStat("(" + sbAppInfo.toString() + ")");
		
		log.info("...Waiting for next cycle...\n");
	}

}
