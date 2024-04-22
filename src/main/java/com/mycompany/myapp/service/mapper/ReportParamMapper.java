package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportParam;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.dto.ReportParamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportParam} and its DTO {@link ReportParamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportParamMapper extends EntityMapper<ReportParamDTO, ReportParam> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportParamDTO toDto(ReportParam s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
