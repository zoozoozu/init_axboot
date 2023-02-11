package com.delcam.domain.mybatis;
 
import java.util.HashMap;
import java.util.List;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
 
 
public interface ExecOrderMapper extends MyBatisMapper {
	              
	List<HashMap>  execOrderTargetList (HashMap<String, String> param);   //실행 대상 및 결과
    
} 