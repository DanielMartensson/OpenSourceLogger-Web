package se.danielmartensson.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Data;
import se.danielmartensson.repositories.DataRepository;

@Service
public class DataService {

	private final DataRepository dataRepository;

	public DataService(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	public List<Data> findAll() {
		return dataRepository.findAll();
	}

	public Data save(Data data) {
		return dataRepository.save(data);
	}
	
	public List<Data> saveAll(List<Data> dataList) {
		return dataRepository.saveAll(dataList);
	}

	// We only want data for each name
	public Collection<Data> findAllNames() {
		List<Data> jobList = new ArrayList<Data>();
		List<String> jobName = new ArrayList<String>();
		for (Data data : findAll()) {
			if (!jobName.contains(data.getJobName())) {
				jobList.add(data);
				jobName.add(data.getJobName());
			}
		}
		return jobList;
	}

	public List<Data> findByJobName(String jobName) {
		return dataRepository.findByJobName(jobName);
	}

	public List<Data> findByJobNameOrderByDateTime(String jobName) {
		return dataRepository.findByJobNameOrderByDateTime(jobName);
	}

	public void deleteInBatch(List<Data> deleteTheseLists) {
		dataRepository.deleteInBatch(deleteTheseLists);
	}

}
