package se.danielmartensson.tools;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import se.danielmartensson.entities.Data;

// Same idea as https://github.com/DanielMartensson/Mataveid/blob/master/sourcecode/filtfilt2.m
public class Filtering {
	public static void filtfilt(List<Data> dataList, double[] filtfiltKValues){
		eulerSimulation(dataList, filtfiltKValues);
		Collections.reverse(dataList); // Flip to reverse
		eulerSimulation(dataList, filtfiltKValues);
		Collections.reverse(dataList); // Flip back to normal again
	}

	private static void eulerSimulation(List<Data> dataList, double[] filtfiltKValues) {
		// Initial states
		float a0 = dataList.get(0).getA0Value();
		float a1 = dataList.get(0).getA1Value();
		float a2 = dataList.get(0).getA2Value();
		float a3 = dataList.get(0).getA3Value();
		float sa0 = dataList.get(0).getSa0Value();
		float sa1 = dataList.get(0).getSa1Value();
		float sa1d = dataList.get(0).getSa1dValue();
		float sa2d = dataList.get(0).getSa2dValue();
		float sa3d = dataList.get(0).getSa3dValue();
		
		// Copy over
		float[] K = new float[filtfiltKValues.length];
		for(int i = 0; i < filtfiltKValues.length; i++)
			K[i] = (float) filtfiltKValues[i];
		
		// Find time difference in seconds
		long timedifferenceMilliseconds = ChronoUnit.MILLIS.between(dataList.get(0).getDateTime(), dataList.get(1).getDateTime());
		float timedifferenceSeconds = (float) ((timedifferenceMilliseconds) / 1000.0);
		
		// Euler simulation
		for(int i = 1; i < dataList.size(); i++) {
			
			// Compute
			a0 = a0 + timedifferenceSeconds*(-1/K[0]*a0 + 1/K[0]*dataList.get(i).getA0Value());
			a1 = a1 + timedifferenceSeconds*(-1/K[1]*a1 + 1/K[1]*dataList.get(i).getA1Value());
			a2 = a2 + timedifferenceSeconds*(-1/K[2]*a2 + 1/K[2]*dataList.get(i).getA2Value());
			a3 = a3 + timedifferenceSeconds*(-1/K[3]*a3 + 1/K[3]*dataList.get(i).getA3Value());
			sa0 = sa0 + timedifferenceSeconds*(-1/K[4]*sa0 + 1/K[4]*dataList.get(i).getSa0Value());
			sa1 = sa1 + timedifferenceSeconds*(-1/K[5]*sa1 + 1/K[5]*dataList.get(i).getSa1Value());
			sa1d = sa1d + timedifferenceSeconds*(-1/K[6]*sa1d + 1/K[6]*dataList.get(i).getSa1dValue());
			sa2d = sa2d + timedifferenceSeconds*(-1/K[7]*sa2d + 1/K[7]*dataList.get(i).getSa2dValue());
			sa3d = sa3d + timedifferenceSeconds*(-1/K[8]*sa3d + 1/K[8]*dataList.get(i).getSa3dValue());
			
			// Save
			dataList.get(i).setA0Value(a0);
			dataList.get(i).setA1Value(a1);
			dataList.get(i).setA2Value(a2);
			dataList.get(i).setA3Value(a3);
			dataList.get(i).setSa0Value(sa0);
			dataList.get(i).setSa1Value(sa1);
			dataList.get(i).setSa1dValue(sa1d);
			dataList.get(i).setSa2dValue(sa2d);
			dataList.get(i).setSa3dValue(sa3d);
		}
	}
}
