package com.anmol.actor;

import java.io.File;

import com.anmol.actor.core.ActorTrait;
import com.anmol.model.message.task.TaskCompleteMessage;
import com.anmol.util.ActorUtil;

import akka.actor.Props;

public class SupervisionActor extends ActorTrait {
	private final static String taskActorNamePrefix = "task-actor-";
	private int numTasksTotal = 0;
	private int numTasksDone = 0;

	public SupervisionActor(String tasksDirPath) {
		try {
			createTaskActors(tasksDirPath);
			logger.info(attachHeader("Actor created!"));
		} catch (Exception e) {
			logger.error(attachHeader("Actor creation failed!"), e);
		}
	}

	private void createTaskActor(File taskFilePath) {
		Props props = Props.create(TaskActor.class, taskFilePath.getAbsolutePath());
		String filename = taskFilePath.getName();
		String actorName = String.format("%s%s", taskActorNamePrefix, filename);
		getContext().actorOf(props, ActorUtil.sanitizeActorName(actorName));
	}

	private void createTaskActors(String tasksDirPath) {
		for (File fileEntry : (new File(tasksDirPath)).listFiles()) {
			if (fileEntry.isFile()) {
				this.numTasksTotal++;
				// Each of them will run independently.
				createTaskActor(fileEntry);
			}
		}
	}

	private void respondToTaskCompleteMessage() {
		this.numTasksDone++;
		String logMsg = String.format("%s/%s tasks done!", numTasksDone, numTasksTotal);
		logger.info(attachHeader(logMsg));
		// Kill the child actor.
		getContext().stop(getSender());
		if (this.numTasksDone == this.numTasksTotal) {
			logger.info(attachHeader("All tasks done!"));
			getContext().system().terminate();
		}
	}

	@Override
	public void onReceive(Object message) {
		String logMsg = String.format("Got new msg - '%s' from '%s'", message, getSender());
		logger.debug(attachHeader(logMsg));
		try {
			if (message instanceof TaskCompleteMessage) {
				// From TaskActor
				respondToTaskCompleteMessage();
			} else {
				logUnknownMsg(message);
			}
		} catch (Exception e) {
			logMsg = String.format("Exception while processing msg '%s':", message);
			logMsg = attachHeader(logMsg);
			logger.error(logMsg, e);
		}
	}
}