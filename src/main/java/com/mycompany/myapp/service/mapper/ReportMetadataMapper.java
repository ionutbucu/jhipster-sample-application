package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ReportMetadata;
import com.mycompany.myapp.service.dto.ReportMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportMetadata} and its DTO {@link ReportMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMetadataMapper extends EntityMapper<ReportMetadataDTO, ReportMetadata> {}
