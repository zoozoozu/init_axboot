package com.delcam.domain.mybatis;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.delcam.domain.BaseService;


@SuppressWarnings("rawtypes")
@Service
public class ExecOrderMyService extends BaseService {
	@Inject
	private ExecOrderMapper execOrderMapper;

	public List<HashMap> execOrderTargetList(HashMap rParam) {
		List<HashMap> rtnList = execOrderMapper.execOrderTargetList(rParam);
		return rtnList;
	}
	
}
