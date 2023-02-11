package com.delcam.domain.init;

import org.springframework.stereotype.Service;
import com.delcam.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import java.util.List;

@Service
public class SequenceService extends BaseService<Sequence, String> {
    private SequenceRepository sequenceRepository;

    @Inject
    public SequenceService(SequenceRepository sequenceRepository) {
        super(sequenceRepository);
        this.sequenceRepository = sequenceRepository;
    }

    public List<Sequence> gets(RequestParams<Sequence> requestParams) {
        return findAll();
    }
}