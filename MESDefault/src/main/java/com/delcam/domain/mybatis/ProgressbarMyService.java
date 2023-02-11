package com.delcam.domain.mybatis;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.delcam.domain.BaseService;

@SuppressWarnings("rawtypes")
@Service
public class ProgressbarMyService extends BaseService{

	@Inject
	private ProgressbarMapper progressbarMapper;
	
	public List<HashMap> getExecHistory1(HashMap rParam) {
		List<HashMap> rtnList = progressbarMapper.getExecOrderList1(rParam);
		return rtnList;
	}

	public List<HashMap> getExecHistory2(HashMap rParam) {
		List<HashMap> rtnList = progressbarMapper.getExecOrderList2(rParam);
		return rtnList;
	}
	
}
