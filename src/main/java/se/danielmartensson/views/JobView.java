package se.danielmartensson.views;

import java.util.Collection;

import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.Sensor;
import se.danielmartensson.entities.Job;
import se.danielmartensson.service.AlarmService;
import se.danielmartensson.service.SensorService;
import se.danielmartensson.service.DataService;
import se.danielmartensson.service.JobService;
import se.danielmartensson.tools.Top;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean. Use the @PWA
 * annotation make the application installable on phones, tablets and some
 * desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 */
@Route("")
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for job creation handling
 * 
 * @author dell
 *
 */
public class JobView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JobView(JobService jobService, SensorService sensorService, AlarmService alarmService, DataService dataService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Grid layout
		GridCrud<Job> jobCrud = new GridCrud<>(Job.class);
		CrudFormFactory<Job> crudFormFactory = new DefaultCrudFormFactory<Job>(Job.class);
		jobCrud.setCrudFormFactory(crudFormFactory);
		jobCrud.getGrid().setColumns("name", "date", "sensor", "alarm");
		jobCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setVisibleProperties(new String[] { "name", "date", "sensor", "alarm" });
		crudFormFactory.setFieldType("date", DatePicker.class);
		crudFormFactory.setFieldProvider("sensor", new ComboBoxProvider<>("Sensor", sensorService.findAll(), new TextRenderer<>(Sensor::getName), Sensor::getName));
		crudFormFactory.setFieldProvider("alarm", new ComboBoxProvider<>("Alarm", alarmService.findAll(), new TextRenderer<>(Alarm::getName), Alarm::getName));

		// Listener
		jobCrud.setCrudListener(new CrudListener<Job>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<Job> findAll() {
				return jobService.findAll();
			}

			@Override
			public Job add(Job job) {
				boolean nameExist = jobService.existsByName(job.getName());
				if (nameExist) {
					new Notification("Cannot add this with a name that already exist.", 3000).open();
					return job;
				}
				return jobService.save(job);
			}

			@Override
			public Job update(Job job) {
				// Check if we updating the same row
				String jobName = job.getName();
				long jobId = job.getId();
				boolean nameExist = jobService.existsByName(jobName);
				Job anotherJobWithSameName = jobService.findByName(jobName);
				if (nameExist && anotherJobWithSameName.getId() != jobId) {
					new Notification("Cannot update this with a name that already exist.", 3000).open();
					return job;
				}		
				// If we rename the name, then all data should be renamed as well
				String oldJobName = jobService.findById(jobId).getName(); // Even if the name is going to be changed, ID is the same
				dataService.updateJobNameWhereJobName(jobName, oldJobName);
				return jobService.save(job);
			}

			@Override
			public void delete(Job job) {
				jobService.delete(job);
			}

		});
		setContent(jobCrud);

	}
}
