package se.danielmartensson.views;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.AlarmLogg;
import se.danielmartensson.repositories.AlarmLoggRepository;
import se.danielmartensson.tools.Top;

@Route("alarm")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class AlarmView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AlarmLoggRepository alarmLoggRepository;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);

		createAlarmLoggCrud();
	}

	private void createAlarmLoggCrud() {
		// Grid layout
		GridCrud<AlarmLogg> alarmLoggCrud = new GridCrud<>(AlarmLogg.class);
		CrudFormFactory<AlarmLogg> crudFormFactory = new DefaultCrudFormFactory<AlarmLogg>(AlarmLogg.class);
		alarmLoggCrud.setCrudFormFactory(crudFormFactory);
		alarmLoggCrud.getGrid().setColumns("AID", "comment", "alarm", "email", "AI0Max", "AI0Min", "AI1Min", "AI1Max", "AI2Min", "AI2Max", "AI3Min", "AI3Max");
		alarmLoggCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setDisabledProperties(CrudOperation.ADD, "AID");
		crudFormFactory.setDisabledProperties(CrudOperation.UPDATE, "AID");
		crudFormFactory.setDisabledProperties(CrudOperation.DELETE, "AID");

		// Listener
		alarmLoggCrud.setCrudListener(new CrudListener<AlarmLogg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<AlarmLogg> findAll() {
				return alarmLoggRepository.findAll();
			}

			@Override
			public AlarmLogg add(AlarmLogg domainObjectToAdd) {
				return alarmLoggRepository.save(domainObjectToAdd);
			}

			@Override
			public AlarmLogg update(AlarmLogg domainObjectToUpdate) {
				return alarmLoggRepository.save(domainObjectToUpdate);
			}
			
			@Override
			public void delete(AlarmLogg domainObjectToDelete) {
				alarmLoggRepository.delete(domainObjectToDelete);
			}

		});
		setContent(alarmLoggCrud);

	}

}
