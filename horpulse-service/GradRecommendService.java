package com.hotpulse.service.grad;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.dto.grad.RecommendQueryDTO;
import com.hotpulse.entity.grad.*;
import com.hotpulse.mapper.grad.*;
import com.hotpulse.vo.grad.RecommendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradRecommendService {

    private final GradSchoolMapper schoolMapper;
    private final GradMajorMapper majorMapper;
    private final GradAdmissionMapper admissionMapper;
    private final GradTutorMapper tutorMapper;

    public List<RecommendVO> recommend(RecommendQueryDTO query) {
        // 1. Find matching majors
        List<GradMajor> majors;
        if (query.getMajorKeyword() != null && !query.getMajorKeyword().isBlank()) {
            majors = majorMapper.selectList(
                new LambdaQueryWrapper<GradMajor>()
                    .like(GradMajor::getName, query.getMajorKeyword())
            );
            if (majors.isEmpty()) {
                majors = majorMapper.selectList(null);
            }
        } else {
            majors = majorMapper.selectList(null);
        }

        if (query.getDegreeType() != null) {
            majors = majors.stream()
                .filter(m -> query.getDegreeType().equals(m.getDegreeType()))
                .collect(Collectors.toList());
        }

        // 2. Find admission data for each major
        List<RecommendVO> results = new ArrayList<>();
        for (GradMajor major : majors) {
            var adQuery = new LambdaQueryWrapper<GradAdmission>()
                .eq(GradAdmission::getMajorId, major.getId())
                .orderByDesc(GradAdmission::getYear)
                .last("LIMIT 2");

            List<GradAdmission> admissions = admissionMapper.selectList(adQuery);
            if (admissions.isEmpty()) continue;

            GradAdmission latest = admissions.get(0);

            // 3. Get school info
            GradSchool school = schoolMapper.selectById(latest.getSchoolId());
            if (school == null) continue;

            // Filter by province / school tag
            if (query.getProvince() != null && !school.getProvince().equals(query.getProvince())) continue;
            if (query.getSchoolTag() != null && !school.getTag().equals(query.getSchoolTag())) continue;

            // 4. Calculate risk level
            String riskLevel = "稳";
            String reason = "";
            if (query.getScore() != null && latest.getAvgScore() > 0) {
                int diff = query.getScore() - latest.getAvgScore();
                if (diff >= 20) { riskLevel = "保"; reason = "你的预估分高于录取均分 " + diff + " 分，录取概率高"; }
                else if (diff >= 0) { riskLevel = "稳"; reason = "你的预估分接近或略高于录取均分，录取概率较大"; }
                else if (diff >= -20) { riskLevel = "冲"; reason = "你的预估分低于录取均分 " + (-diff) + " 分，冲刺有一定机会"; }
                else { riskLevel = "冲"; reason = "录取难度较大，建议作为冲刺目标"; }
            } else {
                reason = school.getTag() + "院校，" + major.getName() + "方向实力突出";
            }

            // 5. Find tutors
            List<GradTutor> tutors = tutorMapper.selectList(
                new LambdaQueryWrapper<GradTutor>()
                    .eq(GradTutor::getSchoolId, school.getId())
                    .eq(GradTutor::getMajorId, major.getId())
            );

            RecommendVO vo = new RecommendVO();
            vo.setSchoolName(school.getName());
            vo.setSchoolTag(school.getTag());
            vo.setCity(school.getCity());
            vo.setMajorName(major.getName());
            vo.setDegreeType(major.getDegreeType());
            vo.setExamSubjects(latest.getExamSubjects());
            vo.setTotalScoreLine(latest.getTotalScoreLine());
            vo.setAvgScore(latest.getAvgScore());
            vo.setApplicantCount(latest.getApplicantCount());
            vo.setAdmitCount(latest.getAdmitCount());
            if (latest.getApplicantCount() > 0) {
                vo.setAdmitRatio(Math.round((double) latest.getAdmitCount() / latest.getApplicantCount() * 10000.0) / 100.0);
            }
            vo.setRiskLevel(riskLevel);
            vo.setReason(reason);
            vo.setTutors(tutors.stream().map(t -> t.getName() + " " + t.getTitle() + " · " + t.getResearchDirection()).collect(Collectors.toList()));
            results.add(vo);
        }

        // Sort: 保 first, then 稳, then 冲
        results.sort(Comparator.comparingInt(r -> {
            return switch (r.getRiskLevel()) {
                case "保" -> 0;
                case "稳" -> 1;
                case "冲" -> 2;
                default -> 3;
            };
        }));

        return results;
    }
}
