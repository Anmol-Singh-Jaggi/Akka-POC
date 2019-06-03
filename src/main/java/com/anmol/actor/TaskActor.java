package com.anmol.actor;

import java.io.IOException;
import java.util.Arrays;

import com.anmol.actor.core.ActorTrait;
import com.anmol.model.message.task.TaskCompleteMessage;
import com.anmol.model.message.task.TaskInitMessage;
import com.anmol.util.IOUtil;

public class TaskActor extends ActorTrait {
	public TaskActor(String taskFilePath) {
		try {
			init();
			logger.info(attachHeader("Actor created!"));
			// Send message to self to start the task.
			// We are not directy starting the task in the constructor because if the task
			// hangs in the constructor, stopping the system becomes very hard.
			getSelf().tell(new TaskInitMessage(taskFilePath), getSelf());
		} catch (Exception e) {
			logger.error(attachHeader("Actor creation failed!"), e);
		}
	}

	private void init() {
	}

	private void performTask(String taskFilePath) throws IOException {
		String fileContents = IOUtil.getFileContents(taskFilePath);
		String[] tokens = fileContents.split(" ");
		Arrays.sort(tokens);
		IOUtil.writeToFile(String.join(" ", tokens), taskFilePath);
		// sort and write back.
		logger.info(attachHeader("Task done!"));
		getContext().parent().tell(new TaskCompleteMessage(), getSelf());
	}

	private void respondToTaskInitMessage(TaskInitMessage message) throws IOException {
		performTask(message.getTaskFilePath());
	}

	@Override
	public void onReceive(Object message) {
		String logMsg = String.format("Got new msg - '%s' from '%s'", message, getSender());
		logger.debug(attachHeader(logMsg));
		try {
			if (message instanceof TaskInitMessage) {
				// From self
				respondToTaskInitMessage((TaskInitMessage) message);
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