package com.cnsi.asonetaskcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsOneTaskCreator {
	private List<File> estimationFiles;

	public void processAsOneTaskCreator(String aWebINFRootDir, String aIterationNo, String aIterationStartDate,String aIterationEndDate, String aCQEstimationFile) {
		String destinationExcelImportIterationFile = aCQEstimationFile + File.separator + "Output";
		File destFile = new File(destinationExcelImportIterationFile);
		if(destFile.exists() == false) {
			destFile.mkdirs();
		}

		AsOneTaskImportExcelGenerator asOneTaskCreator = new AsOneTaskImportExcelGenerator(aWebINFRootDir, aIterationNo, aIterationStartDate,aIterationEndDate);//ERROR-1
		asOneTaskCreator.loadActivitiesDetails();
		
		for (File path : estimationFiles) {
			asOneTaskCreator.loadEstimationSheetVOs(path.getAbsolutePath());
		}
		
		for (File path : estimationFiles) {
			String sDestinationFile = path.getName().substring(0, path.getName().indexOf("."));
			sDestinationFile = destinationExcelImportIterationFile + File.separator + sDestinationFile.replaceAll("Estimation Sheet", "IterationSheet") + ".xls";

			asOneTaskCreator.setDestinationFile(sDestinationFile);
			
			asOneTaskCreator.copyAsOneImportIterationTemplateExcel();
			asOneTaskCreator.generateAsOneStoryCreatorExcel();
		}
	}

	public void loadEstimationSheetVOs(String aCQEstimationFile) {
		System.out.println("CQEstimationFile::::::::::::"+aCQEstimationFile);
		estimationFiles = new ArrayList<File>();
		File fEstimationExcelSheet = new File(aCQEstimationFile);

		try {
			if (fEstimationExcelSheet.exists()) {
				loadEstimationExcelFiles(fEstimationExcelSheet);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("No of Estimation Files:::::::::::::::::"+estimationFiles.size());
	}// End of Method - loadEstimationSheetVOs()

	private void loadEstimationExcelFiles(File aFilePath) {
		try {
			if (aFilePath.exists()) {
				File[] listEstimationFiles = aFilePath.listFiles();

				for (File path : listEstimationFiles) {

					if (path.isDirectory()) {
						loadEstimationExcelFiles(path);
					} else {
						if (path.getName().toLowerCase().contains("estimation sheet")) {
							estimationFiles.add(path);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// End of Method - loadEstimationExcelFiles()

}
