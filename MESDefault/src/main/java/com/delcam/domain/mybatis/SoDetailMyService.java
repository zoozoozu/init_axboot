package com.delcam.domain.mybatis;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.delcam.domain.BaseService;

@SuppressWarnings("rawtypes")
@Service
public class SoDetailMyService extends BaseService {

	@Inject
	private SoDetailMapper soDetailMyMapper;

	public List<HashMap> getShippingTargetList(HashMap param) {

		List<HashMap> rtnList = soDetailMyMapper.getShippingTargetList(param);

		return rtnList;
	}
}
