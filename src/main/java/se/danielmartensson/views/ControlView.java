package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.AlarmLogg;
import se.danielmartensson.entities.CalibrationLogg;
import se.danielmartensson.entities.UserLogg;
import se.danielmartensson.pi4j.IO;
import se.danielmartensson.repositories.AlarmLoggRepository;
import se.danielmartensson.repositories.CalibrationLoggRepository;
import se.danielmartensson.repositories.DataLoggRepository;
import se.danielmartensson.repositories.UserLoggRepository;
import se.danielmartensson.threads.ControlThread;
import se.danielmartensson.threads.SamplingThread;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.Mail;
import se.danielmartensson.tools.PaperSlider;
import se.danielmartensson.tools.Top;

@Route("control")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Push
/**
 * This class handles the control view.
 * @author dell
 *
 */
public class ControlView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final String START_LOGGING = "Start logging";
	public static final String STOP_LOGGING = "Stop logging";
	public static final Integer MAX_SLIDER_VALE = 4095;
	public static final String VARIABLE_PROGRAM = "Variable program";
	public static final String PULSE_PROGRAM = "Pulse program";
	
	@Autowired
	private DataLoggRepository dataLoggRepository;
	
	@Autowired
	private UserLoggRepository userLoggRepository;
	
	@Autowired
	private CalibrationLoggRepository calibrationLoggRepository;
	
	@Autowired
	private AlarmLoggRepository alarmLoggRepository;
	
	@Autowired
	private Mail mail;
	
	@Autowired
	private IO io;
	
	@Value("${views.ControlView.adcAt4mAforAnalog0}")
	int adcAt4mAforAnalog0;
	
	@Value("${views.ControlView.adcAt4mAforAnalog1}")
	int adcAt4mAforAnalog1;
	
	@Value("${views.ControlView.adcAt4mAforAnalog2}")
	int adcAt4mAforAnalog2;
	
	@Value("${views.ControlView.adcAt4mAforAnalog3}")
	int adcAt4mAforAnalog3;
	
	
	/* Layout components */
	private ApexCharts apexChart;

	private PaperSlider do0Slider;
	
	private PaperSlider do1Slider;
	
	private PaperSlider do2Slider;
	
	private PaperSlider do3Slider;
	
	private IntegerField do0HighPulse;
	
	private IntegerField do0LowPulse;
	
	private IntegerField do1HighPulse;
	
	private IntegerField do1LowPulse;
		
	private IntegerField do2HighPulse;
	
	private IntegerField do2LowPulse;
	
	private IntegerField do3HighPulse;
	
	private IntegerField do3LowPulse;
	
	private Checkbox lowFirstDo0;
	
	private Checkbox lowFirstDo1;
	
	private Checkbox lowFirstDo2;
	
	private Checkbox lowFirstDo3;
	
	private Checkbox countOnHighSignal;
	
	private Button loggingActivate;
	
	private Select<Integer> showSamples;
	
	private Select<Integer> samplingTime;
	
	private Select<Long> alarm;
	
	private Select<Long> calibration;
	
	private Select<Long> loggerId;
	
	private Checkbox showPlot;
	
	private RadioButtonGroup<String> radioGroup;
	
	private IntegerField countedPulses;
	
	private IntegerField breakAtPulse;
	
	/* Layout for program */
	private HorizontalLayout thirdRow;


	/* 
	 * Static holders - They MUST be static because if you close web browser
	 * and open up again these components should remains
	 */
	static public int do0SliderSelected = 0;
	
	static public int do1SliderSelected = 0;
	
	static public int do2SliderSelected = 0;
	
	static public int do3SliderSelected = 0;

	static public boolean selectedShowPlot = false;

	static public int selectedShowSamples = 10; // Minimum of showSamples component

	static public int selectedSamplingTime = 100; // Minimum of samplingTime component

	static public long selectedAID = 0L;

	static public long selectedCID = 0L;

	static public long selectedLoggerId = 0L;
	
	static public int do0HighPulseSelected = 0;
	
	static public int do1HighPulseSelected = 0;
	
	static public int do2HighPulseSelected = 0;
	
	static public int do3HighPulseSelected = 0;
	
	static public String selectedProgram = VARIABLE_PROGRAM;
	
	static public int do0LowPulseSelected = 0;
	
	static public int do1LowPulseSelected = 0;
	
	static public int do2LowPulseSelected = 0;
	
	static public int do3LowPulseSelected = 0;
	
	static public boolean do0LowFirst = false;
	
	static public boolean do1LowFirst = false;
	
	static public boolean do2LowFirst = false;
	
	static public boolean do3LowFirst = false;
	
	static public boolean countPulseOnHighSignal = true;
	
	static public int selectedBreakPulseLimit = 1;
	
	static public AtomicBoolean loggingNow;

	static public ControlThread control;
	
	static public SamplingThread sampling;
	
	@PostConstruct
	public void init() {
		// Set the top and the tabs
		Top top = new Top();
		top.setTopAppLayout(this);
		
		if(loggingNow == null) {
			loggingNow = new AtomicBoolean();
		}
		
		// Apex chart
		apexChart = new Graf("Logging into MySQL").getApexChart();
		
		// Create sliders
		createSliders();
		
		// Create pulse list drop down boxes
		createPulseLists();
		
		thirdRow = new HorizontalLayout();

		// Create control panel
		createControlPanel();
		
		// Start the tread forcontrol
		if(control == null) {
			System.out.println(adcAt4mAforAnalog0);
			control = new ControlThread(io, adcAt4mAforAnalog0, adcAt4mAforAnalog1, adcAt4mAforAnalog2, adcAt4mAforAnalog3);
			control.start();
		}
		
		// Start the thread for sampling
		if(sampling == null) {
			sampling = new SamplingThread(dataLoggRepository, calibrationLoggRepository, alarmLoggRepository, userLoggRepository, mail);
			sampling.start();
		}
		// Set components to sampling thread - So we can disable and enable inside the thread
		sampling.setComponentsToThread(UI.getCurrent(),
									apexChart, 
									countedPulses,
									loggingActivate,
									calibration,
									alarm,
									loggerId,
									do0Slider,
									do1Slider,
									do2Slider,
									do3Slider,
									do0HighPulse,
									do1HighPulse,
									do2HighPulse,
									do3HighPulse,
									do0LowPulse,
									do1LowPulse,
									do2LowPulse,
									do3LowPulse,
									samplingTime,
									showSamples,
									showPlot,
									radioGroup,
									lowFirstDo0,
									lowFirstDo1,
									lowFirstDo2,
									lowFirstDo3,
									countOnHighSignal);
			
	}

	private void createPulseLists() {
		do0HighPulse = new IntegerField("High D0");
		do0HighPulse.setMin(0);
		do0HighPulse.setHasControls(true);
		do0HighPulse.setValue(do0HighPulseSelected);
		do0HighPulse.addValueChangeListener(e -> do0HighPulseSelected = e.getValue());
		
		do0LowPulse = new IntegerField("Low D0");
		do0LowPulse.setMin(0);
		do0LowPulse.setHasControls(true);
		do0LowPulse.setValue(do0LowPulseSelected);
		do0LowPulse.addValueChangeListener(e -> do0LowPulseSelected = e.getValue());
		
		lowFirstDo0 = new Checkbox(do0LowFirst);
		lowFirstDo0.setLabel("Low first");
		lowFirstDo0.addValueChangeListener(e -> do0LowFirst = e.getValue());
		
		do1HighPulse = new IntegerField("High D1");
		do1HighPulse.setMin(0);
		do1HighPulse.setHasControls(true);
		do1HighPulse.setValue(do1HighPulseSelected);
		do1HighPulse.addValueChangeListener(e -> do1HighPulseSelected = e.getValue());
		
		do1LowPulse = new IntegerField("Low D1");
		do1LowPulse.setMin(0);
		do1LowPulse.setHasControls(true);
		do1LowPulse.setValue(do1LowPulseSelected);
		do1LowPulse.addValueChangeListener(e -> do1LowPulseSelected = e.getValue());
		
		lowFirstDo1 = new Checkbox(do1LowFirst);
		lowFirstDo1.setLabel("Low first");
		lowFirstDo1.addValueChangeListener(e -> do1LowFirst = e.getValue());
		
		do2HighPulse = new IntegerField("High D2");
		do2HighPulse.setMin(0);
		do2HighPulse.setHasControls(true);
		do2HighPulse.setValue(do2HighPulseSelected);
		do2HighPulse.addValueChangeListener(e -> do1HighPulseSelected = e.getValue());
		
		do2LowPulse = new IntegerField("Low D2");
		do2LowPulse.setMin(0);
		do2LowPulse.setHasControls(true);
		do2LowPulse.setValue(do2LowPulseSelected);
		do2LowPulse.addValueChangeListener(e -> do2LowPulseSelected = e.getValue());
		
		lowFirstDo2 = new Checkbox(do2LowFirst);
		lowFirstDo2.setLabel("Low first");
		lowFirstDo2.addValueChangeListener(e -> do2LowFirst = e.getValue());
		
		do3HighPulse = new IntegerField("High D3");
		do3HighPulse.setMin(0);
		do3HighPulse.setHasControls(true);
		do3HighPulse.setValue(do3HighPulseSelected);
		do3HighPulse.addValueChangeListener(e -> do3HighPulseSelected = e.getValue());
		
		do3LowPulse = new IntegerField("Low D3");
		do3LowPulse.setMin(0);
		do3LowPulse.setHasControls(true);
		do3LowPulse.setValue(do3LowPulseSelected);
		do3LowPulse.addValueChangeListener(e -> do3LowPulseSelected = e.getValue());
		
		lowFirstDo3 = new Checkbox(do3LowFirst);
		lowFirstDo3.setLabel("Low first");
		lowFirstDo3.addValueChangeListener(e -> do3LowFirst = e.getValue());
		
		// Pulse counters
		countedPulses = new IntegerField("Counted pulses");
		countedPulses.setEnabled(false);
		breakAtPulse = new IntegerField("Break at pulse");
		breakAtPulse.setMin(1);
		breakAtPulse.setHasControls(true);
		breakAtPulse.setValue(selectedBreakPulseLimit);
		breakAtPulse.addValueChangeListener(e -> selectedBreakPulseLimit = e.getValue());
		countOnHighSignal = new Checkbox(countPulseOnHighSignal);
		countOnHighSignal.setLabel("High trigg");
		countOnHighSignal.addValueChangeListener(e -> countPulseOnHighSignal = e.getValue());
		
	}

	private void createSliders() {
		do0Slider = new PaperSlider(MAX_SLIDER_VALE);
		do0Slider.setEnabled(false); // Important to set these to disabled at first
		do0Slider.addValueChangeListener(e -> do0SliderSelected = e.getValue());
		do0Slider.setValue(do0SliderSelected);
		
		do1Slider = new PaperSlider(MAX_SLIDER_VALE);
		do1Slider.setEnabled(false);
		do1Slider.addValueChangeListener(e -> do1SliderSelected = e.getValue());
		do1Slider.setValue(do1SliderSelected);
		
		do2Slider = new PaperSlider(MAX_SLIDER_VALE);
		do2Slider.setValue(do2SliderSelected);
		do2Slider.setEnabled(false);
		do2Slider.addValueChangeListener(e -> do2SliderSelected = e.getValue());
		
		do3Slider = new PaperSlider(MAX_SLIDER_VALE);
		do3Slider.setValue(do3SliderSelected);
		do3Slider.setEnabled(false);
		do3Slider.addValueChangeListener(e -> do3SliderSelected = e.getValue());
	}

	private void createControlPanel() {
		
		HorizontalLayout firstRow = createFirstRow();
		HorizontalLayout secondRow = createSecondRow();
		createThirdRow(); // Special case
		HorizontalLayout fourthRow = createFourthRow();
		fourthRow.setAlignItems(Alignment.CENTER);
		
		// Content
		setContent(new VerticalLayout(firstRow, secondRow, thirdRow, fourthRow, apexChart));
	}

	private HorizontalLayout createFourthRow() {
		// Check box if we want to show the plot or not
		showPlot = new Checkbox("Show plot");
		showPlot.setValue(selectedShowPlot);
		showPlot.addValueChangeListener(e -> selectedShowPlot = e.getValue());
				
		// Start and stop button for logging
		loggingActivate = new Button(START_LOGGING);
		loggingActivate.addClickListener(e -> {
			
			// Check if we have missed something
			try {
				if(calibration.getValue() == 0 || alarm.getValue() == 0 || loggerId.getValue() == 0) {}
			}catch(NullPointerException e1) {
				new Notification("Forgot selecting something?", 3000).open();
				return;
			}
						
			// Flipp the flag
			if(loggingNow.get() == true) {
				loggingNow.set(false);
			}else { 
				loggingNow.set(true);
			}
		});
				
		return new HorizontalLayout(showPlot, loggingActivate);
	}

	private HorizontalLayout createSecondRow() {
		// Radio button for selecting program
		radioGroup = new RadioButtonGroup<>();
		radioGroup.setLabel("Program:");
		radioGroup.setItems(VARIABLE_PROGRAM, PULSE_PROGRAM);
		radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup.setValue(selectedProgram);
		radioGroup.addValueChangeListener(e -> {
			selectedProgram = e.getValue();
			createThirdRow();
		});
				
		return new HorizontalLayout(radioGroup);
	}

	private HorizontalLayout createFirstRow() {
		// Set the logger ids
		List<UserLogg> userLoggers = userLoggRepository.findAll();
		loggerId = new Select<>();
		loggerId.setLabel("Logger ID");
		ArrayList<Long> loggerIds = new ArrayList<Long>();
		for(int i = 0; i < userLoggers.size(); i++) {
			loggerIds.add(userLoggers.get(i).getLoggerId());
		}
		loggerId.setItems(loggerIds);
		loggerId.setPlaceholder("No id");
		if(selectedLoggerId > 0)
			loggerId.setValue(selectedLoggerId);
		loggerId.addValueChangeListener(e -> selectedLoggerId = e.getValue());
				
		// Set the calibration id
		List<CalibrationLogg> calibrationsLoggers = calibrationLoggRepository.findAll();
		calibration = new Select<>();
		calibration.setLabel("Calibration ID");
		ArrayList<Long> CIDs = new ArrayList<Long>();
		for(int i = 0; i < calibrationsLoggers.size(); i++) {
			CIDs.add(calibrationsLoggers.get(i).getCID());
		}
		calibration.setItems(CIDs);
		calibration.setPlaceholder("No id");
		if(selectedCID > 0)
			calibration.setValue(selectedCID);
		calibration.addValueChangeListener(e -> selectedCID = e.getValue());
				
		// Set the alarm id
		List<AlarmLogg> alarmsLoggers = alarmLoggRepository.findAll();
		alarm = new Select<>();
		alarm.setLabel("Alarm ID");
		ArrayList<Long> AIDs = new ArrayList<Long>();
		for(int i = 0; i < alarmsLoggers.size(); i++) {
			AIDs.add(alarmsLoggers.get(i).getAID());
		}
		alarm.setItems(AIDs);
		alarm.setPlaceholder("No id");
		if(selectedAID > 0)
			alarm.setValue(selectedAID);
		alarm.addValueChangeListener(e -> selectedAID = e.getValue());
				
		// Sampling time for the thread
		samplingTime = new Select<Integer>(new Integer[] {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 10000, 20000, 30000, 40000, 50000, 60000});
		samplingTime.setLabel("Sampling time");
		samplingTime.setValue(selectedSamplingTime);
		samplingTime.addValueChangeListener(e -> selectedSamplingTime = e.getValue());
				
		// Show amount of samples at the plot
		showSamples = new Select<Integer>(new Integer[] {10, 20, 30, 40, 50});
		showSamples.setLabel("Show samples");
		showSamples.setValue(selectedShowSamples);
		showSamples.addValueChangeListener(e -> selectedShowSamples = e.getValue());
				
		return new HorizontalLayout(loggerId, calibration, alarm, showSamples, samplingTime);
				
	}

	private void createThirdRow() {
		if(selectedProgram.equals(VARIABLE_PROGRAM)) {
			thirdRow = createVariableLayout();
		}else {
			thirdRow = createPulseLayout();
		}
	}

	private HorizontalLayout createPulseLayout() {
		// Pulse layout 0
		VerticalLayout do0PulseLayout = new VerticalLayout(new Label("DO0"), do0HighPulse, do0LowPulse, lowFirstDo0);
		do0PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 1
		VerticalLayout do1PulseLayout = new VerticalLayout(new Label("DO1"), do1HighPulse, do1LowPulse, lowFirstDo1);
		do1PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 2
		VerticalLayout do2PulseLayout = new VerticalLayout(new Label("DO2"), do2HighPulse, do2LowPulse, lowFirstDo2);
		do2PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 3
		VerticalLayout do3PulseLayout = new VerticalLayout(new Label("DO3"), do3HighPulse, do3LowPulse, lowFirstDo3);
		do3PulseLayout.setAlignItems(Alignment.CENTER);
		
		// Pulse counters
		VerticalLayout pulseCounters = new VerticalLayout(new Label("Pulses"), countedPulses, breakAtPulse, countOnHighSignal);
		pulseCounters.setAlignItems(Alignment.CENTER); 
		
		// Layout
		thirdRow.removeAll();
		thirdRow.add(do0PulseLayout, do1PulseLayout, do2PulseLayout, do3PulseLayout, pulseCounters);
		thirdRow.setAlignItems(Alignment.CENTER);
		return thirdRow;
	}

	private HorizontalLayout createVariableLayout() {
		// Slider layout 0
		VerticalLayout do0SliderLayout = new VerticalLayout(new Label("DO0"), do0Slider);
		do0SliderLayout.setAlignItems(Alignment.CENTER);

		// Slider layout 1
		VerticalLayout do1SliderLayout = new VerticalLayout(new Label("DO1"), do1Slider);
		do1SliderLayout.setAlignItems(Alignment.CENTER);

		// Slider layout 2
		VerticalLayout do2SliderLayout = new VerticalLayout(new Label("DO2"), do2Slider);
		do2SliderLayout.setAlignItems(Alignment.CENTER);

		// Slider layout 3
		VerticalLayout do3SliderLayout = new VerticalLayout(new Label("DO3"), do3Slider);
		do3SliderLayout.setAlignItems(Alignment.CENTER);

		// Layout
		thirdRow.removeAll();
		thirdRow.add(do0SliderLayout, do1SliderLayout, do2SliderLayout, do3SliderLayout);
		thirdRow.setAlignItems(Alignment.CENTER);
		return thirdRow;
	}
}
