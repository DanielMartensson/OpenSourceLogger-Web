package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

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

import se.danielmartensson.pi4j.IO;
import se.danielmartensson.views.components.Graf;
import se.danielmartensson.views.components.Mail;
import se.danielmartensson.views.components.PaperSlider;
import se.danielmartensson.views.components.threads.ControlThread;
import se.danielmartensson.views.components.threads.SamplingThread;
import se.danielmartensson.views.database.alarmlogg.AlarmLogg;
import se.danielmartensson.views.database.alarmlogg.AlarmLoggRepository;
import se.danielmartensson.views.database.calibrationlogg.CalibrationLogg;
import se.danielmartensson.views.database.calibrationlogg.CalibrationLoggRepository;
import se.danielmartensson.views.database.datalogg.DataLoggRepository;
import se.danielmartensson.views.database.userlogg.UserLogg;
import se.danielmartensson.views.database.userlogg.UserLoggRepository;
import se.danielmartensson.views.templates.Top;

@Route("control")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Push
public class ControlView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final String START_LOGGING = "Start logging";
	public static final String STOP_LOGGING = "Stop logging";
	public static final Integer MAX_SLIDER_VALE = 4095;
	
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
	
	/* Layout components */

	private ApexCharts apexChart;

	private PaperSlider do0Slider;
	
	private PaperSlider do1Slider;
	
	private PaperSlider do2Slider;
	
	private PaperSlider do3Slider;
	
	private IntegerField do0Pulse;
	
	private IntegerField do0Period;
	
	private IntegerField do1Pulse;
	
	private IntegerField do1Period;
		
	private IntegerField do2Pulse;
	
	private IntegerField do2Period;
	
	private IntegerField do3Pulse;
	
	private IntegerField do3Period;
	
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

	/* Static holders */
	
	static public int do0SliderSelected = 0;
	
	static public int do1SliderSelected = 0;
	
	static public int do2SliderSelected = 0;
	
	static public int do3SliderSelected = 0;

	static public boolean selectedShowPlot = false;

	static public int selectedShowSamples = 10; // Minimum

	static public int selectedSamplingTime = 10; // Minimum

	static public long selectedAID = 0L;

	static public long selectedCID = 0L;

	static public long selectedLoggerId = 0L;
	
	static public int do0PulseSelected = 0;
	
	static public int do1PulseSelected = 0;
	
	static public int do2PulseSelected = 0;
	
	static public int do3PulseSelected = 0;
	
	static public String selectedProgram = "Variable";
	
	static public int do0PeriodSelected = 0;
	
	static public int do1PeriodSelected = 0;
	
	static public int do2PeriodSelected = 0;
	
	static public int do3PeriodSelected = 0;
	
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
			control = new ControlThread(io);
			control.start();
		}
		
		// Start the thread for sampling
		if(sampling == null) {
			sampling = new SamplingThread(dataLoggRepository, calibrationLoggRepository, alarmLoggRepository, mail);
			sampling.start();
		}
		// Set components to sampling thread
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
									do0Pulse,
									do1Pulse,
									do2Pulse,
									do3Pulse,
									do0Period,
									do1Period,
									do2Period,
									do3Period,
									samplingTime,
									showSamples,
									showPlot,
									radioGroup);
			
	}

	private void createPulseLists() {
		do0Pulse = new IntegerField("Activated D0");
		do0Pulse.setMin(-100);
		do0Pulse.setMax(100);
		do0Pulse.setHasControls(true);
		do0Pulse.setValue(do0PulseSelected);
		do0Pulse.addValueChangeListener(e -> do0PulseSelected = e.getValue());
		
		do0Period = new IntegerField("Count D0");
		do0Period.setMin(0);
		do0Period.setHasControls(true);
		do0Period.setValue(do0PeriodSelected);
		do0Period.addValueChangeListener(e -> do0PeriodSelected = e.getValue());
		
		do1Pulse = new IntegerField("Activated D1");
		do1Pulse.setMin(-100);
		do1Pulse.setMax(100);
		do1Pulse.setHasControls(true);
		do1Pulse.setValue(do1PulseSelected);
		do1Pulse.addValueChangeListener(e -> do1PulseSelected = e.getValue());
		
		do1Period = new IntegerField("Count D1");
		do1Period.setMin(0);
		do1Period.setHasControls(true);
		do1Period.setValue(do1PeriodSelected);
		do1Period.addValueChangeListener(e -> do1PeriodSelected = e.getValue());
		
		do2Pulse = new IntegerField("Activated D2");
		do2Pulse.setMin(-100);
		do2Pulse.setMax(100);
		do2Pulse.setHasControls(true);
		do2Pulse.setValue(do2PulseSelected);
		do2Pulse.addValueChangeListener(e -> do2PulseSelected = e.getValue());
		
		do2Period = new IntegerField("Count D2");
		do2Period.setMin(0);
		do2Period.setHasControls(true);
		do2Period.setValue(do2PeriodSelected);
		do2Period.addValueChangeListener(e -> do2PeriodSelected = e.getValue());
		
		do3Pulse = new IntegerField("Activated D3");
		do3Pulse.setMin(-100);
		do3Pulse.setMax(100);
		do3Pulse.setHasControls(true);
		do3Pulse.setValue(do3PulseSelected);
		do3Pulse.addValueChangeListener(e -> do3PulseSelected = e.getValue());
		
		do3Period = new IntegerField("Count D3");
		do3Period.setMin(0);
		do3Period.setHasControls(true);
		do3Period.setValue(do3PeriodSelected);
		do3Period.addValueChangeListener(e -> do3PeriodSelected = e.getValue());
		
		// Pulse counters
		countedPulses = new IntegerField("Counted pulses");
		countedPulses.setEnabled(false);
		breakAtPulse = new IntegerField("Break at pulse");
		breakAtPulse.setMin(1);
		breakAtPulse.setHasControls(true);
		breakAtPulse.setValue(selectedBreakPulseLimit);
		breakAtPulse.addValueChangeListener(e -> selectedBreakPulseLimit = e.getValue());
		
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
			
			if(loggingNow.get() == true) 
				loggingNow.set(false);
			else 
				loggingNow.set(true);
		});
				
		return new HorizontalLayout(showPlot, loggingActivate);
	}

	private HorizontalLayout createSecondRow() {
		// Radio button for selecting program
		radioGroup = new RadioButtonGroup<>();
		radioGroup.setLabel("Program:");
		radioGroup.setItems("Variable", "Pulse");
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
		samplingTime = new Select<Integer>(new Integer[] {10, 15, 20, 25, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000});
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
		if(selectedProgram.equals("Variable")) {
			thirdRow = createVariableLayout();
		}else {
			thirdRow = createPulseLayout();
		}
	}

	private HorizontalLayout createPulseLayout() {
		// Pulse layout 0
		VerticalLayout do0PulseLayout = new VerticalLayout(new Label("DO0"), do0Pulse, do0Period);
		do0PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 1
		VerticalLayout do1PulseLayout = new VerticalLayout(new Label("DO1"), do1Pulse, do1Period);
		do1PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 2
		VerticalLayout do2PulseLayout = new VerticalLayout(new Label("DO2"), do2Pulse, do2Period);
		do2PulseLayout.setAlignItems(Alignment.CENTER);

		// Pulse layout 3
		VerticalLayout do3PulseLayout = new VerticalLayout(new Label("DO3"), do3Pulse, do3Period);
		do3PulseLayout.setAlignItems(Alignment.CENTER);
		
		// Pulse counters
		VerticalLayout pulseCounters = new VerticalLayout(new Label("Pulses"), countedPulses, breakAtPulse);
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
