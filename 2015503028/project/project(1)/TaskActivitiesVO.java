package com.cnsi.asonetaskcreator;

import java.io.Serializable;

public class TaskActivitiesVO implements Serializable {

	private static final long serialVersionUID = 1970045190128713377L;
	
	private String team;

	private String taskKeyWord;

	private String taskDescription;

	private float timePercent;

	private boolean createFlag;

	public TaskActivitiesVO(String taskKeyWord, String taskDescription, float timePercent) {
		this.taskKeyWord = taskKeyWord;
		this.taskDescription = taskDescription;
		this.timePercent = timePercent;
		this.createFlag = false;
	}
	
	public TaskActivitiesVO(String taskKeyWord, String taskDescription, float timePercent, boolean createFlag) {
		this.taskKeyWord = taskKeyWord;
		this.taskDescription = taskDescription;
		this.timePercent = timePercent;
		this.createFlag = createFlag;
	}

	public TaskActivitiesVO(String team, String taskKeyWord, String taskDescription, float timePercent, boolean createFlag) {
		this.team = team;
		this.taskKeyWord = taskKeyWord;
		this.taskDescription = taskDescription;
		this.timePercent = timePercent;
		this.createFlag = createFlag;
	}

	/**
	 * @return the taskKeyWord
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @return the taskKeyWord
	 */
	public String getTaskKeyWord() {
		return taskKeyWord;
	}

	/**
	 * @param taskKeyWord
	 *            the taskKeyWord to set
	 */
	public void setTaskKeyWord(String taskKeyWord) {
		this.taskKeyWord = taskKeyWord;
	}

	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * @param taskDescription
	 *            the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	/**
	 * @return the timePercent
	 */
	public float getTimePercent() {
		return timePercent;
	}

	/**
	 * @param timePercent
	 *            the timePercent to set
	 */
	public void setTimePercent(float timePercent) {
		this.timePercent = timePercent;
	}

	/**
	 * @return the createFlag
	 */
	public boolean isCreateFlag() {
		return createFlag;
	}

	/**
	 * @param createFlag the createFlag to set
	 */
	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}

	public String toString() {
		return taskKeyWord + "; " + taskDescription + "; " + timePercent;
	}
}
