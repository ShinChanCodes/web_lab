package com.cnsi.asonetaskcreator;

import java.io.File;

public class AsOneTaskCreatorConstants {
	
	private AsOneTaskCreatorConstants() {
		
	}
	
	public static final int CQ_COL_NO_IN_ESTIMATION_SHEET = 1;
	public static final int CQ_DESC_COL_NO_IN_ESTIMATION_SHEET = 2;
	public static final int OTRS_COL_NO_IN_ESTIMATION_SHEET = 3;
	public static final int IR_TYPE_COL_NO_IN_ESTIMATION_SHEET = 4;
	public static final int COMPLEXITY_COL_NO_IN_ESTIMATION_SHEET = 5;
	
	public static final int ITERATION_COL_IN_ITER_IMPORT_SHEET = 0;
	public static final int STORY_NUM_COL_IN_ITER_IMPORT_SHEET = 2;
	public static final int STORY_TITLE_COL_IN_ITER_IMPORT_SHEET = 3;
	public static final int STORY_DESC_COL_IN_ITER_IMPORT_SHEET = 4;
	public static final int EST_HRS_COL_IN_ITER_IMPORT_SHEET = 7;
	public static final int ASSIGNED_TO_COL_IN_ITER_IMPORT_SHEET = 11;
	public static final int PLANNED_START_DATE_COL_IN_ITER_IMPORT_SHEET = 12;
	public static final int PLANNED_END_DATE_COL_IN_ITER_IMPORT_SHEET = 13;
	
	public static final String CONFIG_PROP_FILE_PATH = "Templates" + File.separator + "config.properties";
	public static final String ASONE_TASK_TEMPLATE_FILE_PATH = "Templates" + File.separator + "AsOne-Task-Templates.xls";
	public static final String ASONE_IMPORT_ITERATION_TEMPLATE_FILE_PATH = "Templates" + File.separator + "AsOne_Import_Iteration_Template.xls";

}
