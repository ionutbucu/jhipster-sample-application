package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ReportDataSource;
import com.mycompany.myapp.service.dto.ReportDataSourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDataSource} and its DTO {@link ReportDataSourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportDataSourceMapper extends EntityMapper<ReportDataSourceDTO, ReportDataSource> {}
