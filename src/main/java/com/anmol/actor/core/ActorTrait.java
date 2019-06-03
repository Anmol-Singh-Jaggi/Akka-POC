package com.anmol.actor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public abstract class ActorTrait extends UntypedActor {

	protected final ActorRef selfActorRef = getSelf();
	protected final String selfActorName = selfActorRef.path().name();
	protected final String selfActorPath = selfActorRef.path().toString();
	protected final String selfClassName = getContext().props().actorClass().getSimpleName();
	private final String logHeader = String.format("%s ('%s')", selfClassName, selfActorName);
	protected final Logger logger = LoggerFactory.getLogger(getSelf().path().toString());

	public String attachHeader(Object obj) {
		String logMsg = String.format("%s: %s", logHeader, obj);
		return logMsg;
	}

	public void logUnknownMsg(Object msg) {
		String logMsg = String.format("Received unknown message '%s'!", msg);
		logMsg = attachHeader(logMsg);
		logger.error(logMsg);
	}

	public ActorSelection getSiblingActor(String actorName) {
		String actorPath = String.format("../%s", actorName);
		ActorSelection actorSelection = getContext().actorSelection(actorPath);
		return actorSelection;
	}

}
