package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportColumnMapping;
import com.mycompany.myapp.service.dto.ReportColumnMappingDTO;
import com.mycompany.myapp.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportColumnMapping} and its DTO {@link ReportColumnMappingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportColumnMappingMapper extends EntityMapper<ReportColumnMappingDTO, ReportColumnMapping> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportColumnMappingDTO toDto(ReportColumnMapping s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
