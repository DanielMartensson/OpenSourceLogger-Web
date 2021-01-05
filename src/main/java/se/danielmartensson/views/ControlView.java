package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

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
import com.vaadin.flow.router.Route;

import lombok.Getter;
import se.danielmartensson.entities.Job;
import se.danielmartensson.hardware.Serial;
import se.danielmartensson.service.AlarmService;
import se.danielmartensson.service.DataService;
import se.danielmartensson.service.JobService;
import se.danielmartensson.service.MailService;
import se.danielmartensson.threads.ControlThread;
import se.danielmartensson.threads.SamplingThread;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.PaperSlider;
import se.danielmartensson.tools.Top;

@Route("control")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Push
/**
 * This class handles the control view.
 * 
 * @author dell
 *
 */
@Getter
@PropertySource("classpath:application.properties")
public class ControlView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String START_LOGGING = "Start logging";
	public static final String STOP_LOGGING = "Stop logging";
	public static final Integer MAX_SLIDER_VALE = 4095;

	// Button that the thread need to have access to
	private Button loggingActivate;

	// Services
	private JobService jobService;
	private MailService mailService;
	private DataService dataService;
	private AlarmService alarmService;

	// Static for the UI - Also initial values
	public static Job selectedJob;
	public static int selectedSamplingTime = 10;
	public static int selectedShowSamples = 10;
	public static boolean selectedShowPlot;
	public static int sliderSelectedP0 = 0;
	public static int sliderSelectedP1 = 0;
	public static int sliderSelectedP2 = 0;
	public static int sliderSelectedP3 = 0;
	public static int sliderSelectedP4 = 0;
	public static int sliderSelectedP5 = 0;
	public static int sliderSelectedP6 = 0;
	public static int sliderSelectedP7 = 0;
	public static int sliderSelectedP8 = 0;
	public static int sliderSelectedD0 = 0;
	public static int sliderSelectedD1 = 0;
	public static int sliderSelectedD2 = 0;
	public static int selectedBreakPulseLimit = 1;
	private static int selectBeginCountAt = 0;

	// For the thread
	public static AtomicBoolean loggingNow;
	public static ControlThread control;
	public static SamplingThread sampling;
	@Autowired
	private Serial serial;

	@PostConstruct
	public void init() {
		// Set the top and the tabs
		Top top = new Top();
		top.setTopAppLayout(this);

		// Apex chart
		ApexCharts apexChart = new Graf("Logging into MySQL").getApexChart();

		// Create PWM sliders
		List<PaperSlider> PWMSliders = createPWMSliders();

		// Create DAC sliders
		List<PaperSlider> DACSliders = createDACSliders();

		// Create pulse list drop down boxes
		List<IntegerField> counters = createPulseCounter();

		// Create check boxes for digital input signals
		Checkbox[] inputBoxes = createCheckBoxes(new String[] {"I0 - Counter", "I1 - Stop signal", "I2", "I3", "I4", "I5"});
		
		// Create check boxes for showing the measurement series
		Checkbox[] seriesBoxes = createCheckBoxes(new String[] {"A0", "A1", "A2", "A3", "SA0", "SA1", "SA1D", "SA2D", "SA3D"});

		// Create control panel
		HorizontalLayout firstRow = createFirstRow(jobService, counters);
		HorizontalLayout secondRow = createVariableLayout(PWMSliders, "P", 0, 5);
		HorizontalLayout thirdRow = createVariableLayout(PWMSliders, "P", 5, 9);
		HorizontalLayout fourthRow = createVariableLayout(DACSliders, "D", 0, 3);
		HorizontalLayout fifthRow = createCheckBoxesLayout(inputBoxes);
		HorizontalLayout sixthRow = createCheckBoxesLayout(seriesBoxes);
		HorizontalLayout seventhRow = createStartButtonAndShowPlotButton(jobService);

		// Start the tread for control
		if (control == null) {
			loggingNow = new AtomicBoolean();
			control = new ControlThread(serial);
			control.start();
		}

		// Start the thread for sampling
		if (sampling == null) {
			sampling = new SamplingThread(mailService, dataService);
			sampling.start();
		}
		// Set components to sampling thread - So we can disable and enable inside the
		// thread
		sampling.setComponentsToThread(UI.getCurrent(), firstRow, loggingActivate, apexChart, counters, inputBoxes, seriesBoxes);

		// Content
		setContent(new VerticalLayout(firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow, seventhRow, apexChart));
	}

	public ControlView(JobService jobService, MailService mailService, DataService dataService) {
		this.jobService = jobService;
		this.mailService = mailService;
		this.dataService = dataService;
	}

	private HorizontalLayout createCheckBoxesLayout(Checkbox[] checkBoxes) {
		HorizontalLayout checkBoxLayout = new HorizontalLayout();
		for (Checkbox checkbox : checkBoxes)
			checkBoxLayout.add(checkbox);
		checkBoxLayout.setAlignItems(Alignment.CENTER);
		return checkBoxLayout;
	}

	private Checkbox[] createCheckBoxes(String[] labels) {
		Checkbox[] list = new Checkbox[labels.length];
		for(int i = 0; i < labels.length; i++)
			list[i] = new Checkbox(labels[i]);
		return list;
	}

	private List<PaperSlider> createDACSliders() {
		List<PaperSlider> list = new ArrayList<PaperSlider>();
		PaperSlider slider0 = new PaperSlider(MAX_SLIDER_VALE);
		slider0.setValue(sliderSelectedD0);
		slider0.addValueChangeListener(e -> sliderSelectedD0 = e.getValue());
		list.add(slider0);
		PaperSlider slider1 = new PaperSlider(MAX_SLIDER_VALE);
		slider1.setValue(sliderSelectedD1);
		slider1.addValueChangeListener(e -> sliderSelectedD1 = e.getValue());
		list.add(slider1);
		PaperSlider slider2 = new PaperSlider(MAX_SLIDER_VALE);
		slider2.setValue(sliderSelectedD2);
		slider2.addValueChangeListener(e -> sliderSelectedD2 = e.getValue());
		list.add(slider2);
		return list;
	}

	private List<IntegerField> createPulseCounter() {
		// Pulse counter
		IntegerField countedPulses = new IntegerField("Begin count at");
		countedPulses.setMin(0);
		countedPulses.setValue(selectBeginCountAt);
		countedPulses.addValueChangeListener(e -> selectBeginCountAt = e.getValue());
		IntegerField breakAtPulse = new IntegerField("Break at pulse");
		breakAtPulse.setMin(1);
		breakAtPulse.setValue(selectedBreakPulseLimit);
		breakAtPulse.addValueChangeListener(e -> selectedBreakPulseLimit = e.getValue());
		List<IntegerField> list = new ArrayList<IntegerField>();
		list.add(countedPulses);
		list.add(breakAtPulse);
		return list;
	}

	private List<PaperSlider> createPWMSliders() {
		List<PaperSlider> list = new ArrayList<PaperSlider>();
		PaperSlider slider0 = new PaperSlider(MAX_SLIDER_VALE);
		slider0.setValue(sliderSelectedP0);
		slider0.addValueChangeListener(e -> sliderSelectedP0 = e.getValue());
		list.add(slider0);
		PaperSlider slider1 = new PaperSlider(MAX_SLIDER_VALE);
		slider1.setValue(sliderSelectedP1);
		slider1.addValueChangeListener(e -> sliderSelectedP1 = e.getValue());
		list.add(slider1);
		PaperSlider slider2 = new PaperSlider(MAX_SLIDER_VALE);
		slider2.setValue(sliderSelectedP2);
		slider2.addValueChangeListener(e -> sliderSelectedP2 = e.getValue());
		list.add(slider2);
		PaperSlider slider3 = new PaperSlider(MAX_SLIDER_VALE);
		slider3.setValue(sliderSelectedP3);
		slider3.addValueChangeListener(e -> sliderSelectedP3 = e.getValue());
		list.add(slider3);
		PaperSlider slider4 = new PaperSlider(MAX_SLIDER_VALE);
		slider4.setValue(sliderSelectedP4);
		slider4.addValueChangeListener(e -> sliderSelectedP4 = e.getValue());
		list.add(slider4);
		PaperSlider slider5 = new PaperSlider(MAX_SLIDER_VALE);
		slider5.setValue(sliderSelectedP5);
		slider5.addValueChangeListener(e -> sliderSelectedP5 = e.getValue());
		list.add(slider5);
		PaperSlider slider6 = new PaperSlider(MAX_SLIDER_VALE);
		slider6.setValue(sliderSelectedP6);
		slider6.addValueChangeListener(e -> sliderSelectedP6 = e.getValue());
		list.add(slider6);
		PaperSlider slider7 = new PaperSlider(MAX_SLIDER_VALE);
		slider7.setValue(sliderSelectedP7);
		slider7.addValueChangeListener(e -> sliderSelectedP7 = e.getValue());
		list.add(slider7);
		PaperSlider slider8 = new PaperSlider(MAX_SLIDER_VALE);
		slider8.setValue(sliderSelectedP8);
		slider8.addValueChangeListener(e -> sliderSelectedP8 = e.getValue());
		list.add(slider8);
		return list;
	}

	private HorizontalLayout createStartButtonAndShowPlotButton(JobService jobService) {
		// Check box if we want to show the plot or not
		Checkbox showPlot = new Checkbox("Show plot");
		showPlot.setValue(selectedShowPlot);
		showPlot.addValueChangeListener(e -> selectedShowPlot = e.getValue());

		// Start and stop button for logging
		loggingActivate = new Button(START_LOGGING);
		loggingActivate.addClickListener(e -> {
			// Check if we have selected right job
			if (selectedJob == null)
				return;
			if (!jobService.existsByName(selectedJob.getName())) {
				new Notification("This job does not exist. Select another one", 3000).open();
				return;
			}
			if (loggingNow.get() == true) {
				loggingNow.set(false);
			} else {
				loggingNow.set(true);
			}
		});

		return new HorizontalLayout(showPlot, loggingActivate);
	}

	private HorizontalLayout createFirstRow(JobService jobService, List<IntegerField> counters) {
		// Set job names
		Select<Job> selectJob = new Select<Job>();
		selectJob.setLabel("Job name");
		List<Job> jobs = jobService.findAll();
		selectJob.setItems(jobs);
		if (selectedJob != null) {
			for(Job job : jobs) {
				if(job.getName().equals(selectedJob.getName())) {
					selectedJob = job;
					selectJob.setValue(job);
				}
			}
		}
		selectJob.addValueChangeListener(e -> selectedJob = e.getValue());

		// Sampling time for the thread
		Select<Integer> samplingTime = new Select<Integer>(new Integer[] {10, 25, 50, 75, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 10000, 20000, 30000, 40000, 50000, 60000 });
		samplingTime.setLabel("Sampling time");
		samplingTime.setValue(selectedSamplingTime);
		samplingTime.addValueChangeListener(e -> selectedSamplingTime = e.getValue());

		// Show amount of samples at the plot
		Select<Integer> showSamples = new Select<Integer>(new Integer[] { 10, 20, 30, 40, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 600, 700, 800, 900, 1000 });
		showSamples.setLabel("Show samples");
		showSamples.setValue(selectedShowSamples);
		showSamples.addValueChangeListener(e -> selectedShowSamples = e.getValue());

		return new HorizontalLayout(selectJob, showSamples, samplingTime, counters.get(0), counters.get(1));

	}

	private HorizontalLayout createVariableLayout(List<PaperSlider> sliders, String label, int start, int stop) {
		HorizontalLayout variableLayout = new HorizontalLayout();
		for (int i = start; i < stop; i++) {
			VerticalLayout sliderLayout = new VerticalLayout(new Label(label + i), sliders.get(i));
			sliderLayout.setAlignItems(Alignment.CENTER);
			variableLayout.add(sliderLayout);
		}
		variableLayout.setAlignItems(Alignment.CENTER);
		return variableLayout;
	}
}
