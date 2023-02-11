package com.delcam.domain.init;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.delcam.domain.SimpleJpaModel;
import com.delcam.domain.program.Program;

import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "COMMON_SEQUENCE")
@Comment(value = "")
@Alias("sequence")
public class Sequence extends SimpleJpaModel<String> {

	@Id
	@Column(name = "SEQ_NAME", length = 50, nullable = false)
	@Comment(value = "SEQ구분")
	private String seqName;

	@Column(name = "MAX_ID", precision = 19)
	@Comment(value = "AUTO GENERATED NUMBER")
	private Long maxId;

	@Column(name = "REMARK", length = 2000)
	@Comment(value = "비고")
	private String remark;


    @Override
    public String getId() {
        return seqName;
    }
    
    public static Sequence of(String seqName, String remark ) {
    	Sequence seq = new Sequence();
    	seq.setSeqName(seqName);
    	seq.setRemark(remark);
        seq.setMaxId(1L);     //기본값 1부터 시작
       
        return seq;
    }
}