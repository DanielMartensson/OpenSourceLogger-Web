package se.danielmartensson.tools;

import java.util.List;

import se.danielmartensson.entities.Data;

// Same idea as https://github.com/DanielMartensson/Mataveid/blob/master/sourcecode/filtfilt2.m
public class Filtering {
	public static void movingAverageFiltering(List<Data> dataList, int[] points){
		for(int startIndex = 0; startIndex < dataList.size(); startIndex++) {
			dataList.get(startIndex).setA0(sum(startIndex, points[0], dataList, "A0"));
			dataList.get(startIndex).setA1(sum(startIndex, points[1], dataList, "A1"));
			dataList.get(startIndex).setA2(sum(startIndex, points[2], dataList, "A2"));
			dataList.get(startIndex).setA3(sum(startIndex, points[3], dataList, "A3"));
			dataList.get(startIndex).setSa0(sum(startIndex, points[4], dataList, "SA0"));
			dataList.get(startIndex).setSa1(sum(startIndex, points[5], dataList, "SA1"));
			dataList.get(startIndex).setSa1d(sum(startIndex, points[6], dataList, "SA1D"));
			dataList.get(startIndex).setSa2d(sum(startIndex, points[7], dataList, "SA2D"));
			dataList.get(startIndex).setSa3d(sum(startIndex, points[8], dataList, "SA3D"));
		}
	}

	private static float sum(int startIndex, int point, List<Data> dataList, String variable) {
		float sumValue = 0;
		int stopIndex = startIndex + point <= dataList.size() ? startIndex + point :  dataList.size();
		int amoutOfSum = 0;
		for(int i = startIndex; i < stopIndex; i++) {
			switch(variable) {
			case "A0":
				sumValue += dataList.get(i).getA0();
				break;
			case "A1":
				sumValue += dataList.get(i).getA1();
				break;
			case "A2":
				sumValue += dataList.get(i).getA2();
				break;
			case "A3":
				sumValue += dataList.get(i).getA3();
				break;
			case "SA0":
				sumValue += dataList.get(i).getSa0();
				break;
			case "SA1":
				sumValue += dataList.get(i).getSa1();
				break;
			case "SA1D":
				sumValue += dataList.get(i).getSa1d();
				break;
			case "SA2D":
				sumValue += dataList.get(i).getSa2d();
				break;
			case "SA3D":
				sumValue += dataList.get(i).getSa3d();
				break;
			}
			amoutOfSum++;
		}
		return sumValue/amoutOfSum;
	}
}
