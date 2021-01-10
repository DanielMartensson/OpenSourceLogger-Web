package se.danielmartensson.tools;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import se.danielmartensson.entities.Data;

// Same idea as https://github.com/DanielMartensson/Mataveid/blob/master/sourcecode/filtfilt2.m
public class Filtering {
	public static void filtfilt(List<Data> dataList, double[] filtfiltKs){
		eulerSimulation(dataList, filtfiltKs);
		Collections.reverse(dataList); // Flip to reverse
		eulerSimulation(dataList, filtfiltKs);
		Collections.reverse(dataList); // Flip back to normal again
	}

	private static void eulerSimulation(List<Data> dataList, double[] filtfiltKs) {
		// Initial states
		float a0 = dataList.get(0).getA0();
		float a1 = dataList.get(0).getA1();
		float a2 = dataList.get(0).getA2();
		float a3 = dataList.get(0).getA3();
		float sa0 = dataList.get(0).getSa0();
		float sa1 = dataList.get(0).getSa1();
		float sa1d = dataList.get(0).getSa1d();
		float sa2d = dataList.get(0).getSa2d();
		float sa3d = dataList.get(0).getSa3d();
		
		// Copy over
		float[] K = new float[filtfiltKs.length];
		for(int i = 0; i < filtfiltKs.length; i++)
			K[i] = (float) filtfiltKs[i];
		
		// Euler simulation
		for(int i = 1; i < dataList.size(); i++) {
			// Find time difference in seconds
			long timedifferenceMilliseconds = ChronoUnit.MILLIS.between(dataList.get(i-1).getDateTime(), dataList.get(i).getDateTime());
			float timedifferenceSeconds = (float) ((timedifferenceMilliseconds) / 1000.0);
			
			// Compute
			a0 = a0 + timedifferenceSeconds*(-1/K[0]*a0 + 1/K[0]*dataList.get(i).getA0());
			a1 = a1 + timedifferenceSeconds*(-1/K[1]*a1 + 1/K[1]*dataList.get(i).getA1());
			a2 = a2 + timedifferenceSeconds*(-1/K[2]*a2 + 1/K[2]*dataList.get(i).getA2());
			a3 = a3 + timedifferenceSeconds*(-1/K[3]*a3 + 1/K[3]*dataList.get(i).getA3());
			sa0 = sa0 + timedifferenceSeconds*(-1/K[4]*sa0 + 1/K[4]*dataList.get(i).getSa0());
			sa1 = sa1 + timedifferenceSeconds*(-1/K[5]*sa1 + 1/K[5]*dataList.get(i).getSa1());
			sa1d = sa1d + timedifferenceSeconds*(-1/K[6]*sa1d + 1/K[6]*dataList.get(i).getSa1d());
			sa2d = sa2d + timedifferenceSeconds*(-1/K[7]*sa2d + 1/K[7]*dataList.get(i).getSa2d());
			sa3d = sa3d + timedifferenceSeconds*(-1/K[8]*sa3d + 1/K[8]*dataList.get(i).getSa3d());
			
			// Save
			dataList.get(i).setA0(a0);
			dataList.get(i).setA1(a1);
			dataList.get(i).setA2(a2);
			dataList.get(i).setA3(a3);
			dataList.get(i).setSa0(sa0);
			dataList.get(i).setSa1(sa1);
			dataList.get(i).setSa1d(sa1d);
			dataList.get(i).setSa2d(sa2d);
			dataList.get(i).setSa3d(sa3d);
		}
	}
}
