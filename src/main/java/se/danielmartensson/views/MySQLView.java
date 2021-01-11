package se.danielmartensson.views;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import se.danielmartensson.entities.Data;
import se.danielmartensson.entities.Job;
import se.danielmartensson.service.DataService;
import se.danielmartensson.service.JobService;
import se.danielmartensson.tools.Filtering;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.Top;

@Route("mysql")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PreserveOnRefresh
/**
 * This is the database viewer class
 * 
 * @author dell
 *
 */
public class MySQLView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MySQLView(DataService dataService, JobService jobService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Begin to create the graf
		ApexCharts apexChart = new Graf("Viewing from MySQL").getApexChart();

		// Create show data log drop down button
		Select<Job> selectJob = new Select<>();
		selectJob.setItems(jobService.findAll());
		selectJob.setPlaceholder("No job selected");
		selectJob.setLabel("Job name");

		// Downloader when we select a new job
		Anchor download = new Anchor();
		download.getElement().setAttribute("download", true);

		// Count field
		IntegerField countAmoutOfSamples = new IntegerField();
		countAmoutOfSamples.setMin(0);
		countAmoutOfSamples.setEnabled(false);
		countAmoutOfSamples.setPlaceholder("No samples");
		countAmoutOfSamples.setLabel("Amout of samples");

		// Pulse fields
		IntegerField pulseField = new IntegerField();
		pulseField.setMin(0);
		pulseField.setEnabled(false);
		pulseField.setPlaceholder("No pulses");
		pulseField.setLabel("Amout of pulses");

		// Listener for counting how many samples of selected logger id
		Button countSamples = new Button("Count samples and pulses");
		countSamples.addClickListener(e -> updateSamplesAndPulses(selectJob, dataService, countAmoutOfSamples, pulseField));

		// Range settings
		IntegerField indexFirst = createRangeField(1, "No index", "First index");
		IntegerField indexStep = createRangeField(1, "No index", "Step index");
		IntegerField indexLast = createRangeField(1, "No index", "Last index");
		
		// MovingAverage factor
		Checkbox doFiltering = new Checkbox("Moving average", false);
		IntegerField a0Points = createMovingAveragePointSelector(Data.Analog0 + " points", 1, 100);
		IntegerField a1Points = createMovingAveragePointSelector(Data.Analog1 + " points", 1, 100);
		IntegerField a2Points = createMovingAveragePointSelector(Data.Analog2 + " points", 1, 100);
		IntegerField a3Points = createMovingAveragePointSelector(Data.Analog3 + " points", 1, 100);
		IntegerField sa0Points = createMovingAveragePointSelector(Data.SigmaDelta0 + " points", 1, 100);
		IntegerField sa1Points = createMovingAveragePointSelector(Data.SigmaDelta1 + " points", 1, 100);
		IntegerField sa1dPoints = createMovingAveragePointSelector(Data.SigmaDeltaDifferential1 + " points", 1, 100);
		IntegerField sa2dPoints = createMovingAveragePointSelector(Data.SigmaDeltaDifferential2 + " points", 1, 100);
		IntegerField sa3dPoints = createMovingAveragePointSelector(Data.SigmaDeltaDifferential3 + " points", 1, 100);
		
		// Which plot should be shown
		Checkbox showA0 = new Checkbox(Data.Analog0);
		Checkbox showA1 = new Checkbox(Data.Analog1);
		Checkbox showA2 = new Checkbox(Data.Analog2);
		Checkbox showA3 = new Checkbox(Data.Analog3);
		Checkbox showSA0 = new Checkbox(Data.SigmaDelta0);
		Checkbox showSA1 = new Checkbox(Data.SigmaDelta1);
		Checkbox showSA1D = new Checkbox(Data.SigmaDeltaDifferential1);
		Checkbox showSA2D = new Checkbox(Data.SigmaDeltaDifferential2);
		Checkbox showSA3D = new Checkbox(Data.SigmaDeltaDifferential3);
		Checkbox showP0 = new Checkbox(Data.PWM0);
		Checkbox showP1 = new Checkbox(Data.PWM1);
		Checkbox showP2 = new Checkbox(Data.PWM2);
		Checkbox showP3 = new Checkbox(Data.PWM3);
		Checkbox showP4 = new Checkbox(Data.PWM4);
		Checkbox showP5 = new Checkbox(Data.PWM5);
		Checkbox showP6 = new Checkbox(Data.PWM6);
		Checkbox showP7 = new Checkbox(Data.PWM7);
		Checkbox showP8 = new Checkbox(Data.PWM8);
		Checkbox showD0 = new Checkbox(Data.DAC0);
		Checkbox showD1 = new Checkbox(Data.DAC1);
		Checkbox showD2 = new Checkbox(Data.DAC2);
		List<Checkbox> seriesBoxes = new ArrayList<Checkbox>();
		seriesBoxes.add(showA0);
		seriesBoxes.add(showA1);
		seriesBoxes.add(showA2);
		seriesBoxes.add(showA3);
		seriesBoxes.add(showSA0);
		seriesBoxes.add(showSA1);
		seriesBoxes.add(showSA1D);
		seriesBoxes.add(showSA2D);
		seriesBoxes.add(showSA3D);
		seriesBoxes.add(showP0);
		seriesBoxes.add(showP1);
		seriesBoxes.add(showP2);
		seriesBoxes.add(showP3);
		seriesBoxes.add(showP4);
		seriesBoxes.add(showP5);
		seriesBoxes.add(showP6);
		seriesBoxes.add(showP7);
		seriesBoxes.add(showP8);
		seriesBoxes.add(showD0);
		seriesBoxes.add(showD1);
		seriesBoxes.add(showD2);
		
		// Create plot button
		Button createPlot = new Button("Plot");
		createPlot.addClickListener(e -> {
			if(forgotToSelectValues(indexFirst, indexLast, countAmoutOfSamples))
				return;
			int firstIndex = indexFirst.getValue(); // Min 1
			int step = indexStep.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue(); // Min 1
			if(errorIndexing(samples, firstIndex, lastIndex, step))
				return;

			// Change to zero-indexing by removing one value from firstIndex
			firstIndex--;

			// Get the amount of samples and offset we want to show
			int selectedLimit = lastIndex - firstIndex;
			int selectedOffset = firstIndex;
			String jobName = selectJob.getValue().getName();

			// Get the selected rows in the database
			List<Data> selectedData = null;
			if(step > 1)
				selectedData = dataService.findByJobNameOrderByLocalDateTimeAscLimitStep(jobName, selectedOffset, selectedLimit, step);
			else
				selectedData = dataService.findByJobNameOrderByLocalDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
			
			// Do filtering
			if(doFiltering.getValue()) {
				int[] points = new int[] {
										   a0Points.getValue(), 
										   a1Points.getValue(), 
										   a2Points.getValue(), 
										   a3Points.getValue(),
										   sa0Points.getValue(),
										   sa1Points.getValue(),
										   sa1dPoints.getValue(),
										   sa2dPoints.getValue(),
										   sa3dPoints.getValue()};
				Filtering.movingAverageFiltering(selectedData, points);
			}
			
			// Fill the values for the plot
			int selectedSamples = selectedData.size();
			Float[] A0 = new Float[selectedSamples];
			Float[] A1 = new Float[selectedSamples];
			Float[] A2 = new Float[selectedSamples];
			Float[] A3 = new Float[selectedSamples];
			Float[] SA0 = new Float[selectedSamples];
			Float[] SA1 = new Float[selectedSamples];
			Float[] SA1D = new Float[selectedSamples];
			Float[] SA2D = new Float[selectedSamples];
			Float[] SA3D = new Float[selectedSamples];
			Float[] PWM0 = new Float[selectedSamples];
			Float[] PWM1 = new Float[selectedSamples];
			Float[] PWM2 = new Float[selectedSamples];
			Float[] PWM3 = new Float[selectedSamples];
			Float[] PWM4 = new Float[selectedSamples];
			Float[] PWM5 = new Float[selectedSamples];
			Float[] PWM6 = new Float[selectedSamples];
			Float[] PWM7 = new Float[selectedSamples];
			Float[] PWM8 = new Float[selectedSamples];
			Float[] DAC0 = new Float[selectedSamples];
			Float[] DAC1 = new Float[selectedSamples];
			Float[] DAC2 = new Float[selectedSamples];
			int i = 0;
			for(Data data : selectedData){
				A0[i] = data.getA0();
				A1[i] = data.getA1();
				A2[i] = data.getA2();
				A3[i] = data.getA3();
				SA0[i] = data.getSa0();
				SA1[i] = data.getSa1();
				SA1D[i] = data.getSa1d();
				SA2D[i] = data.getSa2d();
				SA3D[i] = data.getSa3d();
				PWM0[i] = (float) data.getP0();
				PWM1[i] = (float) data.getP1();
				PWM2[i] = (float) data.getP2();
				PWM3[i] = (float) data.getP3();
				PWM4[i] = (float) data.getP4();
				PWM5[i] = (float) data.getP5();
				PWM6[i] = (float) data.getP6();
				PWM7[i] = (float) data.getP7();
				PWM8[i] = (float) data.getP8();
				DAC0[i] = (float) data.getD0();
				DAC1[i] = (float) data.getD1();
				DAC2[i] = (float) data.getD2();
				i++;
			}

			// Count how many series boxes that are being checked
			int boxesChecked = 0;
			for(Checkbox seriesBox : seriesBoxes) {
				if(seriesBox.getValue())
					boxesChecked++;
			}
			
			// Create the series list
			Series<?>[] seriesList = new Series[boxesChecked];
			int seriesIndex = 0;
			int seriesBoxesIndex = 0;
			for(Checkbox seriesBox : seriesBoxes) {
				seriesBoxesIndex++; // Will be starting at 1
				if(!seriesBox.getValue())
					continue;
					
				switch(seriesBoxesIndex) {
					case 1:
						seriesList[seriesIndex] = createSerie(A0, Data.Analog0);
						break;
					case 2:
						seriesList[seriesIndex] = createSerie(A1, Data.Analog1);
						break;
					case 3:
						seriesList[seriesIndex] = createSerie(A2, Data.Analog2);
						break;
					case 4:
						seriesList[seriesIndex] = createSerie(A3, Data.Analog3);
						break;
					case 5:
						seriesList[seriesIndex] = createSerie(SA0, Data.SigmaDelta0);
						break;
					case 6:
						seriesList[seriesIndex] = createSerie(SA1, Data.SigmaDelta0);
						break;
					case 7:
						seriesList[seriesIndex] = createSerie(SA1D, Data.SigmaDeltaDifferential1);
						break;
					case 8:
						seriesList[seriesIndex] = createSerie(SA2D, Data.SigmaDeltaDifferential2);
						break;
					case 9:
						seriesList[seriesIndex] = createSerie(SA3D, Data.SigmaDeltaDifferential3);
						break;
					case 10:
						seriesList[seriesIndex] = createSerie(PWM0, Data.PWM0);
						break;
					case 11:
						seriesList[seriesIndex] = createSerie(PWM1, Data.PWM1);
						break;
					case 12:
						seriesList[seriesIndex] = createSerie(PWM2, Data.PWM2);
						break;
					case 13:
						seriesList[seriesIndex] = createSerie(PWM3, Data.PWM3);
						break;
					case 14:
						seriesList[seriesIndex] = createSerie(PWM4, Data.PWM4);
						break;
					case 15:
						seriesList[seriesIndex] = createSerie(PWM5, Data.PWM5);
						break;
					case 16:
						seriesList[seriesIndex] = createSerie(PWM6, Data.PWM6);
						break;
					case 17:
						seriesList[seriesIndex] = createSerie(PWM7, Data.PWM7);
						break;
					case 18:
						seriesList[seriesIndex] = createSerie(PWM8, Data.PWM8);
						break;
					case 19:
						seriesList[seriesIndex] = createSerie(DAC0, Data.DAC0);
						break;
					case 20:
						seriesList[seriesIndex] = createSerie(DAC1, Data.DAC1);
						break;
					case 21:
						seriesList[seriesIndex] = createSerie(DAC2, Data.DAC2);
						break;
				}
				seriesIndex++;
			}
	
			// Update the plot now
			if(boxesChecked > 0)
				apexChart.updateSeries(seriesList);
			
			// Update the download button with new fresh data
			updateDownloadButton(selectJob, download, selectedData);
		});

		// Delete button
		Button deletePlot = new Button("Delete");
		deletePlot.addClickListener(e -> {
			if(forgotToSelectValues(indexFirst, indexLast, countAmoutOfSamples))
				return;
			int firstIndex = indexFirst.getValue(); // Min 1
			int stepIndex = indexStep.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue(); // Min 1
			if(errorIndexing(samples, firstIndex, lastIndex, stepIndex))
				return;

			// Show dialog and ask
			Dialog dialog = new Dialog(new Label("Do you want to delete samples from index " + firstIndex + " and to index " + lastIndex));
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);

			NativeButton delete = new NativeButton("Delete", event -> {
				// This +1 and -1 is because we want to have indexing from zero and one
				int selectedLimit = lastIndex - firstIndex + 1;
				int selectedOffset = firstIndex - 1;
				String jobName = selectJob.getValue().getName();
				dataService.deleteByJobNameOrderByLocalDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
				dialog.close();
				updateSamplesAndPulses(selectJob, dataService, countAmoutOfSamples, pulseField);
			});
			NativeButton cancelButton = new NativeButton("Cancel", event -> dialog.close());
			dialog.add(delete, cancelButton);
			dialog.open();
		});
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(selectJob, indexFirst, indexStep, indexLast, countAmoutOfSamples, pulseField);
		HorizontalLayout secondRow = new HorizontalLayout(countSamples, doFiltering, createPlot, download, deletePlot);
		secondRow.setAlignItems(Alignment.CENTER);
		HorizontalLayout thirdRow = new HorizontalLayout(a0Points, a1Points, a2Points, a3Points, sa0Points, sa1Points, sa1dPoints, sa2dPoints, sa3dPoints);
		HorizontalLayout fourthRow = new HorizontalLayout(showA0, showA1, showA2, showA3, showSA0, showSA1, showSA1D, showSA2D, showSA3D);
		HorizontalLayout fifthRow = new HorizontalLayout(showP0, showP1, showP2, showP3, showP3, showP4, showP5, showP6, showP7, showP8, showD0, showD1, showD2);
		VerticalLayout layout = new VerticalLayout(firstRow, secondRow, thirdRow, fourthRow, fifthRow, apexChart);
		setContent(layout);

	}

	private IntegerField createRangeField(int minValue, String placeHolder, String label) {
		IntegerField integerField = new IntegerField();
		integerField.setMin(minValue);
		integerField.setPlaceholder(placeHolder);
		integerField.setLabel(label);
		integerField.setValue(minValue);
		integerField.addValueChangeListener(e -> {
			if(e.getValue() == null)
				integerField.setValue(e.getOldValue());
			if(e.getValue() < minValue)
				integerField.setValue(e.getOldValue());
		});
		return integerField;
	}

	private IntegerField createMovingAveragePointSelector(String label, int step, int max) {
		IntegerField filterField = new IntegerField(label);
		filterField.setHasControls(true);
		filterField.setStep(step);
		filterField.setMin(step);
		filterField.setMax(max);
		filterField.setValue(10);
		filterField.addValueChangeListener(e -> {
			if(e.getValue() == null) // Type in wrong value
				filterField.setValue(e.getOldValue());
			if(e.getValue() > max || e.getValue() < step)
				filterField.setValue(e.getOldValue());
		});
		return filterField;
	}

	private void updateSamplesAndPulses(Select<Job> selectJob, DataService dataService, IntegerField countAmoutOfSamples, IntegerField pulseField) {
		// Select jobName
		if (selectJob.getValue() == null)
			return; // Not selected
		String jobName = selectJob.getValue().getName();
								
		// Count samples
		int amountOfSamples = (int) dataService.countByJobName(jobName);
		countAmoutOfSamples.setValue(amountOfSamples);
		pulseField.setValue(0); // Reset only
		if(amountOfSamples == 0)
			return;
					
		// Update the pulse field - We take the last sample
		Data topData = dataService.findFirstByJobNameOrderByLocalDateTimeDesc(jobName);
		pulseField.setValue(topData.getPulseNumber());
	}

	private boolean forgotToSelectValues(IntegerField indexFirst, IntegerField indexLast, IntegerField countAmoutOfSamples) {
		if (indexFirst.getValue() == null || indexLast.getValue() == null || countAmoutOfSamples.getValue() == null) {
			new Notification("You need to count samples or set the index", 3000).open();
			return true;
		}else {
			return false;
		}
	}

	private boolean errorIndexing(int samples, int firstIndex, int lastIndex, int step) {
		if (lastIndex > samples) {
			new Notification("Not enough of samples", 3000).open();
			return true;
		}
		if (firstIndex > lastIndex) {
			new Notification("First index cannot be greater than last index", 3000).open();
			return true;
		}
		if(step > lastIndex) {
			new Notification("Step index cannot be larger than last index", 3000).open();
			return true;
		}
		return false;
	}

	// We need to update the download button if we changed the logger ID, else we
	// got two buttons or more
	private void updateDownloadButton(Select<Job> selectJob, Anchor download, List<Data> selectedData) {
		String fileName = String.valueOf(selectJob.getValue()) + ".csv";
		download.removeAll();
		download.removeHref();
		download.setHref(getStreamResource(fileName, selectedData));
		download.add(new Button("Download " + fileName, new Icon(VaadinIcon.DOWNLOAD_ALT)));
	}

	static public Series<Float> createSerie(Float[] data, String legend) {
		return new Series<Float>(legend, data);
	}

	// Create a large CSV file in a form of StringBuilder and then convert it all to
	// bytes
	public StreamResource getStreamResource(String filename, List<Data> selectedData) {
		StringWriter stringWriter = new StringWriter();
		stringWriter.write(Data.ID + "," + 
						   Data.JOB_NAME + "," + 
						   Data.SENSOR_NAME + "," + 
						   Data.CALIBRATION_NAME + "," + 
						   Data.LOCAL_DATE_TIME + "," + 
						   Data.SigmaDelta0 + "," + 
						   Data.SigmaDelta1 + "," + 
						   Data.SigmaDeltaDifferential1 + "," +  
						   Data.SigmaDeltaDifferential2 + "," +  
						   Data.SigmaDeltaDifferential3 + "," + 
						   Data.Analog0 + "," + 
						   Data.Analog1 + "," + 
						   Data.Analog2 + "," + 
						   Data.Analog3 + "," + 
						   Data.DIGITAL0 + "," + 
						   Data.DIGITAL1 + "," + 
						   Data.DIGITAL2 + "," + 
						   Data.DIGITAL3 + "," + 
						   Data.DIGITAL4 + "," + 
						   Data.DIGITAL5 + "," + 
						   Data.PWM0 + "," + 
						   Data.PWM1 + "," + 
						   Data.PWM2 + "," + 
						   Data.PWM3 + "," + 
						   Data.PWM4 + "," + 
						   Data.PWM5 + "," + 
						   Data.PWM6 + "," + 
						   Data.PWM7 + "," + 
						   Data.PWM8 + "," + 
						   Data.DAC0 + "," + 
						   Data.DAC1 + "," + 
						   Data.DAC2 + "," + 
						   Data.PULSE_NUMBER + "," + 
						   Data.BREAK_PULSE_LIMIT + "," + 
						   Data.STOP_SIGNAL + "\n");
		for (Data data : selectedData) {
			String row = data.getId() + "," +
					data.getJobName() + "," +
					data.getSensorName() + "," +
					data.getCalibrationName() + "," +
					data.getLocalDateTime() + "," +
					data.getSa0() + "," +
					data.getSa1() + "," +
					data.getSa1d() + "," +
					data.getSa2d() + "," +
					data.getSa3d() + "," +
					data.getA0() + "," +
					data.getA1() + "," +
					data.getA2() + "," +
					data.getA3() + "," +
					data.isI0() + "," +
					data.isI1() + "," +
					data.isI2() + "," +
					data.isI3() + "," +
					data.isI4() + "," +
					data.isI5() + "," +
					data.getP0() + "," +
					data.getP1() + "," +
					data.getP2() + "," +
					data.getP3() + "," +
					data.getP4() + "," +
					data.getP5() + "," +
					data.getP6() + "," +
					data.getP7() + "," +
					data.getP8() + "," +
					data.getD0() + "," +
					data.getD1() + "," +
					data.getD2() + "," +
					data.getPulseNumber() + "," +
					data.getBreakPulseLimit() + "," +
					data.isStopSignal() + "\n";
			stringWriter.write(row);
		}

		// Try to download
		try {
			byte[] buffer = stringWriter.toString().getBytes("UTF-8");
			return new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		} catch (UnsupportedEncodingException e) {
			byte[] buffer = new byte[] { 0 };
			return new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		}
	}
}