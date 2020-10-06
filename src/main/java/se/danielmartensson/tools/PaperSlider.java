package se.danielmartensson.views.components;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("paper-slider")
@NpmPackage(value = "@polymer/paper-slider", version = "3.0.1")
@JsModule("@polymer/paper-slider/paper-slider.js")
public class PaperSlider extends AbstractSinglePropertyField<PaperSlider, Integer> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaperSlider(int max) {
        super("value", 0, false);
        this.getElement().setProperty("max", max);
        this.getElement().setProperty("pin", true);
    }

}

