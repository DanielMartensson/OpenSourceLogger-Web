package se.danielmartensson.views;

import java.util.Collection;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.service.AlarmService;
import se.danielmartensson.tools.Top;

@Route("alarm")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for alarm handling
 * 
 * @author dell
 *
 */
public class AlarmView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlarmView(AlarmService alarmService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Grid layout
		GridCrud<Alarm> alarmCrud = new GridCrud<>(Alarm.class);
		CrudFormFactory<Alarm> crudFormFactory = new DefaultCrudFormFactory<Alarm>(Alarm.class);
		alarmCrud.setCrudFormFactory(crudFormFactory);
		alarmCrud.getGrid().setColumns("name", "email", "message", "alarmActive", "messageHasBeenSent", "sa0Min", "sa0Max", "sa1Min", "sa1Max", "sa1dMin", "sa1dMax", "sa2dMin", "sa2dMax", "sa3dMin", "sa3dMax", "a0Min", "a0Max", "a1Min", "a1Max", "a2Min", "a2Max", "a3Min", "a3Max");
		alarmCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setVisibleProperties(new String[] { "name", "email", "message", "alarmActive", "messageHasBeenSent", "sa0Min", "sa0Max", "sa1Min", "sa1Max", "sa1dMin", "sa1dMax", "sa2dMin", "sa2dMax", "sa3dMin", "sa3dMax", "a0Min", "a0Max", "a1Min", "a1Max", "a2Min", "a2Max", "a3Min", "a3Max" });
		
		// Listener
		alarmCrud.setCrudListener(new CrudListener<Alarm>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<Alarm> findAll() {
				return alarmService.findAll();
			}

			@Override
			public Alarm add(Alarm alarm) {
				boolean nameExist = alarmService.existsByName(alarm.getName());
				if (nameExist) {
					new Notification("Cannot add this with a name that already exist.", 3000).open();
					return alarm;
				}
				return alarmService.save(alarm);
			}

			@Override
			public Alarm update(Alarm alarm) {
				// Check if we updating the same row
				boolean nameExist = alarmService.existsByName(alarm.getName());
				Alarm anotherAlarmWithSameName = alarmService.findByName(alarm.getName());
				if (nameExist && anotherAlarmWithSameName.getId() != alarm.getId()) {
					new Notification("Cannot update this with a name that already exist.", 3000).open();
					return alarm;
				}
				
				return alarmService.save(alarm);
			}

			@Override
			public void delete(Alarm alarm) {
				boolean parentExist = alarmService.delete(alarm);
				if (parentExist) {
					new Notification("Cannot delete this alarm because a job that have this alarm exist.", 3000).open();
				}
			}
		});
		setContent(alarmCrud);
	}
}
