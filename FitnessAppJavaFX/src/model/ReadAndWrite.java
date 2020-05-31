package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
import org.apache.poi.ss.util.CellUtil;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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

	/*
	 * public static void saveWorkoutTrackerExcel(WorkoutTracker tracker, Scanner
	 * scanner) throws IOException {
	 * 
	 * Platform.runLater(new Runnable() { JFXPanel panel = new JFXPanel(); // Not
	 * sure why, but without these two the program doesn't run JFrame frame = new
	 * JFrame();
	 * 
	 * @Override public void run() { DirectoryChooser chooser = new
	 * DirectoryChooser(); chooser.setTitle("Select or create a folder"); Stage
	 * stage = new Stage();
	 * 
	 * File directory = chooser.showDialog(stage);
	 * 
	 * if (directory == null) {
	 * System.out.println("User backed out without selecting a directory"); return;
	 * }
	 * 
	 * File filePath = new File(directory.getAbsolutePath() + "/" + "WorkoutFiles");
	 * directoryPath = filePath.getAbsolutePath(); if (!filePath.exists()) { try {
	 * Files.createDirectories(Paths.get(directoryPath)); } catch (IOException e) {
	 * e.printStackTrace(); } } }
	 * 
	 * });
	 * 
	 * saveAux(tracker);
	 * 
	 * }
	 */
	private static void createHeaderRow(XSSFSheet sheet, Row row, Cell cell, int dateCol, int exerciseCol, int repCol, int weightCol, CellStyle headerCellStyle) {
		row = sheet.createRow(0);
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
	
	public static void saveWorkoutExcelBetter(WorkoutTracker tracker, XSSFWorkbook workbook) {
		int dateCol = 0, exerciseCol = 1, repCol = 2, weightCol = 3; // Will modify after in loop to other columns	
		int rowNum = 0, exerciseNum = 1, workoutStartRowNum = 0;
		int exerciseStartRowNum = 0;
		int counter = 0;
		String workoutDate = "";
		XSSFSheet sheet;
		Row row = null;
		Cell cell = null;
		
		sheet = workbook.createSheet();	
		
		// Creating the colors, alignment, and font if required for the cells
		CellStyle oddExerciseNum = createCellStyle(sheet, IndexedColors.AQUA.getIndex(), false);
		CellStyle evenExerciseNum = createCellStyle(sheet, IndexedColors.CORAL.getIndex(), false);
		CellStyle dateCellStyle  = createCellStyle(sheet, IndexedColors.TAN.getIndex(), false);
		CellStyle headerCellStyle = createCellStyle(sheet, IndexedColors.GREY_25_PERCENT.getIndex(), true);
		
		// Row 0 - Header Row
		createHeaderRow(sheet, row, cell, dateCol, exerciseCol, repCol, weightCol, headerCellStyle);	
		rowNum++;
		
		for (Workout workout : tracker.getWorkoutList()) {
			exerciseNum = 1; // Reseting exerciseNum to 1
			workoutStartRowNum = rowNum; // Tracking to merge dateCol after
			row = sheet.createRow(rowNum);
			loadCellData(sheet, row, cell, dateCellStyle, dateCol, workout.getStartTime().toString().substring(0, 10), 0); // Creating date cell
			
			for (Exercise exercise : workout.getExerciseArrayList()) {
				exerciseStartRowNum = rowNum; // Tracking to merge exerciseCol after as well
				
				if(exerciseNum % 2 != 0) { // Loading the exercise Name cell
					loadCellData(sheet, row, cell, oddExerciseNum, exerciseCol, exercise.getExerciseName(), 0);
				} else {
					loadCellData(sheet, row, cell, evenExerciseNum, exerciseCol, exercise.getExerciseName(), 0);
				}
				
				for (Set set : exercise.getSetList()) {
					if(exerciseNum % 2 != 0) { // Load rep and weight cells
						loadCellData(sheet, row, cell, oddExerciseNum, repCol, null, set.getReps());
						loadCellData(sheet, row, cell, oddExerciseNum, weightCol, null, set.getWeight());
					} else {
						loadCellData(sheet, row, cell, evenExerciseNum, repCol, null, set.getReps());
						loadCellData(sheet, row, cell, evenExerciseNum, weightCol, null, set.getWeight());
					}
					
					
					row = sheet.createRow(rowNum);
					rowNum++; // Increases row for new set
				}
				
				exerciseNum++; // Increases amount of exercises by 1
				
				// Creates a border to separate the exercise from the one below it (if applicable)
				RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(rowNum, rowNum, exerciseCol, weightCol) , sheet);
				
				// Merging the exercise name column cells together
				
				System.out.println(exerciseStartRowNum);
				System.out.println(rowNum);
				if (exerciseStartRowNum != rowNum - 1) {
					sheet.addMergedRegion(new CellRangeAddress(exerciseStartRowNum, rowNum - 1, exerciseCol, exerciseCol));
				}			
			}
			
			// Merging the date column cells together
			sheet.addMergedRegion(new CellRangeAddress(workoutStartRowNum, rowNum, dateCol, dateCol));

			// Merging the gap in between workouts
			rowNum++;
			row = sheet.createRow(rowNum);
			cell = row.createCell(dateCol);
			cell.setCellStyle(headerCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, dateCol, weightCol));
			
			// Merging the cell to the right of the workout
			row = sheet.getRow(workoutStartRowNum);
			cell = row.createCell(weightCol + 1);
			cell.setCellStyle(headerCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(workoutStartRowNum, sheet.getLastRowNum(), weightCol + 1, weightCol + 1));
							
			// Adding the borders to and around the workout
			RegionUtil.setBorderBottom(BorderStyle.THICK, new CellRangeAddress(rowNum, rowNum, dateCol, weightCol) , sheet);
			RegionUtil.setBorderTop(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, workoutStartRowNum, dateCol, weightCol) , sheet);
			RegionUtil.setBorderLeft(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, rowNum, dateCol, dateCol) , sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(workoutStartRowNum, rowNum, dateCol, dateCol) , sheet);
			RegionUtil.setBorderRight(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, rowNum, weightCol, weightCol) , sheet);
			
			rowNum++; // Increases row for next workout			
		}
		
		sheet.autoSizeColumn(dateCol, true);
		sheet.autoSizeColumn(exerciseCol, true);
		sheet.autoSizeColumn(repCol, true);
		sheet.autoSizeColumn(weightCol, true);
		
		/*
		 * if (counter % 5 == 0) { dateCol += 5; exerciseCol += 5; repCol += 5;
		 * weightCol += 5;
		 * 
		 * rowNum = 0; }
		 */
	}
	
	private static void loadCellData(XSSFSheet sheet, Row row, Cell cell, CellStyle cellStyle, int colNum, String stringValue, double doubleValue) {	
		cell = row.createCell(colNum);
		
		if (stringValue != null) {
			cell.setCellValue(stringValue);
		} else {
			cell.setCellValue(doubleValue);
		}
		
		cell.setCellStyle(cellStyle);
	}

	// This works really well
	public static void saveWorkoutExcel(WorkoutTracker tracker, XSSFWorkbook workbook)  {
		int dateCol = 0, exerciseCol = 1, repCol = 2, weightCol = 3; // Will modify after in loop to other columns	
		int rowNum = 0, exerciseNum = 1, workoutStartRowNum = 0, workoutEndRowNum = 0;
		int exerciseStartRowNum = 0, exerciseEndRowNum = 0;
		int counter = 0;
		String workoutDate = "";
		XSSFSheet sheet;
		Row row;
		Cell cell;
		
		sheet = workbook.createSheet();	
		
		///// OUR COLORS FOR THE CELLS /////
		CellStyle oddExerciseNum = sheet.getWorkbook().createCellStyle();
		oddExerciseNum.setFillForegroundColor(IndexedColors.AQUA.getIndex()); 
		oddExerciseNum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		oddExerciseNum.setAlignment(HorizontalAlignment.CENTER);
		oddExerciseNum.setVerticalAlignment(VerticalAlignment.CENTER);
				
		CellStyle evenExerciseNum = sheet.getWorkbook().createCellStyle();
		evenExerciseNum.setFillForegroundColor(IndexedColors.CORAL.getIndex()); 
		evenExerciseNum.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		evenExerciseNum.setAlignment(HorizontalAlignment.CENTER);
		evenExerciseNum.setVerticalAlignment(VerticalAlignment.CENTER);
		
		CellStyle dateCellStyle = sheet.getWorkbook().createCellStyle();
		dateCellStyle.setFillForegroundColor(IndexedColors.TAN.getIndex()); 
		dateCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
		dateCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); 
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		Font font = workbook.createFont();
		font.setBold(true);
		headerCellStyle.setFont(font);
		
		///// CREATING HEADER ROW /////
		row = sheet.createRow(rowNum++);
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
		cell.setCellStyle(headerCellStyle);

		for (Workout workout : tracker.getWorkoutList()) {
			
			///// DATE CELL /////
			workoutStartRowNum = rowNum;
			workoutDate = workout.getStartTime().toString().substring(0, 10);
			row = sheet.createRow(rowNum++);
			cell = row.createCell(dateCol);
			cell.setCellValue(workoutDate);
			cell.setCellStyle(dateCellStyle);

			for (Exercise exercise : workout.getExerciseArrayList()) {
				
				///// EXERCISE NAME CELL /////
				cell = row.createCell(exerciseCol);
				cell.setCellValue(exercise.getExerciseName());
				exerciseStartRowNum = rowNum - 1;
				exerciseEndRowNum = rowNum - 1;
				
				if (exerciseNum % 2 != 0) {
					cell.setCellStyle(oddExerciseNum);
				} else {
					cell.setCellStyle(evenExerciseNum);
				}

				for (Set set : exercise.getSetList()) {
					///// REP CELL /////
					cell = row.createCell(repCol);
					cell.setCellValue(set.getReps());
					if (exerciseNum % 2 != 0) {
						cell.setCellStyle(oddExerciseNum);
					} else {
						cell.setCellStyle(evenExerciseNum);
					}
					
					///// WEIGHT CELL /////
					cell = row.createCell(weightCol);
					cell.setCellValue(set.getWeight());
					if (exerciseNum % 2 != 0) {
						cell.setCellStyle(oddExerciseNum);
					} else {
						cell.setCellStyle(evenExerciseNum);
					}
					row = sheet.createRow(rowNum++);		
					
					exerciseEndRowNum++;
				}
				
				RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(exerciseEndRowNum - 1, exerciseEndRowNum - 1, exerciseCol, weightCol) , sheet);
				
				// Merging the exercise name column cells together
				if (exerciseStartRowNum != exerciseEndRowNum - 1) {
					sheet.addMergedRegion(new CellRangeAddress(exerciseStartRowNum, exerciseEndRowNum - 1, exerciseCol, exerciseCol));
				}
				
				exerciseNum++;

				
			}			
			
			workoutEndRowNum = rowNum - 2;
			
			// Merging the date column cells together
			sheet.addMergedRegion(new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol));

			// Merging the gap in between workouts
			row = sheet.getRow(rowNum - 1);
			cell = row.createCell(dateCol);
			cell.setCellStyle(headerCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, dateCol, weightCol));
			
			// Merging the cell to the right of the workout
			row = sheet.getRow(workoutStartRowNum);
			cell = row.createCell(weightCol + 1);
			cell.setCellStyle(headerCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(workoutStartRowNum, sheet.getLastRowNum(), weightCol + 1, weightCol + 1));
				
			// Adding the borders to and around the workout
			RegionUtil.setBorderBottom(BorderStyle.THICK, new CellRangeAddress(workoutEndRowNum, workoutEndRowNum, dateCol, weightCol) , sheet);
			RegionUtil.setBorderTop(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, workoutStartRowNum, dateCol, weightCol) , sheet);
			RegionUtil.setBorderLeft(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol) , sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, dateCol, dateCol) , sheet);
			RegionUtil.setBorderRight(BorderStyle.THICK, new CellRangeAddress(workoutStartRowNum, workoutEndRowNum, weightCol, weightCol) , sheet);

			exerciseNum = 1;		
			counter++;
		}
				
		sheet.autoSizeColumn(dateCol, true);
		sheet.autoSizeColumn(exerciseCol, true);
		sheet.autoSizeColumn(repCol, true);
		sheet.autoSizeColumn(weightCol, true);

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
