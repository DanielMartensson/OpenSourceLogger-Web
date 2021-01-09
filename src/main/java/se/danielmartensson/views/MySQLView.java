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
import com.vaadin.flow.component.textfield.NumberField;
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
/**
 * This is the datbase viewer class
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
		IntegerField indexFirst = new IntegerField();
		indexFirst.setMin(1);
		indexFirst.setPlaceholder("No index");
		indexFirst.setLabel("First index");
		IntegerField indexLast = new IntegerField();
		indexLast.setMin(1);
		indexLast.setPlaceholder("No index");
		indexLast.setLabel("Last index");
		
		// Filtfilt factor
		Checkbox doFiltering = new Checkbox("Filtfilt", false);
		NumberField a0Filtfilt = createFiltfiltSimulationConstant("A1 filtfilt", 0.1, 10.0);
		NumberField a1Filtfilt = createFiltfiltSimulationConstant("A2 filtfilt", 0.1, 10.0);
		NumberField a2Filtfilt = createFiltfiltSimulationConstant("A3 filtfilt", 0.1, 10.0);
		NumberField a3Filtfilt = createFiltfiltSimulationConstant("A4 filtfilt", 0.1, 10.0);
		NumberField sa0Filtfilt = createFiltfiltSimulationConstant("SA0 filtfilt", 0.1, 10.0);
		NumberField sa1Filtfilt = createFiltfiltSimulationConstant("SA1 filtfilt", 0.1, 10.0);
		NumberField sa1dFiltfilt = createFiltfiltSimulationConstant("SA1D filtfilt", 0.1, 10.0);
		NumberField sa2dFiltfilt = createFiltfiltSimulationConstant("SA2D filtfilt", 0.1, 10.0);
		NumberField sa3dFiltfilt = createFiltfiltSimulationConstant("SA3D filtfilt", 0.1, 10.0);
		
		// Which plot should be shown
		Checkbox showA0 = new Checkbox("A0");
		Checkbox showA1 = new Checkbox("A1");
		Checkbox showA2 = new Checkbox("A2");
		Checkbox showA3 = new Checkbox("A3");
		Checkbox showSA0 = new Checkbox("SA0");
		Checkbox showSA1 = new Checkbox("SA1");
		Checkbox showSA1D = new Checkbox("SA1D");
		Checkbox showSA2D = new Checkbox("SA2D");
		Checkbox showSA3D = new Checkbox("SA3D");
		Checkbox showP0 = new Checkbox("P0");
		Checkbox showP1 = new Checkbox("P1");
		Checkbox showP2 = new Checkbox("P2");
		Checkbox showP3 = new Checkbox("P3");
		Checkbox showP4 = new Checkbox("P4");
		Checkbox showP5 = new Checkbox("P5");
		Checkbox showP6 = new Checkbox("P6");
		Checkbox showP7 = new Checkbox("P7");
		Checkbox showP8 = new Checkbox("P8");
		Checkbox showD0 = new Checkbox("D0");
		Checkbox showD1 = new Checkbox("D1");
		Checkbox showD2 = new Checkbox("D2");
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
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue(); // Min 1
			if(errorIndexing(samples, firstIndex, lastIndex))
				return;

			// Change to zero-indexing by removing one value from firstIndex
			firstIndex--;

			// Get the amount of samples and offset we want to show
			int selectedLimit = lastIndex - firstIndex;
			int selectedOffset = firstIndex;
			String jobName = selectJob.getValue().getName();

			// Get the selected rows in the database depending on choice of loggerId
			List<Data> selectedData = dataService.findByJobNameOrderByDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
			
			// Do filtering
			if(doFiltering.getValue()) {
				double[] filtfiltKValues = new double[] {
										   a0Filtfilt.getValue(), 
										   a1Filtfilt.getValue(), 
										   a2Filtfilt.getValue(), 
										   a3Filtfilt.getValue(),
										   sa0Filtfilt.getValue(),
										   sa1Filtfilt.getValue(),
										   sa1dFiltfilt.getValue(),
										   sa2dFiltfilt.getValue(),
										   sa3dFiltfilt.getValue()};
				Filtering.filtfilt(selectedData, filtfiltKValues);
			}
			
			// Fill the values for the plot
			Float[] A0 = new Float[selectedLimit];
			Float[] A1 = new Float[selectedLimit];
			Float[] A2 = new Float[selectedLimit];
			Float[] A3 = new Float[selectedLimit];
			Float[] SA0 = new Float[selectedLimit];
			Float[] SA1 = new Float[selectedLimit];
			Float[] SA1D = new Float[selectedLimit];
			Float[] SA2D = new Float[selectedLimit];
			Float[] SA3D = new Float[selectedLimit];
			Float[] PWM0 = new Float[selectedLimit];
			Float[] PWM1 = new Float[selectedLimit];
			Float[] PWM2 = new Float[selectedLimit];
			Float[] PWM3 = new Float[selectedLimit];
			Float[] PWM4 = new Float[selectedLimit];
			Float[] PWM5 = new Float[selectedLimit];
			Float[] PWM6 = new Float[selectedLimit];
			Float[] PWM7 = new Float[selectedLimit];
			Float[] PWM8 = new Float[selectedLimit];
			Float[] DAC0 = new Float[selectedLimit];
			Float[] DAC1 = new Float[selectedLimit];
			Float[] DAC2 = new Float[selectedLimit];
			for(int i = 0; i < selectedData.size(); i++){
				Data data = selectedData.get(i);
				A0[i] = data.getA0Value();
				A1[i] = data.getA1Value();
				A2[i] = data.getA2Value();
				A3[i] = data.getA3Value();
				SA0[i] = data.getSa0Value();
				SA1[i] = data.getSa1Value();
				SA1D[i] = data.getSa1dValue();
				SA2D[i] = data.getSa2dValue();
				SA3D[i] = data.getSa3dValue();
				PWM0[i] = (float) data.getP0Value();
				PWM1[i] = (float) data.getP1Value();
				PWM2[i] = (float) data.getP2Value();
				PWM3[i] = (float) data.getP3Value();
				PWM4[i] = (float) data.getP4Value();
				PWM5[i] = (float) data.getP5Value();
				PWM6[i] = (float) data.getP6Value();
				PWM7[i] = (float) data.getP7Value();
				PWM8[i] = (float) data.getP8Value();
				DAC0[i] = (float) data.getD0Value();
				DAC1[i] = (float) data.getD1Value();
				DAC2[i] = (float) data.getD2Value();
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
						seriesList[seriesIndex] = createSerie(A0, "A0");
						break;
					case 2:
						seriesList[seriesIndex] = createSerie(A1, "A1");
						break;
					case 3:
						seriesList[seriesIndex] = createSerie(A2, "A2");
						break;
					case 4:
						seriesList[seriesIndex] = createSerie(A3, "A3");
						break;
					case 5:
						seriesList[seriesIndex] = createSerie(SA0, "SA0");
						break;
					case 6:
						seriesList[seriesIndex] = createSerie(SA1, "SA1");
						break;
					case 7:
						seriesList[seriesIndex] = createSerie(SA1D, "SA1D");
						break;
					case 8:
						seriesList[seriesIndex] = createSerie(SA2D, "SA2D");
						break;
					case 9:
						seriesList[seriesIndex] = createSerie(SA3D, "SA3D");
						break;
					case 10:
						seriesList[seriesIndex] = createSerie(PWM0, "P0");
						break;
					case 11:
						seriesList[seriesIndex] = createSerie(PWM1, "P1");
						break;
					case 12:
						seriesList[seriesIndex] = createSerie(PWM2, "P2");
						break;
					case 13:
						seriesList[seriesIndex] = createSerie(PWM3, "P3");
						break;
					case 14:
						seriesList[seriesIndex] = createSerie(PWM4, "P4");
						break;
					case 15:
						seriesList[seriesIndex] = createSerie(PWM5, "P5");
						break;
					case 16:
						seriesList[seriesIndex] = createSerie(PWM6, "P6");
						break;
					case 17:
						seriesList[seriesIndex] = createSerie(PWM7, "P7");
						break;
					case 18:
						seriesList[seriesIndex] = createSerie(PWM8, "P8");
						break;
					case 19:
						seriesList[seriesIndex] = createSerie(DAC0, "D0");
						break;
					case 20:
						seriesList[seriesIndex] = createSerie(DAC1, "D1");
						break;
					case 21:
						seriesList[seriesIndex] = createSerie(DAC2, "D2");
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
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue(); // Min 1
			if(errorIndexing(samples, firstIndex, lastIndex))
				return;

			// Show dialog and ask
			Dialog dialog = new Dialog(new Label("Do you want to delete samples between " + firstIndex + " and " + lastIndex));
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);

			NativeButton delete = new NativeButton("Delete", event -> {
				// This +1 and -1 is because we want to have indexing from zero and one
				int selectedLimit = lastIndex - firstIndex + 1;
				int selectedOffset = firstIndex - 1;
				String jobName = selectJob.getValue().getName();
				dataService.deleteByJobNameOrderByDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
				dialog.close();
				updateSamplesAndPulses(selectJob, dataService, countAmoutOfSamples, pulseField);
			});
			NativeButton cancelButton = new NativeButton("Cancel", event -> dialog.close());
			dialog.add(delete, cancelButton);
			dialog.open();
		});
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(selectJob, indexFirst, indexLast, countAmoutOfSamples, pulseField);
		HorizontalLayout secondRow = new HorizontalLayout(countSamples, doFiltering, createPlot, download, deletePlot);
		secondRow.setAlignItems(Alignment.CENTER);
		HorizontalLayout thirdRow = new HorizontalLayout(a0Filtfilt, a1Filtfilt, a2Filtfilt, a3Filtfilt, sa0Filtfilt, sa1Filtfilt, sa1dFiltfilt, sa2dFiltfilt, sa3dFiltfilt);
		HorizontalLayout fourthRow = new HorizontalLayout(showA0, showA1, showA2, showA3, showSA0, showSA1, showSA1D, showSA2D, showSA3D);
		HorizontalLayout fifthRow = new HorizontalLayout(showP0, showP1, showP2, showP3, showP3, showP4, showP5, showP6, showP7, showP8, showD0, showD1, showD2);
		VerticalLayout layout = new VerticalLayout(firstRow, secondRow, thirdRow, fourthRow, fifthRow, apexChart);
		setContent(layout);

	}

	private NumberField createFiltfiltSimulationConstant(String label, double step, double max) {
		NumberField numberFiled = new NumberField(label);
		numberFiled.setHasControls(true);
		numberFiled.setStep(step);
		numberFiled.setMin(step);
		numberFiled.setMax(max);
		numberFiled.setValue(step);
		numberFiled.addValueChangeListener(e -> {
			if(e.getValue() == null) // Type in wrong value
				numberFiled.setValue(e.getOldValue());
		});
		return numberFiled;
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
		Data topData = dataService.findFirstByJobNameOrderByDateTimeDesc(jobName);
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

	private boolean errorIndexing(int samples, int firstIndex, int lastIndex) {
		if (firstIndex > lastIndex) {
			new Notification("First index cannot be greater than last index", 3000).open();
			return true;
		}
		if (lastIndex > samples) {
			new Notification("Not enough of samples", 3000).open();
			return true;
		}
		if (lastIndex <= 0) {
			new Notification("Last index cannot be under 1", 3000).open();
			return true;
		}
		if (firstIndex <= 0) {
			new Notification("First index cannot be under 1", 3000).open();
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
		Series<Float> serie = new Series<Float>();
		serie.setData(data);
		serie.setName(legend);
		return serie;
	}

	// Create a large CSV file in a form of StringBuilder and then convert it all to
	// bytes
	public StreamResource getStreamResource(String filename, List<Data> selectedData) {
		StringWriter stringWriter = new StringWriter();
		stringWriter.write("id,jobName,sensorName,dateTime,sa0Value,sa1Value,sa1dValue,sa2dValue,sa3dValue,a0Value,a1Value,a2Value,a3Value,i0Value,i1Value,i2Value,i3Value,i4Value,i5Value,p0Value,p1Value,p2Value,p3Value,p4Value,p5Value,p6Value,p7Value,p8Value,d0Value,d1Value,d2Value,pulseNumber,breakPulseLimit,stopSignal\n");
		for (Data data : selectedData) {
			String row = data.getId() + "," +
					data.getJobName() + "," +
					data.getSensorName() + "," +
					data.getDateTime() + "," +
					data.getSa0Value() + "," +
					data.getSa1Value() + "," +
					data.getSa1dValue() + "," +
					data.getSa2dValue() + "," +
					data.getSa3dValue() + "," +
					data.getA0Value() + "," +
					data.getA1Value() + "," +
					data.getA2Value() + "," +
					data.getA3Value() + "," +
					data.isI0Value() + "," +
					data.isI1Value() + "," +
					data.isI2Value() + "," +
					data.isI3Value() + "," +
					data.isI4Value() + "," +
					data.isI5Value() + "," +
					data.getP0Value() + "," +
					data.getP1Value() + "," +
					data.getP2Value() + "," +
					data.getP3Value() + "," +
					data.getP4Value() + "," +
					data.getP5Value() + "," +
					data.getP6Value() + "," +
					data.getP7Value() + "," +
					data.getP8Value() + "," +
					data.getD0Value() + "," +
					data.getD1Value() + "," +
					data.getD2Value() + "," +
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