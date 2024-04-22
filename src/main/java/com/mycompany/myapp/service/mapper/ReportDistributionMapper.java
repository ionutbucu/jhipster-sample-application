package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportDistribution;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.dto.ReportDistributionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDistribution} and its DTO {@link ReportDistributionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportDistributionMapper extends EntityMapper<ReportDistributionDTO, ReportDistribution> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportDistributionDTO toDto(ReportDistribution s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
