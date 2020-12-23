package se.danielmartensson.threads;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.server.StreamResource;

import lombok.Setter;
import se.danielmartensson.views.CameraView;

public class CameraThread extends Thread {

	private UI ui;
	private AtomicBoolean startStopThread;
	@Setter
	private Webcam selectedWebcam;
	private Image realTimeCameraImage;
	private Button startStop;
	private Select<String> cameras;
	private Select<String> pictureSize;

	public CameraThread() {
	    //Webcam.setDriver(new V4l4jDriver()); // This is important for Raspberry Pi boards
	}

	@Override
	public void run() {
		while (true) {
			while (startStopThread.get()) {
				try {
					if (!selectedWebcam.isOpen()) {
						ui.access(() -> enableDisableSelectAndButton()); // This will start the camera
					}

					// Snap with camera
					BufferedImage image = selectedWebcam.getImage();
					if (image != null) {

						// Get the stream
						StreamResource stream = getStreamFromImage(image);
						if (stream != null) {
							ui.access(() -> {
								if (startStopThread.get()) // Prevents error if predictedFileAsStreamSource is non null and the
															// predictions.jpg does not exist
									realTimeCameraImage.setSrc(stream);
								enableDisableSelectAndButton();
							});
						} else {
							stopThread();
						}
					} else {
						stopThread();
					}
				} catch (Exception e) {
					// Something bad happen
					e.printStackTrace();
					stopThread();
				}

				sleepThread(10L);
			}
			sleepThread(10L);
		}
	}

	private void sleepThread(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopThread() {
		ui.access(() -> {
			startStopThread.set(false); // Stop
			enableDisableSelectAndButton();
		});
	}

	private StreamResource getStreamFromImage(BufferedImage image) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "png", output);
			byte[] data = output.toByteArray();
			StreamResource stream = new StreamResource("Saved.png", () -> new ByteArrayInputStream(data));
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void enableDisableSelectAndButton() {
		if (startStopThread.get() == true) {
			try {
				selectedWebcam.open();
				startStop.setText(CameraView.STOP);
				startStop.setEnabled(true);
				cameras.setEnabled(false);
				pictureSize.setEnabled(false);
			} catch (Exception e) {
				new Notification("Select another camera", 3000).open();
			}

		} else {
			selectedWebcam.close();
			startStop.setText(CameraView.START);
			cameras.setEnabled(true);
			pictureSize.setEnabled(true);
		}
	}

	public void setComponentsToThread(Button startStop, AtomicBoolean startStopThread, UI ui, Select<String> cameras, Webcam selectedWebcam, Image realTimeCameraImage, Select<String> pictureSize) {
		this.startStop = startStop;
		this.startStopThread = startStopThread;
		this.ui = ui;
		this.cameras = cameras;
		this.selectedWebcam = selectedWebcam;
		this.realTimeCameraImage = realTimeCameraImage;
		this.pictureSize = pictureSize;
	}

}
