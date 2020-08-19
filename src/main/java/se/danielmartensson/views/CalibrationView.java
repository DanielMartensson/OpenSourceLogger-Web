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

import se.danielmartensson.views.database.calibrationlogg.CalibrationLogg;
import se.danielmartensson.views.database.calibrationlogg.CalibrationLoggRepository;
import se.danielmartensson.views.templates.Top;

@Route("calibration")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class CalibrationView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CalibrationLoggRepository calibrationLoggRepository;

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);

		createCalibrationLoggCrud();
	}

	private void createCalibrationLoggCrud() {
		// Grid layout
		GridCrud<CalibrationLogg> calibrationLoggCrud = new GridCrud<>(CalibrationLogg.class);
		CrudFormFactory<CalibrationLogg> crudFormFactory = new DefaultCrudFormFactory<CalibrationLogg>(CalibrationLogg.class);
		calibrationLoggCrud.setCrudFormFactory(crudFormFactory);
		calibrationLoggCrud.getGrid().setColumns("CID", "comment", "SAI0", "BAI0", "SAI1", "BAI1", "SAI2", "BAI2", "SAI3", "BAI3");
		calibrationLoggCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setDisabledProperties(CrudOperation.ADD, "CID");
		crudFormFactory.setDisabledProperties(CrudOperation.UPDATE, "CID");
		crudFormFactory.setDisabledProperties(CrudOperation.DELETE, "CID");

		// Listener
		calibrationLoggCrud.setCrudListener(new CrudListener<CalibrationLogg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<CalibrationLogg> findAll() {
				return calibrationLoggRepository.findAll();
			}

			@Override
			public CalibrationLogg add(CalibrationLogg domainObjectToAdd) {
				return calibrationLoggRepository.save(domainObjectToAdd);
			}

			@Override
			public CalibrationLogg update(CalibrationLogg domainObjectToUpdate) {
				return calibrationLoggRepository.save(domainObjectToUpdate);
			}

			@Override
			public void delete(CalibrationLogg domainObjectToDelete) {
				calibrationLoggRepository.delete(domainObjectToDelete);
			}

		});
		setContent(calibrationLoggCrud);

	}

}
