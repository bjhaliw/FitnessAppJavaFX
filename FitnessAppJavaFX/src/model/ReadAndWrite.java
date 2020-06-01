package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * This class is responsible for the creation, read, and writing of files in
 * order to save the WorkoutTracker and ExerciseList objects. The user will be
 * able to select a directory to save the files, and the files will be
 * automatically generated in the selected directory. The user will be able to
 * view text documents and Excel documents to see the stored information.
 * 
 * @author Brenton Haliw
 *
 */
public class ReadAndWrite extends Application {

	static String directoryPath;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select or create a folder");
		primaryStage.setAlwaysOnTop(true);

		File directory = chooser.showDialog(primaryStage);

		if (directory == null) {
			System.out.println("User backed out without selecting a directory");
			return;
		}

		File filePath = new File(directory.getAbsolutePath() + "/" + "WorkoutFiles");
		directoryPath = filePath.getAbsolutePath();
		if (!filePath.exists()) {
			try {
				Files.createDirectories(Paths.get(directoryPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Platform.exit();

	}

	/**
	 * Helper method that merges unused rows and cells and colors them with the supplied CellStyle
	 * @param workbook - The current XSSFWorkbook that is being used
	 * @param style - The CellStyle to be applied
	 * @param numWorkoutColumns - The amount of columns 
	 */
	private static void fillInExtraRows(XSSFWorkbook workbook, CellStyle style, int numWorkoutColumns) {
		Row row;
		Cell cell;
		XSSFSheet sheet;
		boolean continueLoop = true;
		int firstCol = 0, lastCol = 3, currentRow = 1;
		int rowNumStart = 0;
		int counter = 0;

		// Will loop through the sheets contained within the workbook
		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {

			sheet = workbook.getSheetAt(sheetNum);

			// Looking for open rows
			for (; currentRow < sheet.getLastRowNum(); currentRow++) {
				row = sheet.getRow(currentRow);

				// This should be the weight column since it won't be merged
				cell = row.getCell(lastCol);
				if (cell == null && rowNumStart == 0) { // First identifed empty row
					loadCellData(sheet, row, cell, style, firstCol, "", 0);
					rowNumStart = currentRow;
				} else if (cell != null && rowNumStart != 0) { // Line between Workouts needs to be merged and filled
					sheet.addMergedRegion(new CellRangeAddress(rowNumStart, currentRow - 1, firstCol, lastCol));
					rowNumStart = 0;
				} else if (currentRow + 1 == sheet.getLastRowNum()) { // Empty space after last workout
					// Already at the end of the row list
					if (rowNumStart == 0) {
						rowNumStart = sheet.getLastRowNum();
						row = sheet.getRow(sheet.getLastRowNum());
					}

					loadCellData(sheet, row, cell, style, firstCol, "", 0);
					sheet.addMergedRegion(new CellRangeAddress(rowNumStart, sheet.getLastRowNum(), firstCol, lastCol));
					counter++;

					// Move onto the next workout column by shifting the column #s and restarting at row 0
					if (counter < numWorkoutColumns) {
						firstCol += 5;
						lastCol += 5;
						currentRow = 1;
						rowNumStart = 0;
					}
				}
			}

			lastCol = 3; // Reseting value

			// Adds the color to the vertical columns between each workout column
			while (continueLoop) {
				row = sheet.getRow(0);
				cell = row.getCell(lastCol + 1);
				if (cell == null) {
					continueLoop = false;
				} else {
					sheet.addMergedRegion(new CellRangeAddress(0, sheet.getLastRowNum(), lastCol + 1, lastCol + 1));
					lastCol += 5;
				}
			}
		}
	}

	/**
	 * Reads the WorkkoutTracker object and creates an Excel workbook to display the
	 * information. Information is color coordinated and workouts are separated in
	 * the Excel workbook.
	 * 
	 * @param tracker
	 * @param workbook
	 */
	public static void saveWorkoutExcel(WorkoutTracker tracker, XSSFWorkbook workbook) {
		int dateCol = 0, exerciseCol = 1, repCol = 2, weightCol = 3; // Will modify after in loop to other columns
		int rowNum = 0, exerciseNum = 1, workoutStartRowNum = 0, workoutEndRowNum = 0;
		int exerciseStartRowNum = 0, exerciseEndRowNum = 0;
		int counter = 1, numColumnsTotal = 1;
		String workoutDate = "";
		XSSFSheet sheet;
		Row row = null;
		Cell cell = null;

		sheet = workbook.createSheet();

		///// CREATING THE CELLSTYLES (COLORS, FONT) FOR THE EXCEL SHEET /////
		CellStyle oddExerciseNum = createCellStyle(sheet, IndexedColors.AQUA.getIndex(), false);
		CellStyle evenExerciseNum = createCellStyle(sheet, IndexedColors.CORAL.getIndex(), false);
		CellStyle dateCellStyle = createCellStyle(sheet, IndexedColors.TAN.getIndex(), false);
		CellStyle headerCellStyle = createCellStyle(sheet, IndexedColors.GREY_25_PERCENT.getIndex(), true);

		// Row 0 - Header Row
		row = sheet.createRow(0);
		createHeaderRow(sheet, row, cell, dateCol, exerciseCol, repCol, weightCol, headerCellStyle);
		rowNum++;

		for (Workout workout : tracker.getWorkoutList()) {

			// Creates a new column of workouts starting at row 0 again and reinitializes the variables
			if (counter % 6 == 0) {
				// Autosizes the previous columns
				sheet.autoSizeColumn(dateCol, true);
				sheet.autoSizeColumn(exerciseCol, true);
				sheet.autoSizeColumn(repCol, true);
				sheet.autoSizeColumn(weightCol, true);
				dateCol += 5;
				exerciseCol += 5;
				repCol += 5;
				weightCol += 5;
				rowNum = 1;
				workoutStartRowNum = 0;
				workoutEndRowNum = 0;
				exerciseNum = 1;
				cell = null;
				row = null;
				row = sheet.getRow(0);
				numColumnsTotal++;
				counter = 1;
				createHeaderRow(sheet, row, cell, dateCol, exerciseCol, repCol, weightCol, headerCellStyle);
			}

			///// DATE CELL /////
			workoutStartRowNum = rowNum; // Remembering what row we started on to merge later
			workoutDate = workout.getStartTime().toString().substring(0, 10); // YYYY-MM-DD

			if (sheet.getRow(rowNum) == null) {
				row = sheet.createRow(rowNum++); // Creating the new Exercise Row
			} else {
				row = sheet.getRow(rowNum++);
			}

			loadCellData(sheet, row, cell, dateCellStyle, dateCol, workoutDate, 0); // Loading data for date

			for (Exercise exercise : workout.getExerciseArrayList()) {

				///// EXERCISE NAME CELL /////
				exerciseStartRowNum = rowNum - 1;
				exerciseEndRowNum = rowNum - 1;
				if (exerciseNum % 2 != 0) {
					loadCellData(sheet, row, cell, oddExerciseNum, exerciseCol, exercise.getExerciseName(), 0);
				} else {
					loadCellData(sheet, row, cell, evenExerciseNum, exerciseCol, exercise.getExerciseName(), 0);
				}

				for (Set set : exercise.getSetList()) {
					///// REP AND WEIGHT CELLS /////
					if (exerciseNum % 2 != 0) {
						loadCellData(sheet, row, cell, oddExerciseNum, repCol, null, set.getReps());
						loadCellData(sheet, row, cell, oddExerciseNum, weightCol, null, set.getWeight());
					} else {
						loadCellData(sheet, row, cell, evenExerciseNum, repCol, null, set.getReps());
						loadCellData(sheet, row, cell, evenExerciseNum, weightCol, null, set.getWeight());
					}

					if (sheet.getRow(rowNum) == null) {
						row = sheet.createRow(rowNum++); // Creating the new Exercise Row
					} else {
						row = sheet.getRow(rowNum++);
					}

					exerciseEndRowNum++;
				}

				// Adding a border to the bottom of the exercise row to help distinguish between
				// different ones
				RegionUtil.setBorderBottom(BorderStyle.THIN,
						new CellRangeAddress(exerciseEndRowNum - 1, exerciseEndRowNum - 1, exerciseCol, weightCol),
						sheet);

				// Merging the exercise name column cells together
				if (exerciseStartRowNum != exerciseEndRowNum - 1) {
					sheet.addMergedRegion(
							new CellRangeAddress(exerciseStartRowNum, exerciseEndRowNum - 1, exerciseCol, exerciseCol));
				}
				exerciseNum++;
			}

			workoutEndRowNum = rowNum - 2;

			// Merging the date column cells together
			sheet.addMergedRegion(new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol));

			// Adding the borders to and around the workout
			RegionUtil.setBorderBottom(BorderStyle.THICK,
					new CellRangeAddress(workoutEndRowNum, workoutEndRowNum, dateCol, weightCol), sheet);
			RegionUtil.setBorderTop(BorderStyle.THICK,
					new CellRangeAddress(workoutStartRowNum, workoutStartRowNum, dateCol, weightCol), sheet);
			RegionUtil.setBorderLeft(BorderStyle.THICK,
					new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol), sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN,
					new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol), sheet);
			RegionUtil.setBorderRight(BorderStyle.THICK,
					new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, weightCol, weightCol), sheet);

			exerciseNum = 1;
			counter++; // Workout counter
		}

		// Autosizing the last columns
		sheet.autoSizeColumn(dateCol, true);
		sheet.autoSizeColumn(exerciseCol, true);
		sheet.autoSizeColumn(repCol, true);
		sheet.autoSizeColumn(weightCol, true);

		fillInExtraRows(workbook, headerCellStyle, numColumnsTotal);
	}
	
	public static boolean saveAllInformation(WorkoutTracker tracker, ExerciseList list, String path) throws IOException {
		boolean value = false;
		
		if (path != null) {
			FileOutputStream fos = new FileOutputStream(path + "/WorkoutTracker.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			ReadAndWrite.saveWorkoutExcel(tracker, workbook);
			workbook.write(fos);
			workbook.close();

			tracker.saveWorkoutList(path + "/WorkoutList.txt");
			list.saveExerciseList(path + "/ExerciseList.txt");
			Statistics.saveGraphExcel(tracker, path);

			System.out.println("Successfully saved all information to " + path);
			
			workbook.close();
			fos.close();
			
			value = true;
		}
		
		return value;
	}

	public static void saveAllInformation(WorkoutTracker tracker, ExerciseList list) throws IOException {
		Application.launch(ReadAndWrite.class);

		if (ReadAndWrite.directoryPath != null) {
			FileOutputStream fos = new FileOutputStream(ReadAndWrite.directoryPath + "/WorkoutTracker.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			ReadAndWrite.saveWorkoutExcel(tracker, workbook);
			workbook.write(fos);
			workbook.close();

			tracker.saveWorkoutList(ReadAndWrite.directoryPath + "/WorkoutList.txt");
			list.saveExerciseList(ReadAndWrite.directoryPath + "/ExerciseList.txt");
			Statistics.saveGraphExcel(tracker);

			System.out.println("Successfully saved all information to " + ReadAndWrite.directoryPath);
			
			workbook.close();
			fos.close();
		}
	}

	/**
	 * Automatically fills in Row 0 with the required information.
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param dateCol
	 * @param exerciseCol
	 * @param repCol
	 * @param weightCol
	 * @param headerCellStyle
	 */
	private static void createHeaderRow(XSSFSheet sheet, Row row, Cell cell, int dateCol, int exerciseCol, int repCol,
			int weightCol, CellStyle headerCellStyle) {
		cell = row.createCell(dateCol);
		cell.setCellValue("Date");
		cell.setCellStyle(headerCellStyle);
		cell = row.createCell(exerciseCol);
		cell.setCellValue("Exercise Name");
		cell.setCellStyle(headerCellStyle);
		cell = row.createCell(repCol);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Reps");
		cell = row.createCell(weightCol);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Weight");
		cell = row.createCell(weightCol + 1);
		cell.setCellValue("");
		cell.setCellStyle(headerCellStyle);
	}

	/**
	 * Creates and returns a CellStyle based on the parameters such as color and if they want font or not
	 * @param sheet - The sheet for the current XSSFWorkbook
	 * @param indexColor - The Color the cell should be
	 * @param font - True if needed to change the font to bold
	 * @return The created CellStyle object
	 */
	private static CellStyle createCellStyle(XSSFSheet sheet, short indexColor, boolean font) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font cellStyleFont;

		if (font) {
			cellStyleFont = sheet.getWorkbook().createFont();
			cellStyleFont.setBold(true);
			cellStyle.setFont(cellStyleFont);
		}

		cellStyle.setFillForegroundColor(indexColor);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return cellStyle;
	}

	/**
	 * Creates and loads the required value and CellStyle object into the indicated row's cell
	 * @param sheet - The sheet for the current XSSFWorkbook
	 * @param row - The current row to be modified
	 * @param cell - The current cell within the row to be modified
	 * @param cellStyle - The CellStyle that the cell should have
	 * @param colNum - The number of the cell within the row to be created
	 * @param stringValue - If null, will load the double value instead.
	 * @param doubleValue - Numerical value if required
	 */
	private static void loadCellData(XSSFSheet sheet, Row row, Cell cell, CellStyle cellStyle, int colNum,
			String stringValue, double doubleValue) {
		cell = row.createCell(colNum);

		if (stringValue != null) {
			cell.setCellValue(stringValue);
		} else {
			cell.setCellValue(doubleValue);
		}

		cell.setCellStyle(cellStyle);
	}
}
