package com.cnsi.asonetaskcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AsOneTaskImportExcelGenerator {

	private String STORY_NUMBER_CREATION_FORMAT = "";
	private String ITERATION_NO = "";
	private String START_DATE = "";
	private String END_DATE = "";
	private String webINFRootDir = "";

	private Map<String, TaskActivitiesVO> mapActivities;
	private Map<String, Float> estimatedHrsByComplexity;
	private Map<String, String> leadsMailIdBysubsystem;
	private List<EstimationSheetVO> estimationSheetVOs;
	private Properties configProperties;

	private String ASONE_IMPORT_ITERATION_DEST_FILE;
	
	public void setDestinationFile(String aDestinationFile) {
		this.ASONE_IMPORT_ITERATION_DEST_FILE=aDestinationFile;
	}

	public AsOneTaskImportExcelGenerator(String aWebINFRootDir, String aIterationNo, String aIterationStartDate,String aIterationEndDate) {
		configProperties = new Properties();
		estimationSheetVOs = new ArrayList<EstimationSheetVO>();

        try {
        	this.setWebINFRootDir(aWebINFRootDir);
        	String parentPath = aWebINFRootDir + File.separator;
            FileInputStream fs = new FileInputStream(parentPath + AsOneTaskCreatorConstants.CONFIG_PROP_FILE_PATH);
			configProperties.load(fs);

			ITERATION_NO = aIterationNo;
			START_DATE = aIterationStartDate;
			END_DATE = aIterationEndDate;
			STORY_NUMBER_CREATION_FORMAT = configProperties.getProperty("STORY_NUMBER_CREATION_FORMAT");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDestinationFile() {
		return ASONE_IMPORT_ITERATION_DEST_FILE;
	}
	/*
	 * This method getFileExtension() 
	 * aFilePath is argument of File Value 
	 * return the String Value with File Extension Name
	 */
	public String getFileExtension(File aFilePath) {
		String returnFileExtension = "";
		try {
			returnFileExtension = Files.probeContentType(aFilePath.toPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnFileExtension;
	}

	/*
	 * This method roundedToTwoDecimal() 
	 * aFloatValue is argument of Float Value 
	 * return the Float Value with 2 decimal places Ex: if Arg is 9.0500002 then return the number as 9.05
	 */
	public float roundedToTwoDecimal(float aFloatValue) {
		return Math.round(aFloatValue * 100) / 100.0f;
	}

	/*
	 * This method convertToActualPercentNumber() 
	 * aPercentNumberInString is argument of String Value from Excel File of Column:Time_Percent in Excel AsOne-Task-Templates.xls 
	 * return the Float Value with Actual Percent Number 
	 * Ex: 
	 * 	1. if Arg is 0.05, then return the number as 5.0 
	 * 	2. if Arg is 0.3, then return the number as 30.0 
	 * 	3. if Arg is 0.075, then return the number as 7.5000005
	 */
	public float convertToActualPercentNumber(String aPercentNumberInString) {

		float actualPercentNumber = 0;
		try {
			actualPercentNumber = Float.parseFloat(aPercentNumberInString);
			actualPercentNumber = actualPercentNumber * 100;
		} catch (NumberFormatException e) {
			actualPercentNumber = 0;
		}

		return actualPercentNumber;
	}

	/*
	 * This method getCellValueAsString() 
	 * cell is argument of Cell Object 
	 * return the String Value from the conversion of CELL_TYPE_BOOLEAN, CELL_TYPE_NUMERIC, CELL_TYPE_FORMULA
	 */
	public String getCellValueAsString(Cell cell) {
		String cellValueAsString = "";

		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellValueAsString = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValueAsString = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellValueAsString = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValueAsString = String.valueOf(cell.getNumericCellValue());
				break;
			}
		}

		return cellValueAsString;
	}

	/*
	 * This method loadActivitiesDetails() 
	 * 				to load the Activities Information from the Sheet of Activities in the Template: AsOne-Task-Templates.xls into 
	 * 					HashMap with KEY as String value of Activities_Keyword and 
	 * 						VALUE as TaskActivitiesVO class
	 */
	public void loadActivitiesDetails() {
		mapActivities = new LinkedHashMap<String, TaskActivitiesVO>();
		estimatedHrsByComplexity = new HashMap<String, Float>();
		leadsMailIdBysubsystem = new HashMap<String, String>();
		String parentPath = getWebINFRootDir() + File.separator;
		String asOneTaskTemplateExcel = parentPath + AsOneTaskCreatorConstants.ASONE_TASK_TEMPLATE_FILE_PATH;

		System.out.println("\nJAR Parent Location Path: " + parentPath);
		System.out.println("\nAsOne Task Template Excel Path: " + asOneTaskTemplateExcel);

		try {
			Workbook wbActivityTemplateSheetExcel = null;

			File fAsOneTaskTemplateExcel = new File(asOneTaskTemplateExcel);
			String estimationFileExtension = getFileExtension(fAsOneTaskTemplateExcel);
			System.out.println("\nAsOne Task Template Excel File Version is >= 2007 : " + (!estimationFileExtension.contains("vnd.ms-excel")));

			if (estimationFileExtension.contains("vnd.ms-excel")) {
				wbActivityTemplateSheetExcel = new HSSFWorkbook(new FileInputStream(fAsOneTaskTemplateExcel));
			} else {
				wbActivityTemplateSheetExcel = new XSSFWorkbook(new FileInputStream(fAsOneTaskTemplateExcel));
			}

			Sheet activityTemplateSheet = wbActivityTemplateSheetExcel.getSheet("Activities");
			Iterator<Row> iterator = activityTemplateSheet.iterator();
			boolean isFirstRow = true;

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (isFirstRow == false) {
					String taskKeyWord = getCellValueAsString(nextRow.getCell(1));
					String taskDescription = getCellValueAsString(nextRow.getCell(2));
					float timePercent = convertToActualPercentNumber(getCellValueAsString(nextRow.getCell(3)));
					String sCreateFlag = getCellValueAsString(nextRow.getCell(4));
					String sTeam = getCellValueAsString(nextRow.getCell(6));

					if (!"".equals(taskKeyWord)) {
						boolean createFlag = (sCreateFlag != null && sCreateFlag.trim().toUpperCase().equals("Y"));
						mapActivities.put(taskKeyWord, new TaskActivitiesVO(sTeam, taskKeyWord, taskDescription, roundedToTwoDecimal(timePercent), createFlag));
					}
				}

				isFirstRow = false;
			}

			activityTemplateSheet = wbActivityTemplateSheetExcel.getSheet("DefectSeverity");
			Row activityTemplateSheetRowObj;
			for (int rowIndex = 1; rowIndex < activityTemplateSheet.getPhysicalNumberOfRows(); rowIndex++) {
				activityTemplateSheetRowObj = activityTemplateSheet.getRow(rowIndex);

				String sDefectSeverity = getCellValueAsString(activityTemplateSheetRowObj.getCell(0));
				String sEstimatedHours = getCellValueAsString(activityTemplateSheetRowObj.getCell(1));
				float estimatedHours = 0;

				try {
					estimatedHours = Float.valueOf(sEstimatedHours);
				}catch(NumberFormatException nfe){
					estimatedHours = 0;
				}

				if(!"".equals(sDefectSeverity)) {
					estimatedHrsByComplexity.put(sDefectSeverity.toUpperCase(), estimatedHours);
				}
			}
			
			activityTemplateSheet = wbActivityTemplateSheetExcel.getSheet("Subsystems");
			for (int rowIndex = 1; rowIndex < activityTemplateSheet.getPhysicalNumberOfRows(); rowIndex++) {
				activityTemplateSheetRowObj = activityTemplateSheet.getRow(rowIndex);

				String sSubsystem = getCellValueAsString(activityTemplateSheetRowObj.getCell(0));
				String sSubsystemKeyword = getCellValueAsString(activityTemplateSheetRowObj.getCell(1));
				String sLeadsMailId = getCellValueAsString(activityTemplateSheetRowObj.getCell(2));

				if(!"".equals(sSubsystem)) {
					leadsMailIdBysubsystem.put(sSubsystem.toUpperCase(), sSubsystemKeyword + "-" + sLeadsMailId);
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}//End of Method - loadActivitiesDetails()

	public void copyAsOneImportIterationTemplateExcel() {
		String parentPath = getWebINFRootDir() + File.separator;
		String asOneImportIterationTemplateExcel = parentPath + AsOneTaskCreatorConstants.ASONE_IMPORT_ITERATION_TEMPLATE_FILE_PATH;
		System.out.println("\n AsOne Import Iteration Template Excel Path: " + asOneImportIterationTemplateExcel);
		Workbook wbAsOneImportIterationTemplate;

		try {
			File fAsOneImportIterationTemplateExcel = new File(asOneImportIterationTemplateExcel);
			String estimationFileExtension = getFileExtension(fAsOneImportIterationTemplateExcel);

			System.out.println("\n AsOne Import Iteration Template Excel File Version is >= 2007 : " + (!estimationFileExtension.contains("vnd.ms-excel")));
			
			if (estimationFileExtension.contains("vnd.ms-excel")) {
				wbAsOneImportIterationTemplate = new HSSFWorkbook(new FileInputStream(fAsOneImportIterationTemplateExcel));
			} else {
				wbAsOneImportIterationTemplate = new XSSFWorkbook(new FileInputStream(fAsOneImportIterationTemplateExcel));
			}

			for (int i = wbAsOneImportIterationTemplate.getNumberOfSheets() - 1; i >= 0; i--) {
				if (wbAsOneImportIterationTemplate.getSheetName(i).contentEquals("Sample Iteration Sheet")) {
					wbAsOneImportIterationTemplate.removeSheetAt(i);
				}
			}
			

			FileOutputStream out = new FileOutputStream(new File(getDestinationFile()));
			wbAsOneImportIterationTemplate.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//End of Method - copyAsOneImportIterationTemplateExcel()

	public void loadEstimationSheetVOs(String aEstimationExcelFilePath) {
		System.out.println("EstimationExcelFilePath:::::::;"+aEstimationExcelFilePath);
		File fEstimationExcelSheet = new File(aEstimationExcelFilePath);
		String estimationFileExtension = getFileExtension(fEstimationExcelSheet);
		boolean isExcel2007 = false;

		try {
			String sSubsystem = fEstimationExcelSheet.getName().substring(8, fEstimationExcelSheet.getName().indexOf("-Estimation"));
			System.out.println("Estimation Excel File Name::"+fEstimationExcelSheet.getName());
			System.out.println("Estimation Excel File Extension :::::::: " + estimationFileExtension);

			Workbook wbEstimationExcelSheet = null;
			if (estimationFileExtension.contains("vnd.ms-excel")) {
				wbEstimationExcelSheet = new HSSFWorkbook(new FileInputStream(fEstimationExcelSheet));
			} else {
				wbEstimationExcelSheet = new XSSFWorkbook(new FileInputStream(fEstimationExcelSheet));
				isExcel2007 = true;
			}

			Sheet estimationSheetObj = wbEstimationExcelSheet.getSheet("Estimation");
			Row estimationSheetRowObj;

			System.out.println("isExcel2007::::::::;" + isExcel2007);
			System.out.println("===============================================================================================================");
			System.out.println("Subsystem::"+sSubsystem);
			for (int rowIndex = 4; rowIndex < estimationSheetObj.getPhysicalNumberOfRows(); rowIndex++) {
				estimationSheetRowObj = estimationSheetObj.getRow(rowIndex);

				String sCqNumber = getCellValueAsString(estimationSheetRowObj.getCell(AsOneTaskCreatorConstants.CQ_COL_NO_IN_ESTIMATION_SHEET));
				String sComplexity = getCellValueAsString(estimationSheetRowObj.getCell(AsOneTaskCreatorConstants.COMPLEXITY_COL_NO_IN_ESTIMATION_SHEET));

				if (!("".equals(sCqNumber) || "1.0".equals(sCqNumber))) {
					EstimationSheetVO estimationSheetVO = new EstimationSheetVO();
					estimationSheetVO.setCqNumber(sCqNumber);

					if (!"".equals(sComplexity)) {
						estimationSheetVO.setSubsystem(sSubsystem);
						estimationSheetVO.setCqDescription(getCellValueAsString(estimationSheetRowObj.getCell(AsOneTaskCreatorConstants.CQ_DESC_COL_NO_IN_ESTIMATION_SHEET)));
						estimationSheetVO.setTypeIR(getCellValueAsString(estimationSheetRowObj.getCell(AsOneTaskCreatorConstants.IR_TYPE_COL_NO_IN_ESTIMATION_SHEET)));

						estimationSheetVO.setEstimatedHours(estimatedHrsByComplexity.get(sComplexity.toUpperCase()));
					} else {
						System.out.println("\n CQ Number : " + sCqNumber + " has been skipped because of Complexity Column not filled.");
					}

					estimationSheetVOs.add(estimationSheetVO);
				}
			}
			System.out.println("===============================================================================================================");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//End of Method - loadEstimationSheetVOs()

	public void generateAsOneStoryCreatorExcel() {
		System.out.println("\nStory# Creation Format:::::" + STORY_NUMBER_CREATION_FORMAT);
		System.out.println("\n");

		Workbook wbAsOneImportIterationExcelFile = null;
		Sheet sheetAsOneImportIterationExcelFile = null;
		File fAsoneImportIterationDestFile = new File(getDestinationFile());

		try {
			String asoneImportIterationDestFileExtension = getFileExtension(fAsoneImportIterationDestFile);

			if (asoneImportIterationDestFileExtension.contains("vnd.ms-excel")) {
				wbAsOneImportIterationExcelFile = new HSSFWorkbook(new FileInputStream(fAsoneImportIterationDestFile));
			} else {
				wbAsOneImportIterationExcelFile = new XSSFWorkbook(new FileInputStream(fAsoneImportIterationDestFile));
			}

			sheetAsOneImportIterationExcelFile = wbAsOneImportIterationExcelFile.getSheet("As-One Iteration Sheet");

			int rowIndex = 1;
			if (sheetAsOneImportIterationExcelFile != null) {
				Row row = sheetAsOneImportIterationExcelFile.getRow(rowIndex++);
				Cell cell = row.getCell(1);
				cell.setCellValue(START_DATE);

				row = sheetAsOneImportIterationExcelFile.getRow(rowIndex++);
				cell = row.getCell(1);
				cell.setCellValue(END_DATE);

				for (EstimationSheetVO estimationSheetVO : estimationSheetVOs) {
					String sSubsystem = estimationSheetVO.getSubsystem();
					String sCqNumber = estimationSheetVO.getCqNumber();
					float estimatedHours = estimationSheetVO.getEstimatedHours();

					if(sSubsystem != null && !"".equals(sSubsystem.trim())) {
						for (Map.Entry<String, TaskActivitiesVO> hsmActivities : mapActivities.entrySet()) {
							TaskActivitiesVO taskActivitiesVO = hsmActivities.getValue();

							if(taskActivitiesVO.isCreateFlag()) {
								String team = taskActivitiesVO.getTeam();
								String taskDescription = taskActivitiesVO.getTaskDescription();
								String subsystemKeywordWithMailId = leadsMailIdBysubsystem.get(sSubsystem.toUpperCase());

								row = sheetAsOneImportIterationExcelFile.createRow(rowIndex);

								String storyNumber = STORY_NUMBER_CREATION_FORMAT.replace("CQNUM", sCqNumber);
								storyNumber = storyNumber.replace("SUBSYS", subsystemKeywordWithMailId.substring(0, subsystemKeywordWithMailId.indexOf("-")));
								storyNumber = storyNumber.replace("IRTYPE", configProperties.getProperty(estimationSheetVO.getTypeIR().toUpperCase()));
								storyNumber = storyNumber.replace("ACTKEYWRD", taskActivitiesVO.getTaskKeyWord());
								storyNumber = storyNumber.replaceAll("\\.", "_");
								/*
								 * Story Number
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.ITERATION_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(ITERATION_NO);

								/*
								 * Story Title
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.STORY_NUM_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(storyNumber);

								/*
								 * Story Title
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.STORY_TITLE_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(taskDescription);

								/*
								 * Story Description
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.STORY_DESC_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(estimationSheetVO.getCqDescription() + " - (" + taskDescription + ")");
		
								/*
								 * Estimated Hours
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.EST_HRS_COL_IN_ITER_IMPORT_SHEET);
								double estimatedHrsForTask = taskActivitiesVO.getTimePercent();

								if(taskActivitiesVO.getTimePercent() == 0) {
									estimatedHrsForTask = estimatedHours;
								} else {
									estimatedHrsForTask = (estimatedHrsForTask / 100) * estimatedHours;
								}

								cell.setCellValue(Math.ceil(estimatedHrsForTask));

								/*
								 * Leads Mail ID
								 */
								if(team.toLowerCase().equals("test")) {
									subsystemKeywordWithMailId = leadsMailIdBysubsystem.get("TESTING");
								} else if(team.toLowerCase().equals("ba")) {
									subsystemKeywordWithMailId = leadsMailIdBysubsystem.get("BA");
								}

								cell = row.createCell(AsOneTaskCreatorConstants.ASSIGNED_TO_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(subsystemKeywordWithMailId.substring(subsystemKeywordWithMailId.indexOf("-") + 1));

								/*
								 * Planned Start Date
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.PLANNED_START_DATE_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(START_DATE);
								
								/*
								 * Planned End Date
								 */
								cell = row.createCell(AsOneTaskCreatorConstants.PLANNED_END_DATE_COL_IN_ITER_IMPORT_SHEET);
								cell.setCellValue(END_DATE);
								
								
								rowIndex = rowIndex + 1;
							}//End of Condition - taskActivitiesVO.isCreateFlag()
						}//End Of FOR LOOP - (Map.Entry<String, TaskActivitiesVO> hsmActivities : mapActivities.entrySet()) 
					}//End of Condition - (sSubsystem != null && !"".equals(sSubsystem.trim())) 
				}
			}

			FileOutputStream outputStream = new FileOutputStream(fAsoneImportIterationDestFile);
			wbAsOneImportIterationExcelFile.write(outputStream);

			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}//End of Method - generateAsOneStoryCreatorExcel()

	/**
	 * @return the webINFRootDir
	 */
	public String getWebINFRootDir() {
		return webINFRootDir;
	}

	/**
	 * @param webINFRootDir the webINFRootDir to set
	 */
	public void setWebINFRootDir(String webINFRootDir) {
		this.webINFRootDir = webINFRootDir;
	}
}
