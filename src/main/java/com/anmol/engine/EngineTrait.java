package com.anmol.engine;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anmol.util.IOUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

public abstract class EngineTrait {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String actorSystemConfigFilePath;
	private Config config;
	protected String actorSystemName;

	public String getActorSystemConfigFilePath() {
		return actorSystemConfigFilePath;
	}

	public void setActorSystemConfigFilePath(String actorSystemConfigFilePath) throws IOException {
		this.actorSystemConfigFilePath = actorSystemConfigFilePath;
		String configFileContents = IOUtil.getFileContents(actorSystemConfigFilePath);
		this.config = ConfigFactory.parseString(configFileContents);
	}

	public String getActorSystemName() {
		return actorSystemName;
	}

	public void setActorSystemName(String actorSystemName) {
		this.actorSystemName = actorSystemName;
	}

	protected ActorSystem createActorSystem() {
		ActorSystem actorSystem = ActorSystem.create(actorSystemName, config);
		String logMsg = String.format("New actor system created! - '%s'", actorSystem);
		logger.info(logMsg);
		return actorSystem;
	}

}
