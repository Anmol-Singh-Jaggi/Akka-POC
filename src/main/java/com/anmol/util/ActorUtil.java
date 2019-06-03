package com.anmol.util;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;

public class ActorUtil {
	private final static Logger logger = LoggerFactory.getLogger(ActorUtil.class);

	public static String sanitizeActorName(String actorName) {
		return actorName.replace(" ", "$").replace("/", "$").replace(".", "$");
	}

	public static void sendMessageAsk(Object msg, ActorSelection actor, int minutes) {
		final Timeout timeout = new Timeout(minutes, TimeUnit.MINUTES);
		scala.concurrent.Future<Object> future = Patterns.ask(actor, msg, timeout);
		try {
			Object ack = Await.result(future, timeout.duration());
			String logMsg = String.format("Got ack '%s' for ask message '%s'", ack, msg);
			logger.debug(logMsg);
		} catch (Exception e) {
			String logMsg = String.format("Failed to receive ack for the ask message '%s'!", msg);
			logger.error(logMsg, e);
		}
	}
}
