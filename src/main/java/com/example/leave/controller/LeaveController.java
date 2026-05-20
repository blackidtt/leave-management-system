package com.example.leave.controller;

import com.example.leave.dto.CreateLeaveRequestDto;
import com.example.leave.dto.LeaveActionDto;
import com.example.leave.entity.LeaveRequest;
import com.example.leave.filter.LeaveFilter;
import com.example.leave.filter.PageFilter;
import com.example.leave.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Endpoints untuk mengelola pengajuan cuti karyawan")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    @Operation(summary = "Membuat pengajuan cuti baru")
    public LeaveRequest create(@RequestBody CreateLeaveRequestDto dto) {
        return leaveService.create(dto);
    }

    @PostMapping("/history")
    @Operation(summary = "Mendapatkan riwayat cuti berdasarkan ID karyawan dengan pagination")
    public Page<LeaveRequest> history(@RequestBody LeaveFilter filter) {
        return leaveService.getHistory(
            filter.getEmployeeId(), 
            PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortBy()))
        );
    }

    @PostMapping("/all")
    @Operation(summary = "Mendapatkan semua daftar pengajuan cuti dengan pagination")
    public Page<LeaveRequest> getAll(@RequestBody PageFilter filter) {
        return leaveService.getAll(
            PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortBy()))
        );
    }

    @PostMapping("/approve")
    @Operation(summary = "Menyetujui pengajuan cuti")
    public LeaveRequest approve(@RequestBody LeaveActionDto filter) {
        return leaveService.approve(filter.getId());
    }

    @PostMapping("/reject")
    @Operation(summary = "Menolak pengajuan cuti")
    public LeaveRequest reject(@RequestBody LeaveActionDto filter) {
        return leaveService.reject(filter.getId());
    }
}