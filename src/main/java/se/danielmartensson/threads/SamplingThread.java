package se.danielmartensson.threads;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;

import se.danielmartensson.entities.AlarmLogg;
import se.danielmartensson.entities.CalibrationLogg;
import se.danielmartensson.entities.DataLogg;
import se.danielmartensson.entities.UserLogg;
import se.danielmartensson.repositories.AlarmLoggRepository;
import se.danielmartensson.repositories.CalibrationLoggRepository;
import se.danielmartensson.repositories.DataLoggRepository;
import se.danielmartensson.repositories.UserLoggRepository;
import se.danielmartensson.tools.Mail;
import se.danielmartensson.tools.PaperSlider;
import se.danielmartensson.views.ControlView;
import se.danielmartensson.views.MySQLView;

/**
 * This class plot the values from the ControlTread.java class and also check their thresholds 
 * @author dell
 *
 */
public class SamplingThread extends Thread{
	// For the settings
	private UI ui;
	private DataLoggRepository dataLoggRepository;
	private CalibrationLoggRepository calibrationLoggRepository;
	private AlarmLoggRepository alarmLoggRepository;
	private UserLoggRepository userLoggRepository;
	private Mail mail;

	// For logging values
	private Float[] dataAI0;
	private Float[] dataAI1;
	private Float[] dataAI2;
	private Float[] dataAI3;
	private Float[] dataDO0;
	private Float[] dataDO1;
	private Float[] dataDO2;
	private Float[] dataDO3;
	private static boolean pastPulse;
	static private int pulseNumber;
	
	// Connection to the view
	private ApexCharts apexChart;
	private IntegerField countedPulses;
	private Button loggingActivate;
	private Select<Long> calibration;
	private Select<Long> alarm;
	private Select<Long> loggerId;
	private PaperSlider do0Slider;
	private PaperSlider do1Slider;
	private PaperSlider do2Slider;
	private PaperSlider do3Slider;
	private IntegerField do0HighPulse;
	private IntegerField do1HighPulse;
	private IntegerField do2HighPulse;
	private IntegerField do3HighPulse;
	private IntegerField do0LowPulse;
	private IntegerField do1LowPulse;
	private IntegerField do2LowPulse;
	private IntegerField do3LowPulse;
	private Select<Integer> samplingTime;
	private Select<Integer> showSamples;
	private Checkbox showPlot;
	private RadioButtonGroup<String> radioGroup;
	private Checkbox lowFirstDo0;
	private Checkbox lowFirstDo1;
	private Checkbox lowFirstDo2;
	private Checkbox lowFirstDo3;
	private Checkbox countOnHighSignal;


	public SamplingThread(DataLoggRepository dataLoggRepository, CalibrationLoggRepository calibrationLoggRepository, AlarmLoggRepository alarmLoggRepository, UserLoggRepository userLoggRepository, Mail mail) {
		this.dataLoggRepository = dataLoggRepository;
		this.calibrationLoggRepository = calibrationLoggRepository;
		this.alarmLoggRepository = alarmLoggRepository;
		this.userLoggRepository = userLoggRepository;
		this.mail = mail;
	}
	
	@Override
	public void run() {
		while(true) {			
			// Wait loop
			while(ControlView.loggingNow.get() == false) {
				try {
					Thread.sleep(1000); 
				} catch (InterruptedException e) {}
			}
			
			// Series for the plot and restore the plot and counting
			int showSamplesValue = ControlView.selectedShowSamples;
			long loggerIdValue = ControlView.selectedLoggerId;
			int samplingTimeValue = ControlView.selectedSamplingTime;
			boolean countPulseOnHighSignal = ControlView.countPulseOnHighSignal;
			dataAI0 = new Float[showSamplesValue];
			dataAI1 = new Float[showSamplesValue];
			dataAI2 = new Float[showSamplesValue];
			dataAI3 = new Float[showSamplesValue];
			dataDO0 = new Float[showSamplesValue];
			dataDO1 = new Float[showSamplesValue];
			dataDO2 = new Float[showSamplesValue];
			dataDO3 = new Float[showSamplesValue];
			
			// Reset
			pulseNumber = 0; 
			pastPulse = false; 
			updatePlotAndPulse();
			
			// Get the calibration and alarm and comment from userlogg
			CalibrationLogg calibrationLogg = calibrationLoggRepository.findByCID(ControlView.selectedCID);			
			float BAI0 = calibrationLogg.getBAI0();
			float SAI0 = calibrationLogg.getSAI0();
			float BAI1 = calibrationLogg.getBAI1();
			float SAI1 = calibrationLogg.getSAI0();
			float BAI2 = calibrationLogg.getBAI2();
			float SAI2 = calibrationLogg.getSAI0();
			float BAI3 = calibrationLogg.getBAI3();
			float SAI3 = calibrationLogg.getSAI0();
			AlarmLogg alarmLogg = alarmLoggRepository.findByAID(ControlView.selectedAID);
			float AI0Max = alarmLogg.getAI0Max();
			float AI0Min = alarmLogg.getAI0Min();
			float AI1Max = alarmLogg.getAI1Max();
			float AI1Min = alarmLogg.getAI1Min();
			float AI2Max = alarmLogg.getAI2Max();
			float AI2Min = alarmLogg.getAI2Min();
			float AI3Max = alarmLogg.getAI3Max();
			float AI3Min = alarmLogg.getAI3Min();
			String email = alarmLogg.getEmail();
			boolean alarmActivated = alarmLogg.isAlarm();
			UserLogg userLogg = userLoggRepository.findByLoggerId(ControlView.selectedLoggerId);
			String comment = userLogg.getComment();
			
			// Sampling loop
			while(ControlView.loggingNow.get() == true) {
				// Command signals to the device
				int DO0 = ControlThread.do0;
				int DO1 = ControlThread.do1;
				int DO2 = ControlThread.do2;
				int DO3 = ControlThread.do3;
				
				// Read the AI signals and pulse signal
				float AI0 = SAI0*ControlThread.ai0 + BAI0;
				float AI1 = SAI1*ControlThread.ai1 + BAI1;
				float AI2 = SAI2*ControlThread.ai2 + BAI2;
				float AI3 = SAI3*ControlThread.ai3 + BAI3;
				
				// Read the pulse signal and count it if...
				if((ControlThread.pulse != pastPulse) && ControlThread.pulse == countPulseOnHighSignal)
					pulseNumber++;
				pastPulse = ControlThread.pulse;
				
				// Read the alarm signal
				boolean stopSignal = ControlThread.stopSignal;
				
				// Save them to the database
				DataLogg dataLogg = new DataLogg(0, LocalDateTime.now(), DO0, DO1, DO2, DO3, AI0, AI1, AI2, AI3, loggerIdValue, samplingTimeValue, pulseNumber, ControlView.selectedBreakPulseLimit, stopSignal, comment);
				dataLoggRepository.save(dataLogg);
				
				// Show the values on the plot - First shift it back 1 step, set the last element and update the plot
				if(ControlView.selectedShowPlot == true) {
					Collections.rotate(Arrays.asList(dataAI0), -1);
					Collections.rotate(Arrays.asList(dataAI1), -1);
					Collections.rotate(Arrays.asList(dataAI2), -1);
					Collections.rotate(Arrays.asList(dataAI3), -1);
					Collections.rotate(Arrays.asList(dataDO0), -1);
					Collections.rotate(Arrays.asList(dataDO1), -1);
					Collections.rotate(Arrays.asList(dataDO2), -1);
					Collections.rotate(Arrays.asList(dataDO3), -1);
					dataAI0[showSamplesValue-1] = AI0;
					dataAI1[showSamplesValue-1] = AI1;
					dataAI2[showSamplesValue-1] = AI2;
					dataAI3[showSamplesValue-1] = AI3;
					dataDO0[showSamplesValue-1] = (float) DO0;
					dataDO1[showSamplesValue-1] = (float) DO1;
					dataDO2[showSamplesValue-1] = (float) DO2;
					dataDO3[showSamplesValue-1] = (float) DO3;
					updatePlotAndPulse();
				}
				
				// If we exceeded number of pulse limit
				if(pulseNumber >= ControlView.selectedBreakPulseLimit) {
					interuptMessage(email, "Number of pulse limits exceeded", alarmActivated);
					break; // Jump out from the while loop and update the ControlView
				}
				
				// If we exceeded the thresholds
				if(AI0 > AI0Max || AI0 < AI0Min || AI1 > AI1Max || AI1 < AI1Min || AI2 > AI2Max || AI2 < AI2Min || AI3 > AI3Max || AI3 < AI3Min) {
					interuptMessage(email, "Mesurement exceeded the threshold", alarmActivated);
					break;
				}
				
				// If we read true on stop signal
				if(stopSignal == true) {
					interuptMessage(email, "Stop signal activated - System halted", alarmActivated);
					break;
				}
				
				// Wait
				try {
					Thread.sleep(samplingTimeValue); 
				} catch (InterruptedException e) {}
			}	
			
			// This will cause so components will be enabled again - and also the control thread stops!
			ControlView.loggingNow.set(false); // OFF
			updatePlotAndPulse(); 
		}
	}
	
	private void interuptMessage(String email, String message, boolean alarmActivated) {
		if(alarmActivated == true) {
			mail.sendEmail(email, "Open Source Logger Message", message);
		}
	}

	private void updatePlotAndPulse() {
		ui.access(() -> {
			apexChart.updateSeries(
					MySQLView.createSerie(dataAI0, "AI0"),
					MySQLView.createSerie(dataAI1, "AI1"),
					MySQLView.createSerie(dataAI2, "AI2"),
					MySQLView.createSerie(dataAI3, "AI3"),
					MySQLView.createSerie(dataDO0, "DO0"),
					MySQLView.createSerie(dataDO1, "DO1"),
					MySQLView.createSerie(dataDO2, "DO2"),
					MySQLView.createSerie(dataDO3, "DO3"));
			countedPulses.setValue(pulseNumber);
			disableOrEnableComponents();
			
		});

	}

	public void setComponentsToThread(UI ui, ApexCharts apexChart, IntegerField countedPulses, Button loggingActivate, Select<Long> calibration, Select<Long> alarm, Select<Long> loggerId, PaperSlider do0Slider, PaperSlider do1Slider, PaperSlider do2Slider, PaperSlider do3Slider, IntegerField do0HighPulse, IntegerField do1HighPulse, IntegerField do2HighPulse, IntegerField do3HighPulse, IntegerField do0LowPulse, IntegerField do1LowPulse, IntegerField do2LowPulse, IntegerField do3LowPulse, Select<Integer> samplingTime, Select<Integer> showSamples, Checkbox showPlot, RadioButtonGroup<String> radioGroup, Checkbox lowFirstDo0, Checkbox lowFirstDo1, Checkbox lowFirstDo2, Checkbox lowFirstDo3, Checkbox countOnHighSignal) {
		this.ui = ui;
		this.apexChart = apexChart;
		this.countedPulses = countedPulses;
		this.loggingActivate = loggingActivate;
		this.calibration = calibration;
		this.alarm = alarm;
		this.loggerId = loggerId;
		this.do0Slider = do0Slider;
		this.do1Slider = do1Slider;
		this.do2Slider = do2Slider;
		this.do3Slider = do3Slider;
		this.do0HighPulse = do0HighPulse;
		this.do1HighPulse = do1HighPulse;
		this.do2HighPulse = do2HighPulse;
		this.do3HighPulse = do3HighPulse;
		this.do0LowPulse = do0LowPulse;
		this.do1LowPulse = do1LowPulse;
		this.do2LowPulse = do2LowPulse;
		this.do3LowPulse = do3LowPulse;
		this.samplingTime = samplingTime;
		this.showSamples = showSamples;
		this.showPlot = showPlot;
		this.radioGroup = radioGroup;
		this.lowFirstDo0 = lowFirstDo0;
		this.lowFirstDo1 = lowFirstDo1;
		this.lowFirstDo2 = lowFirstDo2;
		this.lowFirstDo3 = lowFirstDo3;
		this.countOnHighSignal = countOnHighSignal;
		disableOrEnableComponents(); // Once we have set our components, disable them or not.
	}
	
	private void disableOrEnableComponents() {
		if(ControlView.loggingNow.get() == true) {
			// This runs when we don't run the logging
			loggingActivate.setText(ControlView.STOP_LOGGING);
			calibration.setEnabled(false);
			alarm.setEnabled(false);
			loggerId.setEnabled(false);
			do0Slider.setEnabled(true);
			do1Slider.setEnabled(true);
			do2Slider.setEnabled(true);
			do3Slider.setEnabled(true);
			do0HighPulse.setEnabled(true);
			do1HighPulse.setEnabled(true);
			do2HighPulse.setEnabled(true);
			do3HighPulse.setEnabled(true);
			do0LowPulse.setEnabled(true);
			do1LowPulse.setEnabled(true);
			do2LowPulse.setEnabled(true);
			do3LowPulse.setEnabled(true);
			samplingTime.setEnabled(false);
			showSamples.setEnabled(false);
			showPlot.setEnabled(false);
			radioGroup.setEnabled(false);
			lowFirstDo0.setEnabled(false);
			lowFirstDo1.setEnabled(false);
			lowFirstDo2.setEnabled(false);
			lowFirstDo3.setEnabled(false);
			countOnHighSignal.setEnabled(false);
			
		}else{
			loggingActivate.setText(ControlView.START_LOGGING);
			calibration.setEnabled(true);
			alarm.setEnabled(true);
			loggerId.setEnabled(true);
			do0Slider.setEnabled(false);
			do1Slider.setEnabled(false);
			do2Slider.setEnabled(false);
			do3Slider.setEnabled(false);
			do0HighPulse.setEnabled(false);
			do1HighPulse.setEnabled(false);
			do2HighPulse.setEnabled(false);
			do3HighPulse.setEnabled(false);
			do0LowPulse.setEnabled(false);
			do1LowPulse.setEnabled(false);
			do2LowPulse.setEnabled(false);
			do3LowPulse.setEnabled(false);
			samplingTime.setEnabled(true);
			showSamples.setEnabled(true);
			showPlot.setEnabled(true);
			radioGroup.setEnabled(true);
			lowFirstDo0.setEnabled(true);
			lowFirstDo1.setEnabled(true);
			lowFirstDo2.setEnabled(true);
			lowFirstDo3.setEnabled(true);
			countOnHighSignal.setEnabled(true);
			
			// Set zero values
			do0Slider.setValue(0);
			do1Slider.setValue(0);
			do2Slider.setValue(0);
			do3Slider.setValue(0);
			do0HighPulse.setValue(0);
			do1HighPulse.setValue(0);
			do2HighPulse.setValue(0);
			do3HighPulse.setValue(0);
			do0LowPulse.setValue(0);
			do1LowPulse.setValue(0);
			do2LowPulse.setValue(0);
			do3LowPulse.setValue(0);
		}
	}
}
