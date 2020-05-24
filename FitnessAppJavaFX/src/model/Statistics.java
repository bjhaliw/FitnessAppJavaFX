package model;


import java.io.FileOutputStream;
import java.io.IOException;
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
	 * and max amount of volume for each day.
	 * 
	 * Columns: Dates(0), Weight(1), Reps(2), One Rep Max(3), Volume (4)
	 * 
	 * @param tracker
	 * @throws IOException
	 */
	public static void saveGraphExcel(WorkoutTracker tracker) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		String sheetName = "Workout Progression Chart";

		XSSFSheet sheet = workbook.createSheet(sheetName);

		// Workout details (Date, Weights, Reps)
		Row row = sheet.createRow((short) 0);
		
		Cell cell = row.createCell((short) 0);		
		cell.setCellValue("Date");

		cell = row.createCell((short) 1);
		cell.setCellValue("Weight");

		cell = row.createCell((short) 2);
		cell.setCellValue("Reps");

		// First Row
		row = sheet.createRow((short) 1);

		cell = row.createCell((short) 0);
		cell.setCellValue("May 23, 2020");

		cell = row.createCell((short) 1);
		cell.setCellValue(195);

		cell = row.createCell((short) 2);
		cell.setCellValue(5);


		// Second Row
		row = sheet.createRow((short) 2);

		cell = row.createCell((short) 0);
		cell.setCellValue("May 24, 2020");

		cell = row.createCell((short) 1);
		cell.setCellValue(205);

		cell = row.createCell((short) 2);
		cell.setCellValue(5);
		
		// Third Row
		row = sheet.createRow((short) 3);

		cell = row.createCell((short) 0);
		cell.setCellValue("May 25, 2020");

		cell = row.createCell((short) 1);
		cell.setCellValue(210);

		cell = row.createCell((short) 2);
		cell.setCellValue(6);
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 7, 26);

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
				new CellRangeAddress(1, 3, 0, 0));

		XDDFNumericalDataSource<Double> weight = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
				new CellRangeAddress(1, 3, 1, 1));

		XDDFNumericalDataSource<Double> reps = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
				new CellRangeAddress(1, 3, 2, 2));

		XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
		
		XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(dates, weight);
		series1.setTitle("Weight", null);
		series1.setSmooth(true);
		series1.setMarkerSize((short) 6);
		series1.setMarkerStyle(MarkerStyle.STAR);
		
		XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) data.addSeries(dates, reps);
		series2.setTitle("Reps", null);
		series2.setSmooth(true);
		series2.setMarkerSize((short) 6);
		series2.setMarkerStyle(MarkerStyle.SQUARE);
		
		chart.plot(data);

	
		////// SAVING THE INFORMATION ///////
		String fileName = "C:\\Users\\bjhal\\Desktop\\Workout Stuff.xlsx";
		FileOutputStream fos = new FileOutputStream(fileName);
		workbook.write(fos);
		fos.close();
		workbook.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		Statistics.saveGraphExcel(null);
	}
	
}
