package com.anmol.engine;

import java.io.IOException;
import java.util.Properties;

import com.anmol.actor.SupervisionActor;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Engine extends EngineTrait {
	public final static String supervisionActorName = "supervision-actor";

	private void createSupervisionActor(ActorSystem actorSystem, Properties configProps) {
		String tasksFileDirPath = configProps.getProperty("taskFiles.dirPath");
		Props actorProps = Props.create(SupervisionActor.class, tasksFileDirPath);
		actorSystem.actorOf(actorProps, supervisionActorName);
	}

	public void start(Properties configProps) throws IOException {
		setActorSystemName(configProps.getProperty("actorSystem.name"));
		setActorSystemConfigFilePath(configProps.getProperty("actorSystem.config.filePath"));
		ActorSystem actorSystem = createActorSystem();
		createSupervisionActor(actorSystem, configProps);
	}

}
