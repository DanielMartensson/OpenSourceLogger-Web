package se.danielmartensson.tools;

import java.util.List;

import se.danielmartensson.entities.Data;

// Same idea as https://github.com/DanielMartensson/Mataveid/blob/master/sourcecode/filtfilt2.m
public class Filtering {
	public static void movingAverageFiltering(List<Data> dataList, int[] points){
		for(int startIndex = 0; startIndex < dataList.size(); startIndex++) {
			dataList.get(startIndex).setA0(sum(startIndex, points[0], dataList, Data.Analog0));
			dataList.get(startIndex).setA1(sum(startIndex, points[1], dataList, Data.Analog1));
			dataList.get(startIndex).setA2(sum(startIndex, points[2], dataList, Data.Analog2));
			dataList.get(startIndex).setA3(sum(startIndex, points[3], dataList, Data.Analog3));
			dataList.get(startIndex).setSa0(sum(startIndex, points[4], dataList, Data.SigmaDelta0));
			dataList.get(startIndex).setSa1(sum(startIndex, points[5], dataList, Data.SigmaDelta1));
			dataList.get(startIndex).setSa1d(sum(startIndex, points[6], dataList, Data.SigmaDeltaDifferential1));
			dataList.get(startIndex).setSa2d(sum(startIndex, points[7], dataList, Data.SigmaDeltaDifferential2));
			dataList.get(startIndex).setSa3d(sum(startIndex, points[8], dataList, Data.SigmaDeltaDifferential3));
		}
	}

	private static float sum(int startIndex, int point, List<Data> dataList, String variable) {
		float sumValue = 0;
		int stopIndex = startIndex + point <= dataList.size() ? startIndex + point :  dataList.size();
		int amoutOfSum = 0;
		for(int i = startIndex; i < stopIndex; i++) {
			switch(variable) {
			case Data.Analog0:
				sumValue += dataList.get(i).getA0();
				break;
			case Data.Analog1:
				sumValue += dataList.get(i).getA1();
				break;
			case Data.Analog2:
				sumValue += dataList.get(i).getA2();
				break;
			case Data.Analog3:
				sumValue += dataList.get(i).getA3();
				break;
			case Data.SigmaDelta0:
				sumValue += dataList.get(i).getSa0();
				break;
			case Data.SigmaDelta1:
				sumValue += dataList.get(i).getSa1();
				break;
			case Data.SigmaDeltaDifferential1:
				sumValue += dataList.get(i).getSa1d();
				break;
			case Data.SigmaDeltaDifferential2:
				sumValue += dataList.get(i).getSa2d();
				break;
			case Data.SigmaDeltaDifferential3:
				sumValue += dataList.get(i).getSa3d();
				break;
			}
			amoutOfSum++;
		}
		return sumValue/amoutOfSum;
	}
}
