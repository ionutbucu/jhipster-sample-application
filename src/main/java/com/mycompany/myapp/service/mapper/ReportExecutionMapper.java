package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportExecution;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.dto.ReportExecutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportExecution} and its DTO {@link ReportExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportExecutionMapper extends EntityMapper<ReportExecutionDTO, ReportExecution> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportExecutionDTO toDto(ReportExecution s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
