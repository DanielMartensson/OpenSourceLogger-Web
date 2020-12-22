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

import se.danielmartensson.entities.Calibration;
import se.danielmartensson.service.CalibrationService;
import se.danielmartensson.tools.Top;

@Route("calibration")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
/**
 * This class modifies the user interface for calibration handling
 * 
 * @author dell
 *
 */
public class CalibrationView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalibrationView(CalibrationService calibrationService) {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Grid layout
		GridCrud<Calibration> calibrationCrud = new GridCrud<>(Calibration.class);
		CrudFormFactory<Calibration> crudFormFactory = new DefaultCrudFormFactory<Calibration>(Calibration.class);
		calibrationCrud.setCrudFormFactory(crudFormFactory);
		calibrationCrud.getGrid().setColumns("name", "sa0Slope", "sa0Bias", "sa1Slope", "sa1Bias", "sa1dSlope", "sa1dBias", "sa2dSlope", "sa2dBias", "sa3dSlope", "sa3dBias", "a0Slope", "a0Bias", "a1Slope", "a1Bias", "a2Slope", "a2Bias", "a3Slope", "a3Bias");
		calibrationCrud.getGrid().setColumnReorderingAllowed(true);
		crudFormFactory.setUseBeanValidation(true);
		crudFormFactory.setVisibleProperties(new String[] { "name", "sa0Slope", "sa0Bias", "sa1Slope", "sa1Bias", "sa1dSlope", "sa1dBias", "sa2dSlope", "sa2dBias", "sa3dSlope", "sa3dBias", "a0Slope", "a0Bias", "a1Slope", "a1Bias", "a2Slope", "a2Bias", "a3Slope", "a3Bias" });

		// Listener
		calibrationCrud.setCrudListener(new CrudListener<Calibration>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<Calibration> findAll() {
				return calibrationService.findAll();
			}

			@Override
			public Calibration add(Calibration calibration) {
				boolean nameExist = calibrationService.existsByName(calibration.getName());
				if (nameExist) {
					new Notification("Cannot add this with a name that already exist.", 3000).open();
					return calibration;
				}
				return calibrationService.save(calibration);
			}

			@Override
			public Calibration update(Calibration calibration) {
				boolean nameExist = calibrationService.existsByName(calibration.getName());
				if (nameExist) {
					new Notification("Cannot update this with a name that already exist.", 3000).open();
					return calibration;
				}
				return calibrationService.save(calibration);
			}

			@Override
			public void delete(Calibration calibration) {
				boolean parentExist = calibrationService.delete(calibration);
				if (parentExist) {
					new Notification("Cannot delete this calibration because a job that have this calibration exist.", 3000).open();
				}

			}

		});

		setContent(calibrationCrud);

	}

}
