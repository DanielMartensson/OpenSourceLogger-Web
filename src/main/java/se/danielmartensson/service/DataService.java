package se.danielmartensson.service;

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
	
	public void deleteByJobName(String jobName) {
		dataRepository.deleteByJobName(jobName);
	}
		
	public List<Data> findByJobNameOrderByLocalDateTimeAscLimit(String jobName, long selectedOffset, long selectedLimit) {
		return dataRepository.findByJobNameOrderByLocalDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
	}
	
	public List<Data> findByJobNameOrderByLocalDateTimeAscLimitStep(String jobName, long selectedOffset, long selectedLimit, long step) {
		return dataRepository.findByJobNameOrderByLocalDateTimeAscLimitStep(jobName, selectedOffset, selectedLimit, step);
	}
	
	public void deleteByJobNameOrderByLocalDateTimeAscLimit(String jobName, long selectedOffset, long selectedLimit) {
		dataRepository.deleteByJobNameOrderByLocalDateTimeAscLimit(jobName, selectedOffset, selectedLimit);
	}

	public void deleteInBatch(List<Data> deleteTheseLists) {
		dataRepository.deleteInBatch(deleteTheseLists);
	}
	
	public void updateJobNameWhereJobName(String newJobName, String jobName) {
		dataRepository.updateJobNameWhereJobName(newJobName, jobName);
	}

	public Data findFirstByJobNameOrderByLocalDateTimeDesc(String jobName) {
		return dataRepository.findFirstByJobNameOrderByLocalDateTimeDesc(jobName);
	}

	public long countByJobName(String jobName) {
		return dataRepository.countByJobName(jobName);
	}
}