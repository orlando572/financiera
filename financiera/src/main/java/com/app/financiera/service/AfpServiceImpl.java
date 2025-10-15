package com.app.financiera.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.financiera.entity.Afp;
import com.app.financiera.repository.AfpRepository;

@Service
public class AfpServiceImpl implements AfpService {

    @Autowired
    private AfpRepository afpRepository;

    @Override
    public List<Afp> listarAfps() {
        return afpRepository.findAll();
    }
}