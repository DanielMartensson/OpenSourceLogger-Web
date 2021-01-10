package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import se.danielmartensson.lists.AnalogInputs;
import se.danielmartensson.lists.AnalogOutputs;
import se.danielmartensson.lists.DigitalInputs;
import se.danielmartensson.lists.PWMRelays;
import se.danielmartensson.tools.Top;

@Route("about")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class only explains what words stands for. It's just a basic grid
 * 
 * @author dell
 *
 */
public class AboutView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Title
		Label title = new Label("Open Source Logger");
		title.setClassName("headingH1");

		// Upload picture
		Image board = new Image("img/board.png", "Board");
		board.setHeight("800px");

		// Analog inputs
		Label analogInputTitle = new Label("Analog inputs");
		analogInputTitle.setClassName("headingH1");
		Grid<AnalogInputs> analogInputGrid = createGridAnalogInputs();

		// Digital inputs
		Label digitalInputTitle = new Label("Digital inputs");
		digitalInputTitle.setClassName("headingH1");
		Grid<DigitalInputs> digitalInputGrid = createGridDigitalInputs();

		// Analog outputs
		Label analogOutputTitle = new Label("Analog outputs");
		analogOutputTitle.setClassName("headingH1");
		Grid<AnalogOutputs> analogOutputGrid = createGridAnalogOutputs();

		// PWM
		Label pwmRelayTitle = new Label("PWM Relays");
		pwmRelayTitle.setClassName("headingH1");
		Grid<PWMRelays> pwmRelayGrid = createGridPWMRelay();
		
		// How to calibrate
		Label calibrationTitle = new Label("Calibration");
		calibrationTitle.setClassName("headingH1");
		TextArea calibrateInstructions = new TextArea("How to calibrate");
		calibrateInstructions.setValue(
				  "1. Go to Calibration and create a new row where you set all MIN value boxes to 0 and MAX value boxes to 1"
				+ "\n2. Go to Sensor and create a new row where you set all MIN value boxes to 0 and MAX value boxes to 1. Now scalar = 1 and bias = 0 (measurement = scalar*ADC + bias)."
				+ "\n3. Go to Control and start logging. "
				+ "\n4. Now apply the minimum & maximum input signal, given from your sensor, into A0, A1, A2, A3, SA0, SA1, SA1D, SA2D, SA3D."
				+ "\n5. Go to MySQL and collect the data download the .csv file after you have plot the data."
				+ "\n6. Open the .csv file and find the minimum & maximum average of A0, A1, A2, A3, SA0, SA1, SA1D, SA2D, SA3D."
				+ "\n7. Go Calibration and write in these ADC values into the MIN value boxes and MAX value boxes."
				+ "\n8. Go to Sensor and write in the minimum & maximum physical value for the sensor e.g 0-400 Bar"
				+ "\n9. Now you have calibrate for that type of sensor e.g 4-20mA or 0-20mA sensor"
				+ "\n10. Notice if you are using another sensor with different output intervall, you need to create a new calibration");
		calibrateInstructions.setEnabled(false);
		calibrateInstructions.setWidthFull();
		
		// Layout
		VerticalLayout layout = new VerticalLayout(title, board, analogInputTitle, analogInputGrid, digitalInputTitle, digitalInputGrid, analogOutputTitle, analogOutputGrid, pwmRelayTitle, pwmRelayGrid, pwmRelayGrid, calibrationTitle, calibrateInstructions);
		layout.setAlignItems(Alignment.CENTER);
		setContent(layout);
	}

	private Grid<PWMRelays> createGridPWMRelay() {
		Grid<PWMRelays> grid = new Grid<>(PWMRelays.class);
		List<PWMRelays> list = new ArrayList<>();
		list.add(new PWMRelays("P0", "4096", "NPN"));
		list.add(new PWMRelays("P1", "4096", "NPN"));
		list.add(new PWMRelays("P2", "4096", "NPN"));
		list.add(new PWMRelays("P3", "4096", "NPN"));
		list.add(new PWMRelays("P4", "4096", "NPN"));
		list.add(new PWMRelays("P5", "4096", "NPN"));
		list.add(new PWMRelays("P6", "4096", "NPN"));
		list.add(new PWMRelays("P7", "4096", "NPN"));
		list.add(new PWMRelays("P8", "4096", "NPN"));
		grid.setItems(list);
		grid.setHeightByRows(true);
		grid.setColumns("variable", "resolution", "loadConnection");
		return grid;
	}

	private Grid<AnalogOutputs> createGridAnalogOutputs() {
		Grid<AnalogOutputs> grid = new Grid<>(AnalogOutputs.class);
		List<AnalogOutputs> list = new ArrayList<>();
		list.add(new AnalogOutputs("D0", "0-3.3V", "4096", "30V"));
		list.add(new AnalogOutputs("D1", "0-3.3V", "4096", "30V"));
		list.add(new AnalogOutputs("D2", "0-3.3V", "4096", "30V"));
		grid.setItems(list);
		grid.setHeightByRows(true);
		grid.setColumns("variable", "signalVoltage", "resolution", "maxReverseVoltage");
		return grid;
	}

	private Grid<DigitalInputs> createGridDigitalInputs() {
		Grid<DigitalInputs> grid = new Grid<>(DigitalInputs.class);
		List<DigitalInputs> list = new ArrayList<>();
		list.add(new DigitalInputs("I0", "0/24V", "50V"));
		list.add(new DigitalInputs("I1", "0/24V", "50V"));
		list.add(new DigitalInputs("I2", "0/24V", "50V"));
		list.add(new DigitalInputs("I3", "0/24V", "50V"));
		list.add(new DigitalInputs("I4", "0/24V", "50V"));
		list.add(new DigitalInputs("I5", "0/24V", "50V"));
		grid.setItems(list);
		grid.setHeightByRows(true);
		grid.setColumns("variable", "signalVoltage", "maxInput");
		return grid;
	}

	private Grid<AnalogInputs> createGridAnalogInputs() {
		Grid<AnalogInputs> grid = new Grid<>(AnalogInputs.class);
		List<AnalogInputs> list = new ArrayList<>();
		list.add(new AnalogInputs("A0", "0-22mA", "0-3.3V", "4096", "Single", "30V", "A0 Min", "A0 Max"));
		list.add(new AnalogInputs("A1", "0-22mA", "0-3.3V", "4096", "Single", "30V", "A1 Min", "A1 Max"));
		list.add(new AnalogInputs("A2", "0-22mA", "0-3.3V", "4096", "Single", "30V", "A2 Min", "A2 Max"));
		list.add(new AnalogInputs("A3", "0-22mA", "0-3.3V", "4096", "Single", "30V", "A3 Min", "A3 Max"));
		list.add(new AnalogInputs("SA0", "0-22mA", "0-3.3V", "65536", "Single", "30V", "Sa0 Min", "Sa0 Max"));
		list.add(new AnalogInputs("SA1", "0-22mA", "0-3.3V", "65536", "Single", "30V", "Sa1 Min", "Sa1 Max"));
		list.add(new AnalogInputs("SA1D", "0-22mA", "0-3.3V", "65536", "Differential", "30V", "Sa1D Min", "Sa1D Max"));
		list.add(new AnalogInputs("SA2D", "0-22mA", "0-3.3V", "65536", "Differential", "30V", "Sa2D Min", "Sa2D Max"));
		list.add(new AnalogInputs("SA3D", "0-22mA", "0-3.3V", "65536", "Differential", "30V", "Sa3D Min", "Sa3D Max"));
		grid.setItems(list);
		grid.setHeightByRows(true);
		grid.setColumns("variable", "signalAmpere", "signalVoltage", "resolution", "mode", "maxInput", "sensorMinVariable", "sensorMaxVariable");
		return grid;
	}
}