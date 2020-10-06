package se.danielmartensson.tools;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import se.danielmartensson.security.SecurityConfig;
import se.danielmartensson.views.AboutView;
import se.danielmartensson.views.AlarmView;
import se.danielmartensson.views.CalibrationView;
import se.danielmartensson.views.ControlView;
import se.danielmartensson.views.JobbView;
import se.danielmartensson.views.MySQLView;


public class Top {
	
	private Tabs tabs;
	private Image img;
	private DrawerToggle drawerToggle;

	public Top() {
		// Header
		img = new Image("img/barPicture.png", "Open Source Logger Icon");
        img.setHeight("44px");
        drawerToggle = new DrawerToggle();
        
        // Tabs and its listener
        Tab jobbTab = new Tab("Jobb");
        jobbTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(JobbView.class);
        });
        Tab controlTab = new Tab("Control");
        controlTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(ControlView.class);
        });
        Tab mySQLTab = new Tab("MySQL");
        mySQLTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(MySQLView.class);
        });
        Tab calibrationTab = new Tab("Calibration");
        calibrationTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(CalibrationView.class);
        });
        Tab alarmTab = new Tab("Alarm");
        alarmTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(AlarmView.class);
        });
        Tab aboutTab = new Tab("About");
        aboutTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().navigate(AboutView.class);
        });
        Tab logoutTab = new Tab("Logout");
        logoutTab.getElement().addEventListener("click", e -> {
        	UI.getCurrent().getPage().setLocation(SecurityConfig.LOGOUT);
        });
        tabs = new Tabs(jobbTab, controlTab, mySQLTab, calibrationTab, alarmTab, aboutTab, logoutTab);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        
	}
	
	public void setTopAppLayout(AppLayout appLayout) {
		appLayout.addToNavbar(drawerToggle, img);
		appLayout.addToDrawer(tabs);
	}
}
