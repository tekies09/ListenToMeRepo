package com.ssafy.a605.service;

import com.ssafy.a605.model.dto.ScheduleDto;
import com.ssafy.a605.model.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    public Page<ScheduleDto> getCounselorApprovedSchedule(String userEmail, Pageable pageRequest) throws Exception;
    public Page<ScheduleDto> getCounselorEndedSchedule(String userEmail, Pageable pageRequest) throws Exception;
    public Page<ScheduleDto> getClientApprovedSchedule(String userEmail, Pageable pageRequest) throws Exception;
    public Page<ScheduleDto> getClientEndedSchedule(String userEmail, Pageable pageRequest) throws Exception;
    public boolean setScheduleTime(ScheduleDto scheduleDto, String userEmail) throws Exception;
    public boolean checkScheduleTime(LocalDateTime dateTime, String userEmail) throws Exception;
    public List<ScheduleDto> getCounselorSchedule(String userEmail) throws Exception;
    public boolean requestCounseling(String userEmail, int scheduleId) throws Exception;
    public boolean acceptCounseling(String userEmail, int scheduleId) throws Exception;
    public boolean endCounseling(String userEmail, int scheduleId) throws Exception;
    public Schedule getCounseling(int scheduleId) throws Exception;
}