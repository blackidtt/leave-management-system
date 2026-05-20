package com.example.leave.service;

import com.example.leave.dto.CreateLeaveRequestDto;
import com.example.leave.entity.Employee;
import com.example.leave.entity.LeaveRequest;
import com.example.leave.enums.LeaveStatus;
import com.example.leave.repository.EmployeeRepository;
import com.example.leave.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public LeaveRequest create(CreateLeaveRequestDto dto) {

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        long daysRequested = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;

        if (daysRequested > employee.getAnnualLeaveQuota()) {
            throw new RuntimeException("Insufficient leave quota. Requested: " + daysRequested + ", Available: " + employee.getAnnualLeaveQuota());
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepository.save(leave);
    }

    public Page<LeaveRequest> getHistory(Long employeeId, Pageable pageable) {
        return leaveRequestRepository.findByEmployeeId(employeeId, pageable);
    }

    public Page<LeaveRequest> getAll(Pageable pageable) {
        return leaveRequestRepository.findAll(pageable);
    }

    @Transactional
    public LeaveRequest approve(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }

        Employee employee = leave.getEmployee();
        long daysRequested = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        if (employee.getAnnualLeaveQuota() < daysRequested) {
            throw new RuntimeException("Cannot approve: Employee has insufficient quota");
        }

        employee.setAnnualLeaveQuota(employee.getAnnualLeaveQuota() - (int) daysRequested);
        employeeRepository.save(employee);

        leave.setStatus(LeaveStatus.APPROVED);

        return leaveRequestRepository.save(leave);
    }

    public LeaveRequest reject(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.REJECTED);

        return leaveRequestRepository.save(leave);
    }
}