package com.aivle08.big_project_api.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {
    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public void sendPostRequest() {
        // 요청 본문 데이터 (Map)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("job", "IT영업");
        requestBody.put("eval_item", "인재상");
        requestBody.put("eval_item_content", """
                인재상
                1. 고객(Customer Value)
                • 기준:
                o 고객의 요구를 깊이 이해하고, 문제 해결을 위해 적극적으로 노력한 경험.
                o 고객 중심 사고를 증명할 수 있는 사례(프로젝트, 대외활동 등).
                • 평가 요소:
                o 대외활동, 인턴 경험, 프로젝트에서 고객의 니즈를 반영한 문제 해결 경험.
                o 새로운 고객 경험 제공을 위한 아이디어를 실제로 실행한 사례.
                
                2. 역량(Excellence)
                • 기준:
                o 직무와 관련된 전문 지식 및 기술 보유(예: 5G, AI, IoT, 데이터 분석).
                o 문제 해결 능력을 입증할 수 있는 수상 내역 또는 프로젝트 경험.
                • 평가 요소:
                o 자격증: 직무와 연관된 자격증 보유(정보처리기사, 네트워크관리사 등).
                o 경진대회: 직무 관련 공모전이나 대회에서의 수상 경험.
                o 기술 스택: 프로그래밍, 데이터 분석, 클라우드 기술 등 실무에 필요한 기술 활용 능력.
                
                3. 실질(Practical Outcome)
                • 기준:
                o 화려한 스펙보다는 실제 성과를 창출한 경험 강조.
                o 본인의 역할과 기여도를 명확히 설명할 수 있는 사례.
                • 평가 요소:
                o 실무 경험: 인턴십, 산학 협력, 실질적인 성과를 창출한 프로젝트 경험.
                o 성과 중심 활동: KPI를 설정하고 달성한 경험.
                o 문서화 능력: 실질적 결과물을 보고서, 발표자료로 정리한 능력.
                
                4. 화합(Togetherness)
                • 기준:
                o 팀워크와 협업 경험이 풍부하며, 다양한 사람들과 조화를 이룬 사례.
                o 갈등 상황에서도 문제를 해결하고 합의점을 찾은 경험.
                • 평가 요소:
                o 팀 프로젝트에서 역할 수행과 성과 기여 사례.
                o 협력 과정에서 나타난 리더십, 커뮤니케이션 능력.
                o 다문화 또는 다양한 배경의 사람들과 함께 일한 경험(해외 활동, 글로벌 프로젝트).
                """);
        requestBody.put("applicant_id", 1);

        // API 호출
        String response = webClient.post()
                .uri("/score") // 엔드포인트 설정
                .contentType(MediaType.APPLICATION_JSON) // JSON 요청
                .bodyValue(requestBody) // 요청 본문 설정
                .retrieve()
                .bodyToMono(String.class) // 응답을 String으로 변환
                .block(); // 동기 방식으로 처리

        // 응답 출력
        System.out.println("Response: " + response);
    }

}
