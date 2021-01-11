package se.danielmartensson.views;

import java.util.Collection;

import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.Calibration;
import se.danielmartensson.entities.Sensor;
import se.danielmartensson.service.CalibrationService;
import se.danielmartensson.service.SensorService;
import se.danielmartensson.tools.Top;

@Route("sensor")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for sensor handling
 * 
 * @author dell
 *
 */
public class SensorView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SensorView(SensorService sensorService, CalibrationService calibrationService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Grid layout
		GridCrud<Sensor> sensorCrud = new GridCrud<>(Sensor.class);
		CrudFormFactory<Sensor> crudFormFactory = new DefaultCrudFormFactory<Sensor>(Sensor.class);
		sensorCrud.setCrudFormFactory(crudFormFactory);
		sensorCrud.getGrid().setColumns("name", "comment", "calibration", "sa0MinValue", "sa0MaxValue", "sa1MinValue", "sa1MaxValue", "sa1dMinValue", "sa1dMaxValue", "sa2dMinValue", "sa2dMaxValue", "sa3dMinValue", "sa3dMaxValue", "a0MinValue", "a0MaxValue", "a1MinValue", "a1MaxValue", "a2MinValue", "a2MaxValue", "a3MinValue", "a3MaxValue");
		sensorCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setVisibleProperties(new String[] {"name", "comment", "calibration", "sa0MinValue", "sa0MaxValue", "sa1MinValue", "sa1MaxValue", "sa1dMinValue", "sa1dMaxValue", "sa2dMinValue", "sa2dMaxValue", "sa3dMinValue", "sa3dMaxValue", "a0MinValue", "a0MaxValue", "a1MinValue", "a1MaxValue", "a2MinValue", "a2MaxValue", "a3MinValue", "a3MaxValue"});
		crudFormFactory.setFieldProvider("calibration", new ComboBoxProvider<>("Calibration", calibrationService.findAll(), new TextRenderer<>(Calibration::getName), Calibration::getName));

		// Listener
		sensorCrud.setCrudListener(new CrudListener<Sensor>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<Sensor> findAll() {
				return sensorService.findAll();
			}

			@Override
			public Sensor add(Sensor sensor) {
				boolean nameExist = sensorService.existsByName(sensor.getName());
				if (nameExist) {
					new Notification("Cannot add this with a name that already exist.", 3000).open();
					return sensor;
				}
				return sensorService.save(sensor);
			}

			@Override
			public Sensor update(Sensor sensor) {
				// Check if we updating the same row
				boolean nameExist = sensorService.existsByName(sensor.getName());
				if (nameExist) {
					Sensor anotherSensorWithSameName = sensorService.findByName(sensor.getName());
					if(anotherSensorWithSameName.getId() != sensor.getId()) {
						new Notification("Cannot update this with a name that already exist.", 3000).open();
						return sensor;
					}
				}
				return sensorService.save(sensor);
			}

			@Override
			public void delete(Sensor sensor) {
				boolean childDeleted = sensorService.delete(sensor);
				if (!childDeleted) {
					new Notification("Cannot delete this sensor because a job that have this sensor exist.", 3000).open();
				}
			}

		});
		setContent(sensorCrud);
	}
}