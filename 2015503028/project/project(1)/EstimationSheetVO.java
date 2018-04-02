package com.cnsi.asonetaskcreator;

public class EstimationSheetVO {
	
	private String subsystem;
	
	private String cqNumber;
	
	private String otrs;
	
	private String cqDescription;
	
	private String typeIR;
	
	private String complexity;
	
	private float estimatedHours;
	
	/**
	 * @return the subsystem
	 */
	public String getSubsystem() {
		return subsystem;
	}

	/**
	 * @param subsystem the subsystem to set
	 */
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * @return the cqNumber
	 */
	public String getCqNumber() {
		return cqNumber;
	}

	/**
	 * @param cqNumber the cqNumber to set
	 */
	public void setCqNumber(String cqNumber) {
		this.cqNumber = cqNumber;
	}

	/**
	 * @return the otrs
	 */
	public String getOtrs() {
		return otrs;
	}

	/**
	 * @param otrs the otrs to set
	 */
	public void setOtrs(String otrs) {
		this.otrs = otrs;
	}

	/**
	 * @return the cqDescription
	 */
	public String getCqDescription() {
		return cqDescription;
	}

	/**
	 * @param cqDescription the cqDescription to set
	 */
	public void setCqDescription(String cqDescription) {
		this.cqDescription = cqDescription;
	}

	/**
	 * @return the typeIR
	 */
	public String getTypeIR() {
		return typeIR;
	}

	/**
	 * @param typeIR the typeIR to set
	 */
	public void setTypeIR(String typeIR) {
		this.typeIR = typeIR;
	}

	/**
	 * @return the complexity
	 */
	public String getComplexity() {
		return complexity;
	}

	/**
	 * @param complexity the complexity to set
	 */
	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	/**
	 * @return the estimatedHours
	 */
	public float getEstimatedHours() {
		return estimatedHours;
	}

	/**
	 * @param estimatedHours the estimatedHours to set
	 */
	public void setEstimatedHours(float estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public String toString() {
		return cqNumber + "; " + cqDescription + "; " + typeIR + "; " + estimatedHours;
	}

}
