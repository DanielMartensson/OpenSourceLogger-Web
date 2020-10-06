package se.danielmartensson.views;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

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

import se.danielmartensson.entities.DataLogg;
import se.danielmartensson.repositories.DataLoggRepository;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.Top;

@Route("mysql")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MySQLView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DataLoggRepository dataLoggRepository;

	private ApexCharts apexChart;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Begin to create the graf
		apexChart = new Graf("Viewing from MySQL").getApexChart();

		// Then create the control panel
		createSearchPanel();
	}

	private void createSearchPanel() {
		// Set the logger ids
		List<DataLogg> dataLoggers = dataLoggRepository.findAll();
		Select<Long> loggerId = new Select<>();
		List<Long> loggerIds = new ArrayList<Long>();
		for(int i = 0; i < dataLoggers.size(); i++) {
			long value = dataLoggers.get(i).getLoggerId();
			if(loggerIds.contains(value) == false)
				loggerIds.add(value);
		}
		loggerId.setItems(loggerIds);
		loggerId.setPlaceholder("No id");
		loggerId.setLabel("Logger Id");
		
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
			if(loggerId.getValue() == null)
				return;
			long selectedLoggerId = loggerId.getValue();
			List<DataLogg> dataLogg = dataLoggRepository.findByLoggerId(selectedLoggerId);
			int amountOfSamples = dataLogg.size();
			int pulses = 0;
			if(amountOfSamples > 0)
				dataLogg.get(amountOfSamples-1).getPulseNumber();
			countAmoutOfSamples.setValue(amountOfSamples);
			pulseField.setValue(pulses);
		});
		
		// Download all data
		Anchor download = new Anchor();
		loggerId.addValueChangeListener(e-> {
			updateDownloadButton(loggerId, download);
		});
		download.getElement().setAttribute("download",true);
        
		// Range settings
		IntegerField indexFirst = new IntegerField();
		indexFirst.setMin(1);
		indexFirst.setPlaceholder("No index");
		indexFirst.setLabel("First index");
		IntegerField indexLast = new IntegerField();
		indexLast.setMin(1);
		indexLast.setPlaceholder("No index");
		indexLast.setLabel("Last index");
		
		// Create plot
		Button createPlot = new Button("Plot");
		createPlot.addClickListener(e -> {
			// Quick check if we have selected values
			if(indexFirst.getValue() == null || indexLast.getValue() == null || countAmoutOfSamples.getValue() == null) {
				new Notification("You need to count samples or set the index", 3000).open();
				return;
			}
			int firstIndex = indexFirst.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue();
			if(firstIndex > lastIndex) {
				new Notification("First index cannot be greater than last index", 3000).open();
				return;
			}
			if(lastIndex > samples) {
				new Notification("Not enough of samples", 3000).open();
				return;
			}
			if(lastIndex <= 0) {
				new Notification("Last index cannot be under 1", 3000).open();
				return;
			}
			if(firstIndex <= 0) {
				new Notification("First index cannot be under 1", 3000).open();
				return;
			}
			
			// Change to zero-indexing by removing one value from firstIndex
			firstIndex--;
			
			// Get the amount of samples we want to show
			int selectedSamples = lastIndex - firstIndex;
			
			// Get the selected rows in the database depending on choice of loggerId
			List<DataLogg> selectedLogger = dataLoggRepository.findByLoggerIdOrderByDateTime(loggerId.getValue());
			Float[] dataAI0 = new Float[selectedSamples];
			Float[] dataAI1 = new Float[selectedSamples];
			Float[] dataAI2 = new Float[selectedSamples];
			Float[] dataAI3 = new Float[selectedSamples];
			Float[] dataDO0 = new Float[selectedSamples];
			Float[] dataDO1 = new Float[selectedSamples];
			Float[] dataDO2 = new Float[selectedSamples];
			Float[] dataDO3 = new Float[selectedSamples];
			IntStream.range(0, selectedSamples).forEach(i -> {
				DataLogg dataLogg = selectedLogger.get(i);
				dataAI0[i] = dataLogg.getAI0();
				dataAI1[i] = dataLogg.getAI1();
				dataAI2[i] = dataLogg.getAI2();
				dataAI3[i] = dataLogg.getAI3();
				dataDO0[i] = (float) dataLogg.getDO0();
				dataDO1[i] = (float) dataLogg.getDO1();
				dataDO2[i] = (float) dataLogg.getDO2();
				dataDO3[i] = (float) dataLogg.getDO3();
			});
			  
			// Update
			apexChart.updateSeries(
					createSerie(dataAI0, "AI0"),
					createSerie(dataAI1, "AI1"),
					createSerie(dataAI2, "AI2"),
					createSerie(dataAI3, "AI3"),
					createSerie(dataDO0, "DO0"),
					createSerie(dataDO1, "DO1"),
					createSerie(dataDO2, "DO2"),
					createSerie(dataDO3, "DO3"));
		});
		
		// Delete
		Button deletePlot = new Button("Delete");
		deletePlot.addClickListener(e -> {
			// Quick check if we have selected values
			if(indexFirst.getValue() == null || indexLast.getValue() == null || countAmoutOfSamples.getValue() == null) {
				new Notification("You need to count samples or set the index", 3000).open();
				return;
			}
			int firstIndex = indexFirst.getValue(); // Min 1
			int lastIndex = indexLast.getValue(); // Min 1
			int samples = countAmoutOfSamples.getValue();
			if(firstIndex > lastIndex) {
				new Notification("First index cannot be greater than last index", 3000).open();
				return;
			}
			if(lastIndex > samples) {
				new Notification("Not enough of samples", 3000).open();
				return;
			}
			if(lastIndex <= 0) {
				new Notification("Last index cannot be under 1", 3000).open();
				return;
			}
			if(firstIndex <= 0) {
				new Notification("First index cannot be under 1", 3000).open();
				return;
			}
			
			// Show dialog and ask
			Dialog dialog = new Dialog(new Label("Do you want to delete samples between " + firstIndex + " and " + lastIndex));
			dialog.setCloseOnEsc(false);
			dialog.setCloseOnOutsideClick(false);
			
			NativeButton delete = new NativeButton("Delete", event -> {
				// Deleting parts of selectedLogger. We split into maximum 2000 
				List<DataLogg> selectedLogger = dataLoggRepository.findByLoggerIdOrderByDateTime(loggerId.getValue());
				List<DataLogg> deleteThese = selectedLogger.subList(firstIndex - 1, lastIndex);
				for(List<DataLogg> deleteTheseLists : Lists.partition(deleteThese, 2000)) {
					dataLoggRepository.deleteInBatch(deleteTheseLists);
				}
				dialog.close();
				
				// This will prevent us to plot values that don't exist - You need to press the counting button first
				countAmoutOfSamples.setValue(countAmoutOfSamples.getValue() - (lastIndex - firstIndex + 1));
				updateDownloadButton(loggerId, download);
			});
			NativeButton cancelButton = new NativeButton("Cancel", event -> {
				dialog.close();
			});
			
			dialog.add(delete, cancelButton);
			dialog.open();
			
		});
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(loggerId, indexFirst, indexLast, countAmoutOfSamples, pulseField);
		HorizontalLayout secondRow = new HorizontalLayout(countSamples, createPlot, download, deletePlot);
		VerticalLayout layout = new VerticalLayout(firstRow, secondRow, apexChart);
		setContent(layout);
	}
	
	private void updateDownloadButton(Select<Long> loggerId, Anchor download) {
		String fileName = String.valueOf(loggerId.getValue()) + ".csv";
		List<DataLogg> selectedLogger = dataLoggRepository.findByLoggerIdOrderByDateTime(loggerId.getValue());
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
	
	public StreamResource getStreamResource(String filename, List<DataLogg> selectedLogger) {
		// Create a large CSV file in a form of StringBuilder and then convert it all to bytes
		StringWriter stringWriter = new StringWriter();
		stringWriter.write("id, dateTime, DO0, DO1, DO2, DO3, AI0, AI1, AI2, AI3, loggerId, samplingTime, pulseNumber, breakPulseLimit, stopSignal, comment\n");
		for (int i = 0; i < selectedLogger.size(); ++ i) {
			DataLogg dataLogg = selectedLogger.get(i);
			String row = dataLogg.getId() + "," +
			dataLogg.getDateTime() + "," +
			dataLogg.getDO0() + "," +
			dataLogg.getDO1() + "," +
			dataLogg.getDO2() + "," +
			dataLogg.getDO3() + "," +
			dataLogg.getAI0() + "," +
			dataLogg.getAI1() + "," +
			dataLogg.getAI2() + "," +
			dataLogg.getAI3() + "," +
			dataLogg.getLoggerId() + "," +
			dataLogg.getSamplingTime() + "," +
			dataLogg.getPulseNumber() + "," +
			dataLogg.getBreakPulseLimit() + "," +
			dataLogg.isStopSignal() + "," +
			dataLogg.getComment() + "\n";
			stringWriter.write(row);
		}
		
		// Try to download
		try {
			byte[] buffer = stringWriter.toString().getBytes("UTF-8");
			return new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		} catch (UnsupportedEncodingException e) {
			byte[] buffer = new byte[] {0};
			return new StreamResource(filename, () -> new ByteArrayInputStream(buffer));
		}
    }
}
