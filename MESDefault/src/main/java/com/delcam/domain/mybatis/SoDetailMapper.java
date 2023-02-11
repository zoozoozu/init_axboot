package com.delcam.domain.mybatis;

import java.util.HashMap;
import java.util.List;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

public interface SoDetailMapper extends MyBatisMapper {
	
	List<HashMap> getShippingTargetList(HashMap param); 
}
