package com.delcam.domain.mybatis;

import java.util.HashMap;
import java.util.List;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

public interface ProgressbarMapper extends MyBatisMapper {

	List<HashMap>  getExecOrderList1 (HashMap<String, Long> param);
	
	List<HashMap>  getExecOrderList2 (HashMap<String, Long> param);
}
