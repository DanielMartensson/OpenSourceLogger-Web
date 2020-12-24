package se.danielmartensson.views;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.helper.Series;
import com.google.common.collect.Lists;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import se.danielmartensson.entities.Data;
import se.danielmartensson.service.DataService;
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

	public MySQLView(DataService dataService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Begin to create the graf
		ApexCharts apexChart = new Graf("Viewing from MySQL").getApexChart();

		// Create show data log drop down button
		Select<Data> selectJob = new Select<>();
		selectJob.setItems(dataService.findAllNames());
		selectJob.setPlaceholder("No job selected");
		selectJob.setLabel("Job name");

		// Downloader when we select a new job
		Anchor download = new Anchor();
		selectJob.addValueChangeListener(e -> updateDownloadButton(selectJob, download, dataService));
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
		countSamples.addClickListener(e -> {
			if (selectJob.getValue() == null)
				return; // Not selected
			List<Data> dataOfJobName = dataService.findByJobName(selectJob.getValue().getJobName());
			if (dataOfJobName == null)
				return; // No samples
			int amountOfSamples = dataOfJobName.size();
			int pulses = 0;
			if (amountOfSamples > 0)
				pulses = dataOfJobName.get(amountOfSamples - 1).getPulseNumber();
			countAmoutOfSamples.setValue(amountOfSamples);
			pulseField.setValue(pulses);
		});

		// Range settings
		IntegerField indexFirst = new IntegerField();
		indexFirst.setMin(1);
		indexFirst.setPlaceholder("No index");
		indexFirst.setLabel("First index");
		IntegerField indexLast = new IntegerField();
		indexLast.setMin(1);
		indexLast.setPlaceholder("No index");
		indexLast.setLabel("Last index");

		// Create plot button
		Button createPlot = new Button("Plot");
		createPlot.addClickListener(e -> {
			// Quick check if we have selected values
			if (indexFirst.getValue() == null || indexLast.getValue() == null || countAmoutOfSamples.getValue() == null) {
				new Notification("You need to count samples or set the index", 3000).open();
				return;
			}
			int firstIndex = indexFirst.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue();
			if (firstIndex > lastIndex) {
				new Notification("First index cannot be greater than last index", 3000).open();
				return;
			}
			if (lastIndex > samples) {
				new Notification("Not enough of samples", 3000).open();
				return;
			}
			if (lastIndex <= 0) {
				new Notification("Last index cannot be under 1", 3000).open();
				return;
			}
			if (firstIndex <= 0) {
				new Notification("First index cannot be under 1", 3000).open();
				return;
			}

			// Change to zero-indexing by removing one value from firstIndex
			firstIndex--;

			// Get the amount of samples we want to show
			int selectedSamples = lastIndex - firstIndex;

			// Get the selected rows in the database depending on choice of loggerId
			List<Data> selectedData = dataService.findByJobNameOrderByDateTime(selectJob.getValue().getJobName());
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
			for(int i = firstIndex; i < lastIndex; i++){
				Data data = selectedData.get(i);
				A0[i-firstIndex] = data.getA0();
				A1[i-firstIndex] = data.getA1();
				A2[i-firstIndex] = data.getA2();
				A3[i-firstIndex] = data.getA3();
				SA0[i-firstIndex] = data.getSa0();
				SA1[i-firstIndex] = data.getSa1();
				SA1D[i-firstIndex] = data.getSa1d();
				SA2D[i-firstIndex] = data.getSa2d();
				SA3D[i-firstIndex] = data.getSa3d();
				PWM0[i-firstIndex] = (float) data.getP0();
				PWM1[i-firstIndex] = (float) data.getP1();
				PWM2[i-firstIndex] = (float) data.getP2();
				PWM3[i-firstIndex] = (float) data.getP3();
				PWM4[i-firstIndex] = (float) data.getP4();
				PWM5[i-firstIndex] = (float) data.getP5();
				PWM6[i-firstIndex] = (float) data.getP6();
				PWM7[i-firstIndex] = (float) data.getP7();
				PWM8[i-firstIndex] = (float) data.getP8();
				DAC0[i-firstIndex] = (float) data.getD0();
				DAC1[i-firstIndex] = (float) data.getD1();
				DAC2[i-firstIndex] = (float) data.getD2();
			}

			// Update
			apexChart.updateSeries(
					createSerie(A0, "A0"),
					createSerie(A1, "A1"),
					createSerie(A2, "A2"),
					createSerie(A3, "A3"),
					createSerie(SA0, "SA0"),
					createSerie(SA1, "SA1"),
					createSerie(SA1D, "SA1D"),
					createSerie(SA2D, "SA2D"),
					createSerie(SA3D, "SA3D"),
					createSerie(PWM0, "P0"),
					createSerie(PWM1, "P1"),
					createSerie(PWM2, "P2"),
					createSerie(PWM3, "P3"),
					createSerie(PWM4, "P4"),
					createSerie(PWM5, "P5"),
					createSerie(PWM6, "P6"),
					createSerie(PWM7, "P7"),
					createSerie(PWM8, "P8"),
					createSerie(DAC0, "D0"),
					createSerie(DAC1, "D1"),
					createSerie(DAC2, "D2"));
		});

		// Delete button
		Button deletePlot = new Button("Delete");
		deletePlot.addClickListener(e -> {
			// Quick check if we have selected values
			if (indexFirst.getValue() == null || indexLast.getValue() == null || countAmoutOfSamples.getValue() == null) {
				new Notification("You need to count samples or set the index", 3000).open();
				return;
			}
			int firstIndex = indexFirst.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue();
			if (firstIndex > lastIndex) {
				new Notification("First index cannot be greater than last index", 3000).open();
				return;
			}
			if (lastIndex > samples) {
				new Notification("Not enough of samples", 3000).open();
				return;
			}
			if (lastIndex <= 0) {
				new Notification("Last index cannot be under 1", 3000).open();
				return;
			}
			if (firstIndex <= 0) {
				new Notification("First index cannot be under 1", 3000).open();
				return;
			}

			// Show dialog and ask
			Dialog dialog = new Dialog(new Label("Do you want to delete samples between " + firstIndex + " and " + lastIndex));
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);

			NativeButton delete = new NativeButton("Delete", event -> {
				// Deleting parts of selectedLogger. We split into maximum 2000
				List<Data> slectedData = dataService.findByJobNameOrderByDateTime(selectJob.getValue().getJobName());
				List<Data> deleteThese = slectedData.subList(firstIndex - 1, lastIndex);
				for (List<Data> deleteTheseLists : Lists.partition(deleteThese, 2000)) {
					dataService.deleteInBatch(deleteTheseLists);
				}
				dialog.close();

				// This will prevent us to plot values that don't exist - You need to press the
				// counting button first
				countAmoutOfSamples.setValue(countAmoutOfSamples.getValue() - (lastIndex - firstIndex + 1));
				updateDownloadButton(selectJob, download, dataService);
			});
			NativeButton cancelButton = new NativeButton("Cancel", event -> {
				dialog.close();
			});

			dialog.add(delete, cancelButton);
			dialog.open();

		});

		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(selectJob, indexFirst, indexLast, countAmoutOfSamples, pulseField);
		HorizontalLayout secondRow = new HorizontalLayout(countSamples, createPlot, download, deletePlot);
		VerticalLayout layout = new VerticalLayout(firstRow, secondRow, apexChart);
		setContent(layout);

	}

	// We need to update the download button if we changed the logger ID, else we
	// got two buttons or more
	private void updateDownloadButton(Select<Data> selectJob, Anchor download, DataService dataService) {
		String fileName = String.valueOf(selectJob.getValue()) + ".csv";
		List<Data> selectedLogger = dataService.findByJobNameOrderByDateTime(selectJob.getValue().getJobName());
		download.removeAll();
		download.removeHref();
		download.setHref(getStreamResource(fileName, selectedLogger));
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
		stringWriter.write("id,jobName,calibrationName,dateTime,sa0,sa1,sa1d,sa2d,sa3d,a0,a1,a2,a3,i0,i1,i2,i3,i4,i5,p0,p1,p2,p3,p4,p5,p6,p7,p8,d0,d1,d2,pulseNumber,breakPulseLimit,stopSignal\n");
		for (int i = 0; i < selectedData.size(); ++i) {
			Data data = selectedData.get(i);
			String row = data.getId() + "," +
					data.getJobName() + "," +
					data.getCalibrationName() + "," +
					data.getDateTime() + "," +
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
