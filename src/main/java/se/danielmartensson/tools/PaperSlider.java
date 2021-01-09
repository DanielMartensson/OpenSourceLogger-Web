package se.danielmartensson.tools;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("paper-slider")
@NpmPackage(value = "@polymer/paper-slider", version = "3.0.1")
@JsModule("@polymer/paper-slider/paper-slider.js")
/**
 * This class is made for the PWM and DAC sliders
 * 
 * @author Daniel Mårtensson
 *
 */
public class PaperSlider extends AbstractSinglePropertyField<PaperSlider, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// These paper slider are modified to 500px in width
	public PaperSlider(int max) {
		super("value", 0, false);
		this.getElement().setProperty("max", max);
		this.getElement().setProperty("pin", true);
	}
}
