package com.anmol.model.message.task;

import java.io.Serializable;

public class TaskInitMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String taskFilePath;

	public TaskInitMessage(String taskFilePath) {
		this.taskFilePath = taskFilePath;
	}

	public String getTaskFilePath() {
		return taskFilePath;
	}

	@Override
	public String toString() {
		return "TaskInitMessage [taskFilePath=" + taskFilePath + "]";
	}

}
