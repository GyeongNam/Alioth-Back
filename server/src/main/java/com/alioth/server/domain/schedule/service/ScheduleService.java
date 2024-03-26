package com.alioth.server.domain.schedule.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleCreateDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.schedule.dto.req.ScheduleUpdateDto;
import com.alioth.server.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TypeChange typeChange;

    private final SalesMemberRepository salesMemberRepository;

    @Autowired
    ScheduleService(
            ScheduleRepository scheduleRepository,
            TypeChange typeChange,
            SalesMemberRepository salesMemberRepository
    ){
        this.scheduleRepository = scheduleRepository;
        this.typeChange = typeChange;
        this.salesMemberRepository = salesMemberRepository;
    }

    public Schedule findById(Long scheduleId){
        return scheduleRepository.findById(scheduleId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    public ScheduleResDto save(ScheduleCreateDto scheduleCreateDto) {
        SalesMembers salesMembers = salesMemberRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("존재하지 않는 사원입니다."));
        return typeChange.ScheduleToScheduleResDto(
                scheduleRepository.save(
                        typeChange.ScheduleCreateDtoToSchedule(scheduleCreateDto, salesMembers)
                )
        );
    }

    public ScheduleResDto update(ScheduleUpdateDto scheduleUpdateDto) {
        Schedule schedule = this.findById(scheduleUpdateDto.scheduleId());
        schedule.update(scheduleUpdateDto);
        return typeChange.ScheduleToScheduleResDto(schedule);
    }

    public ScheduleResDto delete(Long scheduleId) {
        Schedule schedule =  this.findById(scheduleId);
        schedule.delete();
        return typeChange.ScheduleToScheduleResDto(schedule);
    }

    public List<ScheduleResDto> list() {
        SalesMembers salesMembers = salesMemberRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("존재하지 않는 사원입니다."));
        return scheduleRepository.findAllBySalesMembers(salesMembers)
                .stream()
                .map(typeChange::ScheduleToScheduleResDto)
                .collect(Collectors.toList());
    }
}
