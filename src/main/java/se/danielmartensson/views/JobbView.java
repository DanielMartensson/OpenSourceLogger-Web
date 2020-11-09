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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import se.danielmartensson.entities.UserLogg;
import se.danielmartensson.repositories.UserLoggRepository;
import se.danielmartensson.tools.Top;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for job creation handling
 * @author dell
 *
 */
public class JobbView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserLoggRepository userLoggRepository;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// Create user logg crud
		createUserLoggCrud();
	}

	private void createUserLoggCrud() {
		// Grid layout
		GridCrud<UserLogg> userLoggCrud = new GridCrud<>(UserLogg.class);
		CrudFormFactory<UserLogg> crudFormFactory = new DefaultCrudFormFactory<UserLogg>(UserLogg.class);
		userLoggCrud.setCrudFormFactory(crudFormFactory);
		userLoggCrud.getGrid().setColumns("loggerId", "name", "date", "comment");
		userLoggCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		String[] columnNames = {"name", "date", "comment"};
		crudFormFactory.setVisibleProperties(CrudOperation.READ, columnNames);
		crudFormFactory.setVisibleProperties(CrudOperation.ADD, columnNames);
		crudFormFactory.setVisibleProperties(CrudOperation.UPDATE, columnNames);
		crudFormFactory.setVisibleProperties(CrudOperation.DELETE, columnNames);
		crudFormFactory.setFieldType("date", DatePicker.class);
		
		// Listener
		userLoggCrud.setCrudListener(new CrudListener<UserLogg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<UserLogg> findAll() {
				return userLoggRepository.findAll();
			}

			@Override
			public UserLogg add(UserLogg domainObjectToAdd) {			
				return userLoggRepository.save(domainObjectToAdd);
			}

			@Override
			public UserLogg update(UserLogg domainObjectToUpdate) {
				return userLoggRepository.save(domainObjectToUpdate);
			}

			@Override
			public void delete(UserLogg domainObjectToDelete) {
				userLoggRepository.delete(domainObjectToDelete);
			}

		});
		setContent(userLoggCrud);
		
	}
	
}
