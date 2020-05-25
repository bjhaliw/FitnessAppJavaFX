package model;


import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
/**
 * This class is responsible for creating the different statistics that the user
 * would like to see. Examples include graphs, charts, one rep max, etc.
 * Users will be able to choose if they want to see the graphs with JavaFX
 * or creating and updating an Excel spreadsheet and having the graphs
 * printed on there instead. Uses Apache POI to create the Excel spreadsheets.
 * 
 * @author Brenton Haliw
 *
 */
public class Statistics  {

	/**
	 * This method is responsible for creating and storing the user's highest amount
	 * of weight lifted, highest amount of reps for an exercise, one rep max estimation,
	 * and max amount of volume for each day. Writes to an Excel file and saves it on 
	 * the desktop of the user's device.
	 * 
	 * Columns: Dates(0), Weight(1), Reps(2), Volume (3), One Rep Max(4)
	 * 
	 * @param tracker
	 * @throws IOException
	 */
	private static void saveGraphExcel(WorkoutTracker tracker) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		LocalDateTime time = LocalDateTime.now();

		////// WRITING THE VALUES ///////
		writeMaxValues(tracker, workbook);
		
		////// CREATING THE GRAPHS ///////
		createGraphs(workbook);
	
		////// SAVING THE INFORMATION ///////
		String fileName = time.toString().substring(0, 10);
		FileOutputStream fos = new FileOutputStream("C:\\Users\\bjhal\\Desktop\\" + fileName + ".xlsx");
		workbook.write(fos);
		fos.close();
		workbook.close();
		
	}
	
	/**
	 * Writes the max values for each workout onto their own sheets and records the max weight,
	 * max reps, max volume, and one rep max estimate into their specified columns. If this is
	 * the first time the exercise is being recorded, then a brand new sheet is automatically 
	 * made and then filled in.
	 * @param tracker
	 * @param workbook
	 */
	private static void writeMaxValues(WorkoutTracker tracker, XSSFWorkbook workbook) {
		int rowNum = 0;
		XSSFSheet sheet = null;
		Row row;
		Cell cell;
		
		for(Workout workout : tracker.getWorkoutList()) {
			for (Exercise exercise : workout.getExerciseArrayList()) {
				
				sheet = workbook.getSheet(exercise.getExerciseName());
				
				if(sheet == null) { // Sheet doesn't exist for the specific exercise
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
		 * After writing all of the information to the workbook, we go through
		 * each sheet and then autosize it to make the values readable later.
		 */
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			
			sheet.setVerticallyCenter(true);
		}		
	}
	
	/**
	 * This method is responsible for creating the graphs that will be displayed in the
	 * workbook passed in as a parameter. There will be three graphs overall. One will
	 * display BOTH One Rep Max and Max Weight, another will be max reps, and the last
	 * will be max volume. Each sheet is parsed and has graphs off to their right side.
	 * @param workbook
	 */
	private static void createGraphs(XSSFWorkbook workbook) {
		XSSFSheet sheet = null;

		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);
			
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
			leftAxis.setTitle("Max Weight");

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

			XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
			
			XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(dates, weight);
			series1.setTitle("Weight", null);
			series1.setSmooth(true);
			series1.setMarkerSize((short) 6);
			series1.setMarkerStyle(MarkerStyle.STAR);
			
			/*
			 * XDDFLineChartData.Series series2 = (XDDFLineChartData.Series)
			 * data.addSeries(dates, reps); series2.setTitle("Reps", null);
			 * series2.setSmooth(true); series2.setMarkerSize((short) 6);
			 * series2.setMarkerStyle(MarkerStyle.SQUARE);
			 */
			
			XDDFLineChartData.Series series3 = (XDDFLineChartData.Series) data.addSeries(dates, oneRepMax);
			series3.setTitle("One Rep Max", null);
			series3.setSmooth(true);
			series3.setMarkerSize((short) 6);
			series3.setMarkerStyle(MarkerStyle.SQUARE);
			
			chart.plot(data);			
		}
	}
	
	public static void main(String[] args) throws IOException {
		WorkoutTracker tracker = new WorkoutTracker();
				
		tracker.loadWorkoutList("C:\\Users\\bjhal\\Desktop\\WorkoutList.txt");
		
		Statistics.saveGraphExcel(tracker);
	}
	
	
	/*
	 * XSSFWorkbook workbook = new XSSFWorkbook();
	 * 
	 * String sheetName = "Workout Progression Chart";
	 * 
	 * XSSFSheet sheet = workbook.createSheet(sheetName);
	 * 
	 * // Workout details (Date, Weights, Reps) Row row = sheet.createRow((short)
	 * 0);
	 * 
	 * Cell cell = row.createCell((short) 0); cell.setCellValue("Date");
	 * 
	 * cell = row.createCell((short) 1); cell.setCellValue("Weight");
	 * 
	 * cell = row.createCell((short) 2); cell.setCellValue("Reps");
	 * 
	 * // First Row row = sheet.createRow((short) 1);
	 * 
	 * cell = row.createCell((short) 0); cell.setCellValue("May 23, 2020");
	 * 
	 * cell = row.createCell((short) 1); cell.setCellValue(195);
	 * 
	 * cell = row.createCell((short) 2); cell.setCellValue(5);
	 * 
	 * 
	 * // Second Row row = sheet.createRow((short) 2);
	 * 
	 * cell = row.createCell((short) 0); cell.setCellValue("May 24, 2020");
	 * 
	 * cell = row.createCell((short) 1); cell.setCellValue(205);
	 * 
	 * cell = row.createCell((short) 2); cell.setCellValue(5);
	 * 
	 * // Third Row row = sheet.createRow((short) 3);
	 * 
	 * cell = row.createCell((short) 0); cell.setCellValue("May 25, 2020");
	 * 
	 * cell = row.createCell((short) 1); cell.setCellValue(210);
	 * 
	 * cell = row.createCell((short) 2); cell.setCellValue(6);
	 * 
	 * sheet.autoSizeColumn(0); sheet.autoSizeColumn(1); sheet.autoSizeColumn(2);
	 * 
	 * 
	 * XSSFDrawing drawing = sheet.createDrawingPatriarch(); XSSFClientAnchor anchor
	 * = drawing.createAnchor(0, 0, 0, 0, 0, 4, 7, 26);
	 * 
	 * XSSFChart chart = drawing.createChart(anchor);
	 * chart.setTitleText("Max Weight Each Day"); chart.setTitleOverlay(false);
	 * 
	 * XDDFChartLegend legend = chart.getOrAddLegend();
	 * legend.setPosition(LegendPosition.TOP_RIGHT);
	 * 
	 * XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
	 * bottomAxis.setTitle("Date"); XDDFValueAxis leftAxis =
	 * chart.createValueAxis(AxisPosition.LEFT); leftAxis.setTitle("Max Weight");
	 * 
	 * XDDFDataSource<String> dates =
	 * XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 3,
	 * 0, 0));
	 * 
	 * XDDFNumericalDataSource<Double> weight =
	 * XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 3,
	 * 1, 1));
	 * 
	 * XDDFNumericalDataSource<Double> reps =
	 * XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 3,
	 * 2, 2));
	 * 
	 * XDDFLineChartData data = (XDDFLineChartData)
	 * chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
	 * 
	 * XDDFLineChartData.Series series1 = (XDDFLineChartData.Series)
	 * data.addSeries(dates, weight); series1.setTitle("Weight", null);
	 * series1.setSmooth(true); series1.setMarkerSize((short) 6);
	 * series1.setMarkerStyle(MarkerStyle.STAR);
	 * 
	 * XDDFLineChartData.Series series2 = (XDDFLineChartData.Series)
	 * data.addSeries(dates, reps); series2.setTitle("Reps", null);
	 * series2.setSmooth(true); series2.setMarkerSize((short) 6);
	 * series2.setMarkerStyle(MarkerStyle.SQUARE);
	 * 
	 * chart.plot(data);
	 */

	
}
