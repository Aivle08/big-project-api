package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.ResumeRetriever;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.ResumeRetrieverRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResumeRetrieverService {

    private final ResumeRetrieverRepository resumeRetrieverRepository;
    private final ApplicantRepository applicantRepository;

    public ResumeRetrieverService(ResumeRetrieverRepository resumeRetrieverRepository, ApplicantRepository applicantRepository) {
        this.resumeRetrieverRepository = resumeRetrieverRepository;
        this.applicantRepository = applicantRepository;
    }


    public List<ResumeRetriever> getResumeRetriever(Long applicant_id) {
        List<ResumeRetriever> resumeRetrievers = resumeRetrieverRepository.findByApplicant_Id(applicant_id);

        return resumeRetrievers;
    }

    public ResumeRetriever createResumeRetriever(Long applicant_id, ResumeRetriever resumeRetriever) {
        System.out.println("t3");

        ResumeRetriever retriever = ResumeRetriever.builder()
                .applicant(applicantRepository.findById(applicant_id).get())
                .chunk(resumeRetriever.getChunk())
                .build();
        System.out.println("t2");

        return resumeRetrieverRepository.save(retriever);
    }
}