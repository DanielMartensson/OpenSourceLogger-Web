package se.danielmartensson.views;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import se.danielmartensson.threads.CameraThread;
import se.danielmartensson.tools.Top;

@Route("camera")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Push
public class CameraView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String START = "START";
	public static final String STOP = "STOP";

	// For the thread
	private static AtomicBoolean startStopThread = null;
	private static CameraThread cameraThread = null;
	private static Webcam selectedWebcam = null;

	// Selected values - Save them
	private static String selectedCamera = null;
	private static String selectedPictureSize = null;

	// This need to be a non-static field
	private Button startStop = null;

	public CameraView() {
		Top top = new Top();
		top.setTopAppLayout(this);

		// Create image for the real time
		Image realTimeCameraImage = new Image();
		Select<String> pictureSize = new Select<String>();
		pictureSize.setEnabled(false);
		pictureSize.setLabel("Video Size Stream");
		realTimeCameraImage.setTitle("Real Time Video");

		// Start and stop button for camera
		startStop = new Button(START);
		startStopYoloConfiguration();

		// Create the thread
		if (cameraThread == null) {
			startStopThread = new AtomicBoolean(false);
			cameraThread = new CameraThread();
		}

		// Create the drop down button for the camera
		Select<String> cameras = new Select<String>();
		cameras.setLabel("Camera");
		createCameraSelectorButton(cameras, cameraThread, realTimeCameraImage, pictureSize);
		setPictureSize(pictureSize, realTimeCameraImage);

		// Set the components to the thread
		cameraThread.setComponentsToThread(startStop, startStopThread, UI.getCurrent(), cameras, selectedWebcam, realTimeCameraImage, pictureSize);
		if (!cameraThread.isAlive())
			cameraThread.start();

		// Content
		VerticalLayout layout = new VerticalLayout();
		FormLayout form = new FormLayout(startStop, cameras, pictureSize);
		form.setResponsiveSteps(new ResponsiveStep("10em", 1), new ResponsiveStep("32em", 2), new ResponsiveStep("40em", 3));
		layout.add(form);
		layout.add(realTimeCameraImage);
		layout.setAlignItems(Alignment.CENTER);
		setContent(layout);
	}

	private void setPictureSize(Select<String> pictureSize, Image realTimeCameraImage) {
		pictureSize.addValueChangeListener(e -> {
			if (e.getValue() == null)
				return;
			// Change the display image
			String[] size = e.getValue().split("x"); // Width x height
			int width = (int) Float.parseFloat(size[0]);
			int height = (int) Float.parseFloat(size[1]);
			selectedPictureSize = width + "x" + height;
			realTimeCameraImage.setWidth(width + "px");
			realTimeCameraImage.setHeight(height + "px");

			// Change the camera
			if (selectedWebcam != null) {
				if (!selectedWebcam.isOpen()) {
					selectedWebcam.setViewSize(new Dimension(width, height));
				} else {
					new Notification("You can only set the camera size when the camera is closed", 3000).open();
				}
			}
		});
	}

	private void startStopYoloConfiguration() {
		startStop.setEnabled(false); // Need to be before the listener
		if (selectedWebcam != null) {
			if (selectedWebcam.isOpen()) {
				startStop.setEnabled(true);
			}
		}
		startStop.addClickListener(e -> {
			if (startStopThread.get()) {
				startStopThread.set(false); // Stop YOLO
			} else {
				startStopThread.set(true); // Start YOLO
			}
		});
	}

	/**
	 * This creates the camera selector drop down button and also gives it a
	 * listener for enable the camera
	 * 
	 * @param cameras
	 * @param imageShowRealTimeThread
	 * @param realTimeCameraImage
	 * @param thresholds
	 * @param pictureSize
	 */
	private void createCameraSelectorButton(Select<String> cameras, CameraThread imageShowRealTimeThread, Image realTimeCameraImage, Select<String> pictureSize) {
		// Fill with camera names
		List<Webcam> webcamsList = Webcam.getWebcams();
		String[] webcamNames = new String[webcamsList.size()];
		int i = 0;
		for (Webcam webcam : webcamsList) {
			String cameraName = webcam.getName();
			boolean contains = Arrays.stream(webcamNames).anyMatch(cameraName::equals); // Check if cameraName contains in webcamArray
			if (!contains) {
				webcamNames[i] = cameraName;
				i++;
			}
		}
		cameras.setItems(webcamNames);

		// Add a listener for enabling the camera
		cameras.addValueChangeListener(e -> {
			if (selectedWebcam == null) {
				selectNewCamera(cameras, imageShowRealTimeThread, realTimeCameraImage, pictureSize);
			} else {
				selectedWebcam.close();
				selectNewCamera(cameras, imageShowRealTimeThread, realTimeCameraImage, pictureSize);
			}
		});
		if (selectedCamera != null) {
			cameras.setValue(selectedCamera);
		}
	}

	/**
	 * This will select the camera from webcamsList for us and open it
	 * 
	 * @param cameras
	 * @param imageShowRealTimeThread
	 * @param realTimeCameraImage
	 * @param thresholds
	 * @param pictureSize
	 */
	private void selectNewCamera(Select<String> cameras, CameraThread imageShowRealTimeThread, Image realTimeCameraImage, Select<String> pictureSize) {
		List<Webcam> webcamsList = Webcam.getWebcams();
		String selectedCameraName = cameras.getValue();
		selectedCamera = cameras.getValue();
		selectedWebcam = webcamsList.stream().filter(x -> selectedCameraName.equals(x.getName())).findFirst().get(); // This generates a new object of the web cam
		startStop.setEnabled(true);
		pictureSize.setEnabled(true);
		imageShowRealTimeThread.setSelectedWebcam(selectedWebcam);
		fillPictureSizesDropdownButton(pictureSize);
	}

	private void fillPictureSizesDropdownButton(Select<String> pictureSize) {
		pictureSize.clear();
		ArrayList<String> list = new ArrayList<>();
		for (Dimension dimension : selectedWebcam.getViewSizes()) {
			list.add(dimension.getWidth() + "x" + dimension.getHeight());
		}
		pictureSize.setItems(list);
		// Select the selected
		if (selectedPictureSize == null)
			return;
		for (String resolution : list) {
			String[] size = resolution.split("x"); // Width x height
			int width = (int) Float.parseFloat(size[0]);
			int height = (int) Float.parseFloat(size[1]);
			String[] selectedSize = selectedPictureSize.split("x"); // Width x height
			int widthSelected = (int) Float.parseFloat(selectedSize[0]);
			int heightSelected = (int) Float.parseFloat(selectedSize[1]);
			if (width == widthSelected && height == heightSelected) {
				pictureSize.setValue(resolution);
			}
		}
	}
}