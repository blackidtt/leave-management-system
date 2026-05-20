package com.example.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data transfer object untuk membuat pengajuan cuti")
public class CreateLeaveRequestDto {

    @Schema(description = "ID unik karyawan", example = "101")
    private Long employeeId;

    @Schema(description = "Tanggal mulai cuti", example = "2023-12-01")
    private LocalDate startDate;

    @Schema(description = "Tanggal berakhir cuti", example = "2023-12-05")
    private LocalDate endDate;

    @Schema(description = "Alasan pengajuan cuti", example = "Acara keluarga")
    private String reason;
}