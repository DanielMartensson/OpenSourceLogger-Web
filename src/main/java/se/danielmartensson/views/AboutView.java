package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;

import se.danielmartensson.views.components.Explonation;
import se.danielmartensson.views.templates.Top;

@Route("about")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class AboutView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@PostConstruct
	public void init() {
		Top top = new Top();
		top.setTopAppLayout(this);
		
		// Create explanation
		createExplonationGrid();
	}

	private void createExplonationGrid() {
		List<Explonation> explainList = new ArrayList<>();
		explainList.add(new Explonation("Logger Id", "The ID of the logger file"));
		explainList.add(new Explonation("Date", "The date when the logger file was created"));
		explainList.add(new Explonation("Name", "Name of the user who created the logger file"));
		explainList.add(new Explonation("Comment", "An optional comment about the logger file or settings"));
		explainList.add(new Explonation("CID", "Calibration identity is used when data is sampling"));
		explainList.add(new Explonation("AID", "Alarm identity is used when data is sampling. AID = 0, no alarm used"));
		explainList.add(new Explonation("SAI0", "Scalar of analog input 0"));
		explainList.add(new Explonation("BAI0", "Bias of analog input 0"));
		explainList.add(new Explonation("SAI1", "Scalar of analog input 1"));
		explainList.add(new Explonation("BAI1", "Bias of analog input 1"));
		explainList.add(new Explonation("SAI2", "Scalar of analog input 2"));
		explainList.add(new Explonation("BAI2", "Bias of analog input 2"));
		explainList.add(new Explonation("SAI3", "Scalar of analog input 3"));
		explainList.add(new Explonation("BAI3", "Bias of analog input 3"));
		explainList.add(new Explonation("Alarm", "A check mark if the alarm should be activated or not"));
		explainList.add(new Explonation("Email", "Destination for the alarm message when the threshold breaks"));
		explainList.add(new Explonation("AI0Min", "Minimum threshold for analog input 0"));
		explainList.add(new Explonation("AI0Max", "Maximum threshold for analog input 0"));
		explainList.add(new Explonation("AI1Min", "Minimum threshold for analog input 1"));
		explainList.add(new Explonation("AI1Max", "Maximum threshold for analog input 1"));
		explainList.add(new Explonation("AI2Min", "Minimum threshold for analog input 2"));
		explainList.add(new Explonation("AI2Max", "Maximum threshold for analog input 2"));
		explainList.add(new Explonation("AI3Min", "Minimum threshold for analog input 3"));
		explainList.add(new Explonation("AI3Max", "Maximum threshold for analog input 3"));
		explainList.add(new Explonation("DO0", "Digital output 0"));
		explainList.add(new Explonation("DO1", "Digital output 1"));
		explainList.add(new Explonation("DO2", "Digital output 2"));
		explainList.add(new Explonation("DO3", "Digital output 3"));
		explainList.add(new Explonation("AI0", "Analog input 0"));
		explainList.add(new Explonation("AI1", "Analog input 1"));
		explainList.add(new Explonation("AI2", "Analog input 2"));
		explainList.add(new Explonation("AI3", "Analog input 3"));
		Grid<Explonation> grid = new Grid<>(Explonation.class);
		grid.setItems(explainList);
		grid.setHeightFull();
		grid.setWidth("1200px");
		setContent(grid);	
	}
}
