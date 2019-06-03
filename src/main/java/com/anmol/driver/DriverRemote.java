package com.anmol.driver;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anmol.engine.Engine;
import com.anmol.util.IOUtil;

public class DriverRemote {
	private final static Logger logger = LoggerFactory.getLogger(DriverRemote.class);

	public static void main(String[] args) throws IOException {
		Engine engine = new Engine();
		Properties configProps = new Properties();
		configProps.load(new StringReader(IOUtil.getFileContents("classpath:///config.properties")));
		engine.start(configProps);
		logger.info("Engine started!");
	}

}
