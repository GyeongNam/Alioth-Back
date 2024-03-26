package com.alioth.server.domain.schedule.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.ScheduleCreateDto;
import com.alioth.server.domain.schedule.dto.ScheduleResDto;
import com.alioth.server.domain.schedule.dto.ScheduleUpdateDto;
import com.alioth.server.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TypeChange typeChange;

    @Autowired
    ScheduleService(
            ScheduleRepository scheduleRepository,
            TypeChange typeChange
    ){
        this.scheduleRepository = scheduleRepository;
        this.typeChange = typeChange;
    }

    public Schedule findById(Long scheduleId){
        return scheduleRepository.findById(scheduleId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    public ScheduleResDto save(ScheduleCreateDto scheduleCreateDto) {
        return typeChange.scheduleToScheduleResDto(
                scheduleRepository.save(
                        typeChange.ScheduleCreateDtoToSchedule(scheduleCreateDto)
                )
        );
    }

    public ScheduleResDto update(ScheduleUpdateDto scheduleUpdateDto) {
        Schedule schedule = this.findById(scheduleUpdateDto.scheduleId());
        schedule.update(scheduleUpdateDto);
        return typeChange.scheduleToScheduleResDto(schedule);
    }

    public ScheduleResDto delete(Long scheduleId) {
        Schedule schedule =  this.findById(scheduleId);
        schedule.delete();
        return typeChange.scheduleToScheduleResDto(schedule);
    }

    public List<ScheduleResDto> list(long memberId) {
        return scheduleRepository.findAllByMemberId(memberId)
                .stream()
                .map(typeChange::scheduleToScheduleResDto)
                .collect(Collectors.toList());
    }
}
