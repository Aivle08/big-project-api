package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.response.FileUploadResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    private final AmazonS3 s3Client;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicantRepository applicantRepository;

    @Value("${aws.s3.bucket}")
    public String bucketName;

    public S3Service(AmazonS3 s3Client, RecruitmentRepository recruitmentRepository, ApplicantRepository applicantRepository) {
        this.s3Client = s3Client;
        this.recruitmentRepository = recruitmentRepository;
        this.applicantRepository = applicantRepository;
    }

    public String uploadFile(MultipartFile file, String keyName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            System.out.println(bucketName);
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));

            return s3Client.getUrl(bucketName, keyName).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String uploadOneFile(MultipartFile file, Long recruitmentId) {
        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            FileUploadResponseDTO response = new FileUploadResponseDTO(
                    "badRequest",
                    "Only PDF files are allowed.",
                    null
            );
            throw new RuntimeException("error");
        }

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).get();
        Applicant applicant = Applicant.builder()
                .recruitment(recruitment).build();
        Applicant savedApplicant = applicantRepository.save(applicant);

        //file_name: recruitmentId_applicantId.pdf
        String newFileName = recruitmentId + "_" + savedApplicant.getId() + ".pdf";

        // S3에 파일 업로드
        String s3Url = this.uploadFile(file, newFileName);

        // Applicant 업데이트
        Applicant applicantWithFile = Applicant.builder()
                .recruitment(savedApplicant.getRecruitment())
                .id(savedApplicant.getId())
                .fileName(newFileName)
                .build();
        applicantRepository.save(applicantWithFile);

        return s3Url;
    }
}
