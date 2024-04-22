package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportSchedule;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.dto.ReportScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportSchedule} and its DTO {@link ReportScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportScheduleMapper extends EntityMapper<ReportScheduleDTO, ReportSchedule> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportScheduleDTO toDto(ReportSchedule s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
