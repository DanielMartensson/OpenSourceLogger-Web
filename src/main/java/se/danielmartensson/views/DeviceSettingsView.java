package se.danielmartensson.views;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.PWM;
import se.danielmartensson.hardware.Serial;
import se.danielmartensson.service.PWMService;
import se.danielmartensson.tools.Top;

@Route("pwm")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for pwm handling
 * 
 * @author dell
 *
 */
public class DeviceSettingsView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MIN_VALUE_INTEGER_FIELD = 31;

	private static final int MAX_VALUE_INTEGER_FIELD = 2000000;

	@Autowired
	private PWMService pwmService;

	@Autowired
	private Serial serial;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Selector and integer text fields
		SerialPort[] serialPorts = serial.getSerialPorts();
		Select<SerialPort> serialDevice = new Select<SerialPort>(serialPorts); // Will show port description
		serialDevice.setLabel("Port description");
		IntegerField Frequency_P0_P1_P2 = new IntegerField("Frequency P0 P1 P2");
		IntegerField Frequency_P3_P7_P8 = new IntegerField("Frequency P3 P7 P8");
		IntegerField Frequency_P6_P5 = new IntegerField("Frequency P6 P5");
		IntegerField Frequency_P4 = new IntegerField("Frequency P4");

		// Get values
		int Frequency_P0_P1_P2_Value = MIN_VALUE_INTEGER_FIELD;
		int Frequency_P3_P7_P8_Value = MIN_VALUE_INTEGER_FIELD;
		int Frequency_P6_P5_Value = MIN_VALUE_INTEGER_FIELD;
		int Frequency_P4_Value = MIN_VALUE_INTEGER_FIELD;
		List<PWM> list = pwmService.findAll(); // We only want the first row
		if (list.size() > 0) {
			Frequency_P0_P1_P2_Value = list.get(0).getFrequencyP0P1P2();
			Frequency_P3_P7_P8_Value = list.get(0).getFrequencyP3P7P8();
			Frequency_P6_P5_Value = list.get(0).getFrequencyP6P5();
			Frequency_P4_Value = list.get(0).getFrequencyP4();
			for (SerialPort serialPort : serialPorts) {
				if (serialPort.getPortDescription().equals(list.get(0).getPortDescription())) {
					serialDevice.setValue(serialPort);
				}
			}
		}

		// Set max and min value and also current value
		setMinMaxValueIntegerField(Frequency_P0_P1_P2, Frequency_P0_P1_P2_Value);
		setMinMaxValueIntegerField(Frequency_P3_P7_P8, Frequency_P3_P7_P8_Value);
		setMinMaxValueIntegerField(Frequency_P6_P5, Frequency_P6_P5_Value);
		setMinMaxValueIntegerField(Frequency_P4, Frequency_P4_Value);

		// Grid layout
		GridCrud<PWM> pwmCrud = new GridCrud<>(PWM.class);
		CrudFormFactory<PWM> crudFormFactory = new DefaultCrudFormFactory<PWM>(PWM.class);
		pwmCrud.setCrudFormFactory(crudFormFactory);
		String[] columnNames = new String[] { "portDescription", "frequencyP0P1P2", "frequencyP3P7P8", "frequencyP6P5", "frequencyP4" };
		String[] columnProperties = new String[] { "id", "portDescription", "frequencyP0P1P2", "frequencyP3P7P8", "frequencyP6P5", "frequencyP4" };
		pwmCrud.getGrid().setColumns(columnNames);
		pwmCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setDisabledProperties(CrudOperation.ADD, columnProperties);
		crudFormFactory.setDisabledProperties(CrudOperation.DELETE, columnProperties);
		crudFormFactory.setDisabledProperties(CrudOperation.UPDATE, columnProperties);

		// Select settings
		Button saveSettings = new Button("Save settings");
		saveSettings.addClickListener(e -> {
			// Quick check
			if (serialDevice.getValue() == null) {
				new Notification("No port selected", 3000).open();
				return;
			}
			if (!serial.isPortOpen()) {
				new Notification("No port open", 3000).open();
				return;
			}
			
			// Check if our values are OK
			int P0_P1_P2_Value = Frequency_P0_P1_P2.getValue();
			int P3_P7_P8_Value = Frequency_P3_P7_P8.getValue();
			int P6_P5_Value = Frequency_P6_P5.getValue();
			int P4_Value = Frequency_P4.getValue();
			String portDescription = serialDevice.getValue().getPortDescription();
			if (P0_P1_P2_Value < MIN_VALUE_INTEGER_FIELD || P3_P7_P8_Value < MIN_VALUE_INTEGER_FIELD || P6_P5_Value < MIN_VALUE_INTEGER_FIELD || P4_Value < MIN_VALUE_INTEGER_FIELD) {
				new Notification("Too small number!", 3000).open();
				return;
			}
				
			if (P0_P1_P2_Value > MAX_VALUE_INTEGER_FIELD || P3_P7_P8_Value > MAX_VALUE_INTEGER_FIELD || P6_P5_Value > MAX_VALUE_INTEGER_FIELD || P4_Value > MAX_VALUE_INTEGER_FIELD) {
				new Notification("Too large number!", 3000).open();
				return;
			}

			// OK! Write to database and send to the device about new settings
			pwmService.deleteAll(); // Only one setting allowed
			pwmService.save(new PWM(0, portDescription, P0_P1_P2_Value, P3_P7_P8_Value, P6_P5_Value, P4_Value));
			int[] frequency = new int[4];
			frequency[0] = 2000000 / P4_Value; //TIM2
			frequency[1] = 2000000 / P6_P5_Value; //TIM3
			frequency[2] = 2000000 / P3_P7_P8_Value; //TIM4
			frequency[3] = 2000000 / P0_P1_P2_Value; //TIM5
			serial.trancieve(frequency);
			pwmCrud.refreshGrid();
			new Notification("Success!", 3000).open();
		});

		// Connect button
		Button connectDevice = new Button("Connect device");
		connectDevice.addClickListener(e -> {
			if (serialDevice.getValue() == null) {
				new Notification("No port selected", 3000).open();
				return;
			}
			String portDescription = serialDevice.getValue().getPortDescription();
			serial.closePort();
			serial.selectNewPort(portDescription);
			if(serial.isPortOpen())
				new Notification("Yes, port is open!", 3000).open();
			else
				new Notification("No, port is closed!", 3000).open();
		});
		
		FormLayout settings = new FormLayout(serialDevice, Frequency_P0_P1_P2, Frequency_P3_P7_P8, Frequency_P6_P5, Frequency_P4, connectDevice, saveSettings);

		// Listener
		pwmCrud.setCrudListener(new CrudListener<PWM>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<PWM> findAll() {
				return pwmService.findAll();
			}

			@Override
			public PWM add(PWM pwm) {
				return null;
			}

			@Override
			public PWM update(PWM pwm) {
				return null;
			}

			@Override
			public void delete(PWM pwm) {
			}

		});

		setContent(new VerticalLayout(settings, pwmCrud));
	}

	private void setMinMaxValueIntegerField(IntegerField integerField, int value) {
		// Min MIN_VALUE_INTEGER_FIELD Hz and max MAX_VALUE_INTEGER_FIELD Hz as TIM frequency in this STM32 device
		integerField.setMin(MIN_VALUE_INTEGER_FIELD);
		integerField.setHasControls(true);
		integerField.setMax(MAX_VALUE_INTEGER_FIELD);
		integerField.setValue(value);
	}

	public DeviceSettingsView() {

	}
}
