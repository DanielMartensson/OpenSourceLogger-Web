package se.danielmartensson.views;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import se.danielmartensson.views.components.Graf;
import se.danielmartensson.views.database.datalogg.DataLogg;
import se.danielmartensson.views.database.datalogg.DataLoggRepository;
import se.danielmartensson.views.templates.Top;

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
			int amountOfSamples = dataLogg.size()-1;
			int pulses = dataLogg.get(amountOfSamples).getPulseNumber();
			countAmoutOfSamples.setValue(amountOfSamples);
			pulseField.setValue(pulses);
		});
		
		// Download all data
		Anchor download = new Anchor();
		loggerId.addValueChangeListener(e-> {
			String fileName = String.valueOf(loggerId.getValue()) + ".csv";
			List<DataLogg> selectedLogger = dataLoggRepository.findByLoggerId(loggerId.getValue());
			download.setHref(getStreamResource(fileName, selectedLogger));
			download.add(new Button("Download " + fileName, new Icon(VaadinIcon.DOWNLOAD_ALT)));
		});
		download.getElement().setAttribute("download",true);
        
		// Range settings
		IntegerField indexFirst = new IntegerField();
		indexFirst.setMin(0);
		indexFirst.setPlaceholder("No index");
		indexFirst.setLabel("First index");
		IntegerField indexLast = new IntegerField();
		indexLast.setMin(0);
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
			Integer firstIndex = indexFirst.getValue();
			Integer lastIndex = indexLast.getValue() + 1; // This resulting indexing from 0 
			Integer samples = countAmoutOfSamples.getValue();
			if(firstIndex >= lastIndex) {
				new Notification("First index cannot be greater or equal to last index", 3000).open();
				return;
			}
			if(lastIndex > samples) {
				new Notification("Notice that we have indexing from zero here!", 3000).open();
				return;
			}
			
			// Get the selected rows in the database depending on choice of loggerId
			List<DataLogg> selectedLogger = dataLoggRepository.findByLoggerId(loggerId.getValue());
			Float[] dataAI0 = new Float[lastIndex - firstIndex];
			Float[] dataAI1 = new Float[lastIndex - firstIndex];
			Float[] dataAI2 = new Float[lastIndex - firstIndex];
			Float[] dataAI3 = new Float[lastIndex - firstIndex];
			Float[] dataDO0 = new Float[lastIndex - firstIndex];
			Float[] dataDO1 = new Float[lastIndex - firstIndex];
			Float[] dataDO2 = new Float[lastIndex - firstIndex];
			Float[] dataDO3 = new Float[lastIndex - firstIndex];
			for(int i = firstIndex; i < lastIndex; i++) {
				dataAI0[i - firstIndex] = selectedLogger.get(i).getAI0();
				dataAI1[i - firstIndex] = selectedLogger.get(i).getAI1();
				dataAI2[i - firstIndex] = selectedLogger.get(i).getAI2();
				dataAI3[i - firstIndex] = selectedLogger.get(i).getAI3();
				dataDO0[i - firstIndex] = (float) selectedLogger.get(i).getDO0();
				dataDO1[i - firstIndex] = (float) selectedLogger.get(i).getDO1();
				dataDO2[i - firstIndex] = (float) selectedLogger.get(i).getDO2();
				dataDO3[i - firstIndex] = (float) selectedLogger.get(i).getDO3();
			}
			  
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
		
		// Layout
		HorizontalLayout firstRow = new HorizontalLayout(loggerId, indexFirst, indexLast, countAmoutOfSamples, pulseField);
		HorizontalLayout secondRow = new HorizontalLayout(countSamples, createPlot, download);
		VerticalLayout layout = new VerticalLayout(firstRow, secondRow, apexChart);
		setContent(layout);
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
		stringWriter.write("id, dateTime, DO0, DO1, DO2, DO3, AI0, AI1, AI2, AI3, loggerId, samplingTime, pulseNumber, breakPulseLimit, stopSignal\n");
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
			dataLogg.isStopSignal() + "\n";
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
