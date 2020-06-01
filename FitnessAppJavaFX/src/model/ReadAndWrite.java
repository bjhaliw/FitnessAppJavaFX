package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
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
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.application.Application;
import javafx.application.Platform;
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

	public static void fillInExtraRows(XSSFWorkbook workbook, CellStyle style, int sheetWidth) {
		Row row;
		Cell cell;
		XSSFSheet sheet;
		boolean continueLoop = true;
		int firstCol = 0, lastCol = 3, currentRow = 1;
		int rowNumStart = 0;
		int counter = 0;
		System.out.println(sheetWidth);

		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {

			sheet = workbook.getSheetAt(sheetNum);

			// Looking for open rows
			for (; currentRow < sheet.getLastRowNum(); currentRow++) {
				row = sheet.getRow(currentRow);

				cell = row.getCell(lastCol);
				if (cell == null && rowNumStart == 0) { // First identify empty row
					loadCellData(sheet, row, cell, style, firstCol, "", 0);
					rowNumStart = currentRow;
				} else if (cell != null && rowNumStart != 0) { // Line Between Workouts gets merged and filled
					System.out.println("Merging Row Num : " + rowNumStart + " and Last row: " + (currentRow - 1));
					sheet.addMergedRegion(new CellRangeAddress(rowNumStart, currentRow - 1, firstCol, lastCol));
					rowNumStart = 0;
				} else if (currentRow + 1 == sheet.getLastRowNum()) { // Empty space after last workout
					// Already at the end
					if (rowNumStart == 0) {
						rowNumStart = sheet.getLastRowNum();
						row = sheet.getRow(sheet.getLastRowNum());
					}

					System.out.println("Merging Row Num : " + rowNumStart + " and Last row: " + sheet.getLastRowNum());
					loadCellData(sheet, row, cell, style, firstCol, "", 0);
					sheet.addMergedRegion(new CellRangeAddress(rowNumStart, sheet.getLastRowNum(), firstCol, lastCol));
					counter++;

					if (counter < sheetWidth) {
						firstCol += 5;
						lastCol += 5;
						currentRow = 1;
						rowNumStart = 0;
					}
				}
			}

			lastCol = 3; // Reseting value

			while (continueLoop) {
				row = sheet.getRow(0);
				cell = row.getCell(lastCol + 1);
				if (cell == null) {
					System.out.println("Breaking while loop");
					continueLoop = false;
				} else {
					sheet.addMergedRegion(new CellRangeAddress(0, sheet.getLastRowNum(), lastCol + 1, lastCol + 1));
					lastCol += 5;
				}
			}
		}
	}

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

			if (counter % 6 == 0) {
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

	/*	*//**
			 * Reads the WorkkoutTracker object and creates an Excel workbook to display the
			 * information. Information is color coordinated and workouts are separated in
			 * the Excel workbook.
			 * 
			 * @param tracker
			 * @param workbook
			 *//*
				 * public static void saveWorkoutExcel(WorkoutTracker tracker, XSSFWorkbook
				 * workbook) { int dateCol = 0, exerciseCol = 1, repCol = 2, weightCol = 3; //
				 * Will modify after in loop to other columns int rowNum = 0, exerciseNum = 1,
				 * workoutStartRowNum = 0, workoutEndRowNum = 0; int exerciseStartRowNum = 0,
				 * exerciseEndRowNum = 0; int counter = 1; String workoutDate = ""; XSSFSheet
				 * sheet; Row row = null; Cell cell = null;
				 * 
				 * sheet = workbook.createSheet();
				 * 
				 * ///// CREATING THE CELLSTYLES (COLORS, FONT) FOR THE EXCEL SHEET /////
				 * CellStyle oddExerciseNum = createCellStyle(sheet,
				 * IndexedColors.AQUA.getIndex(), false); CellStyle evenExerciseNum =
				 * createCellStyle(sheet, IndexedColors.CORAL.getIndex(), false); CellStyle
				 * dateCellStyle = createCellStyle(sheet, IndexedColors.TAN.getIndex(), false);
				 * CellStyle headerCellStyle = createCellStyle(sheet,
				 * IndexedColors.GREY_25_PERCENT.getIndex(), true);
				 * 
				 * // Row 0 - Header Row row = sheet.createRow(0); createHeaderRow(sheet, row,
				 * cell, dateCol, exerciseCol, repCol, weightCol, headerCellStyle); rowNum++;
				 * 
				 * for (Workout workout : tracker.getWorkoutList()) {
				 * 
				 * if (counter % 6 == 0) { sheet.autoSizeColumn(dateCol, true);
				 * sheet.autoSizeColumn(exerciseCol, true); sheet.autoSizeColumn(repCol, true);
				 * sheet.autoSizeColumn(weightCol, true); dateCol += 5; exerciseCol += 5; repCol
				 * += 5; weightCol += 5; rowNum = 1; workoutStartRowNum = 0; workoutEndRowNum =
				 * 0; exerciseNum = 1; cell = null; row = null; row = sheet.getRow(0);
				 * createHeaderRow(sheet, row, cell, dateCol, exerciseCol, repCol, weightCol,
				 * headerCellStyle); }
				 * 
				 * ///// DATE CELL ///// workoutStartRowNum = rowNum; // Remembering what row we
				 * started on to merge later workoutDate =
				 * workout.getStartTime().toString().substring(0, 10); // YYYY-MM-DD
				 * 
				 * if (sheet.getRow(rowNum) == null) { row = sheet.createRow(rowNum++); //
				 * Creating the new Exercise Row } else { row = sheet.getRow(rowNum++); }
				 * 
				 * loadCellData(sheet, row, cell, dateCellStyle, dateCol, workoutDate, 0); //
				 * Loading data for date
				 * 
				 * for (Exercise exercise : workout.getExerciseArrayList()) {
				 * 
				 * ///// EXERCISE NAME CELL ///// exerciseStartRowNum = rowNum - 1;
				 * exerciseEndRowNum = rowNum - 1; if (exerciseNum % 2 != 0) {
				 * loadCellData(sheet, row, cell, oddExerciseNum, exerciseCol,
				 * exercise.getExerciseName(), 0); } else { loadCellData(sheet, row, cell,
				 * evenExerciseNum, exerciseCol, exercise.getExerciseName(), 0); }
				 * 
				 * for (Set set : exercise.getSetList()) { ///// REP AND WEIGHT CELLS ///// if
				 * (exerciseNum % 2 != 0) { loadCellData(sheet, row, cell, oddExerciseNum,
				 * repCol, null, set.getReps()); loadCellData(sheet, row, cell, oddExerciseNum,
				 * weightCol, null, set.getWeight()); } else { loadCellData(sheet, row, cell,
				 * evenExerciseNum, repCol, null, set.getReps()); loadCellData(sheet, row, cell,
				 * evenExerciseNum, weightCol, null, set.getWeight()); }
				 * 
				 * if (sheet.getRow(rowNum) == null) { row = sheet.createRow(rowNum++); //
				 * Creating the new Exercise Row } else { row = sheet.getRow(rowNum++); }
				 * 
				 * exerciseEndRowNum++; }
				 * 
				 * // Adding a border to the bottom of the exercise row to help distinguish
				 * between // different ones RegionUtil.setBorderBottom(BorderStyle.THIN, new
				 * CellRangeAddress(exerciseEndRowNum - 1, exerciseEndRowNum - 1, exerciseCol,
				 * weightCol), sheet);
				 * 
				 * // Merging the exercise name column cells together if (exerciseStartRowNum !=
				 * exerciseEndRowNum - 1) { sheet.addMergedRegion( new
				 * CellRangeAddress(exerciseStartRowNum, exerciseEndRowNum - 1, exerciseCol,
				 * exerciseCol)); } exerciseNum++; }
				 * 
				 * workoutEndRowNum = rowNum - 2;
				 * 
				 * // Merging the date column cells together sheet.addMergedRegion(new
				 * CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol));
				 * 
				 * // Merging the gap in between workouts row = sheet.getRow(rowNum - 1); cell =
				 * row.createCell(dateCol); cell.setCellStyle(headerCellStyle);
				 * sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, dateCol,
				 * weightCol));
				 * 
				 * // Merging the cell to the right of the workout row =
				 * sheet.getRow(workoutStartRowNum); cell = row.createCell(weightCol + 1);
				 * cell.setCellStyle(headerCellStyle); sheet.addMergedRegion( new
				 * CellRangeAddress(workoutStartRowNum, rowNum - 1, weightCol + 1, weightCol +
				 * 1));
				 * 
				 * // Adding the borders to and around the workout
				 * RegionUtil.setBorderBottom(BorderStyle.THICK, new
				 * CellRangeAddress(workoutEndRowNum, workoutEndRowNum, dateCol, weightCol),
				 * sheet); RegionUtil.setBorderTop(BorderStyle.THICK, new
				 * CellRangeAddress(workoutStartRowNum, workoutStartRowNum, dateCol, weightCol),
				 * sheet); RegionUtil.setBorderLeft(BorderStyle.THICK, new
				 * CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol),
				 * sheet); RegionUtil.setBorderRight(BorderStyle.THIN, new
				 * CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol),
				 * sheet); RegionUtil.setBorderRight(BorderStyle.THICK, new
				 * CellRangeAddress(workoutStartRowNum, workoutEndRowNum, weightCol, weightCol),
				 * sheet);
				 * 
				 * exerciseNum = 1; counter++; // Workout counter }
				 * 
				 * // Autosizing the last columns sheet.autoSizeColumn(dateCol, true);
				 * sheet.autoSizeColumn(exerciseCol, true); sheet.autoSizeColumn(repCol, true);
				 * sheet.autoSizeColumn(weightCol, true);
				 * 
				 * // Looking to see if there were any open cells not colored in int firstCol =
				 * 0; int lastCol = 4; int i = 1; rowNum = 0;
				 * 
				 * // Looking for open spaces for(; i < sheet.getLastRowNum(); i++) { row =
				 * sheet.getRow(i);
				 * 
				 * if (row == null) { break; }
				 * 
				 * cell = row.getCell(firstCol); if(cell == null && rowNum == 0) {
				 * loadCellData(sheet, row, cell, headerCellStyle, firstCol, "", 0); rowNum = i;
				 * } }
				 * 
				 * if (rowNum != i) { sheet.addMergedRegion(new CellRangeAddress(rowNum,i,
				 * firstCol, lastCol)); }
				 * 
				 * 
				 * }
				 */

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
		}
	}

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

	/**
	 * This method is responsible for creating and storing the user's highest amount
	 * of weight lifted, highest amount of reps for an exercise, one rep max
	 * estimation, and max amount of volume for each day. Writes to an Excel file
	 * and saves it on the desktop of the user's device.
	 * 
	 * Columns: Dates(0), Weight(1), Reps(2), Volume (3), One Rep Max(4)
	 * 
	 * @param tracker
	 * @throws IOException
	 */
	public static void saveGraphExcel(WorkoutTracker tracker, Scanner scanner) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();

		////// WRITING THE VALUES ///////
		writeMaxValues(tracker, workbook);

		////// CREATING THE GRAPHS ///////
		createGraphs(workbook);

		////// SAVING THE INFORMATION ///////
		final JFileChooser fc = new JFileChooser();
		JFrame jf = new JFrame();
		jf.setAlwaysOnTop(true);
		File excelFile = null;
		FileOutputStream fileOut = null;

		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle("Save File");
		fc.setCurrentDirectory(new File(System.getProperties().getProperty("user.home") + "\\Desktop"));

		// Allows us to only see directories and files ending with .xlsx
		fc.setFileFilter(new FileFilter() {
			public boolean accept(final File f) {
				return f.isDirectory() || f.getAbsolutePath().endsWith(".xlsx");
			}

			public String getDescription() {
				return "Excel files (*.xlsx)";
			}
		});

		// Prompts the user to select or type in a file
		int returnVal = fc.showSaveDialog(jf);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			excelFile = fc.getSelectedFile();

			// If the user wants to create a brand new file
			if (!excelFile.exists()) {
				fileOut = new FileOutputStream(excelFile + ".xlsx");

				// If the user selected a previous .xlsx file
			} else {
				int res = JOptionPane.showConfirmDialog(jf, "File already exists. Do you wish to overwrite?");

				if (res == JOptionPane.YES_OPTION) {
					fileOut = new FileOutputStream(excelFile);

				} else if (res == JOptionPane.NO_OPTION) {
					int returnVal2 = fc.showSaveDialog(jf);
					if (returnVal2 == JFileChooser.APPROVE_OPTION) {

						File file2 = fc.getSelectedFile();
						if (!file2.exists()) {
							fileOut = new FileOutputStream(file2 + ".xlsx");
						}
					}
				} else if (res == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(jf, "User cancelled operation.");
				}
			}

			if (fileOut != null) {
				workbook.write(fileOut);
				fileOut.close();
				System.out.println("\n Your Excel file has been generated!");
				JOptionPane.showMessageDialog(jf, "File Created.");
				workbook.close();
			}
		} else if (returnVal == JOptionPane.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(jf, "User cancelled operation.");
		}
	}

	/**
	 * Writes the max values for each workout onto their own sheets and records the
	 * max weight, max reps, max volume, and one rep max estimate into their
	 * specified columns. If this is the first time the exercise is being recorded,
	 * then a brand new sheet is automatically made and then filled in.
	 * 
	 * @param tracker
	 * @param workbook
	 */
	private static void writeMaxValues(WorkoutTracker tracker, XSSFWorkbook workbook) {
		int rowNum = 0;
		XSSFSheet sheet = null;
		Row row;
		Cell cell;

		for (Workout workout : tracker.getWorkoutList()) {
			for (Exercise exercise : workout.getExerciseArrayList()) {

				sheet = workbook.getSheet(exercise.getExerciseName());

				if (sheet == null) { // Sheet doesn't exist for the specific exercise
					sheet = workbook.createSheet(exercise.getExerciseName());
				}

				rowNum = sheet.getLastRowNum();

				if (rowNum == -1) { // A brand new sheet was created earlier
					////// CREATING OUR HEADER ROW ///////
					row = sheet.createRow(0);
					cell = row.createCell(0);
					cell.setCellValue("Date");
					cell = row.createCell(1);
					cell.setCellValue("Max Weight");
					cell = row.createCell(2);
					cell.setCellValue("Max Reps");
					cell = row.createCell(3);
					cell.setCellValue("Max Total Volume");
					cell = row.createCell(4);
					cell.setCellValue("One Rep Max");

					// Need to create a new row to be manipulated by code below
					row = sheet.createRow(1);
				} else { // Sheet already contained rows
					row = sheet.createRow(rowNum + 1);
				}

				// Date - Col 0
				cell = row.createCell(0);
				cell.setCellValue(workout.getStartTime().toString().substring(0, 10));

				// Weight - Col 1
				cell = row.createCell(1);
				cell.setCellValue(exercise.getMaxWeight());

				// Reps - Col 2
				cell = row.createCell(2);
				cell.setCellValue(exercise.getMaxReps());

				// Volume - Col 3
				cell = row.createCell(3);
				cell.setCellValue(exercise.getTotalVolume());

				// One Rep Max - Col 4
				cell = row.createCell(4);
				cell.setCellValue(exercise.getOneRepMax());
			}
		}

		/*
		 * After writing all of the information to the workbook, we go through each
		 * sheet and then autosize it to make the values readable later.
		 */
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
		}
	}

	/**
	 * This method is responsible for creating the graphs that will be displayed in
	 * the workbook passed in as a parameter. There will be three graphs overall.
	 * One will display BOTH One Rep Max and Max Weight, another will be max reps,
	 * and the last will be max volume. Each sheet is parsed and has graphs off to
	 * their right side.
	 * 
	 * @param workbook
	 */
	private static void createGraphs(XSSFWorkbook workbook) {
		XSSFSheet sheet = null;

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);

			///// READING THE ROWS AND COLUMNS TO GATHER OUR DATA TO BE GRAPHED /////
			XDDFDataSource<String> dates = XDDFDataSourcesFactory.fromStringCellRange(sheet,
					new CellRangeAddress(1, sheet.getLastRowNum(), 0, 0));

			XDDFNumericalDataSource<Double> weight = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
					new CellRangeAddress(1, sheet.getLastRowNum(), 1, 1));

			XDDFNumericalDataSource<Double> reps = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
					new CellRangeAddress(1, sheet.getLastRowNum(), 2, 2));

			XDDFNumericalDataSource<Double> volume = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
					new CellRangeAddress(1, sheet.getLastRowNum(), 3, 3));

			XDDFNumericalDataSource<Double> oneRepMax = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
					new CellRangeAddress(1, sheet.getLastRowNum(), 4, 4));

			///// CREATING MAX WEIGHT AND ONE REP MAX GRAPH /////
			XSSFDrawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 6, 0, 15, 25);

			XSSFChart chart = drawing.createChart(anchor);
			chart.setTitleText("Max Weight Each Day");
			chart.setTitleOverlay(false);

			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP_RIGHT);

			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle("Date");
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle("Max Weight (lbs)");

			XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

			XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(dates, weight);
			series1.setTitle("Weight", null);
			series1.setSmooth(true);
			series1.setMarkerSize((short) 6);
			series1.setMarkerStyle(MarkerStyle.STAR);

			XDDFLineChartData.Series series3 = (XDDFLineChartData.Series) data.addSeries(dates, oneRepMax);
			series3.setTitle("One Rep Max", null);
			series3.setSmooth(true);
			series3.setMarkerSize((short) 6);
			series3.setMarkerStyle(MarkerStyle.SQUARE);

			chart.plot(data);

			///// CREATING MAX REPS GRAPH /////
			anchor = drawing.createAnchor(0, 0, 0, 0, 15, 0, 24, 25);
			chart = drawing.createChart(anchor);
			chart.setTitleText("Max Reps Each Day");
			chart.setTitleOverlay(false);
			legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP_RIGHT);
			bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle("Date");
			leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle("Max Reps");
			data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

			XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) data.addSeries(dates, reps);
			series2.setTitle("Reps", null);
			series2.setSmooth(true);
			series2.setMarkerSize((short) 6);
			series2.setMarkerStyle(MarkerStyle.STAR);

			chart.plot(data);

			///// CREATING MAX VOLUME GRAPH /////
			anchor = drawing.createAnchor(0, 0, 0, 0, 10, 25, 20, 50);
			chart = drawing.createChart(anchor);
			chart.setTitleText("Max Volume Each Day");
			chart.setTitleOverlay(false);
			legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP_RIGHT);
			bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle("Date");
			leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle("Max Volume (lbs)");
			data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

			XDDFLineChartData.Series series4 = (XDDFLineChartData.Series) data.addSeries(dates, volume);
			series4.setTitle("Volume", null);
			series4.setSmooth(true);
			series4.setMarkerSize((short) 6);
			series4.setMarkerStyle(MarkerStyle.STAR);

			chart.plot(data);
		}
	}

}
