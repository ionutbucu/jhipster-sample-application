package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.ReportDataSource;
import com.mycompany.myapp.domain.ReportMetadata;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.dto.ReportDataSourceDTO;
import com.mycompany.myapp.service.dto.ReportMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "datasource", source = "datasource", qualifiedByName = "reportDataSourceRid")
    @Mapping(target = "metadata", source = "metadata", qualifiedByName = "reportMetadataRid")
    ReportDTO toDto(Report s);

    @Named("reportDataSourceRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDataSourceDTO toDtoReportDataSourceRid(ReportDataSource reportDataSource);

    @Named("reportMetadataRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportMetadataDTO toDtoReportMetadataRid(ReportMetadata reportMetadata);
}
