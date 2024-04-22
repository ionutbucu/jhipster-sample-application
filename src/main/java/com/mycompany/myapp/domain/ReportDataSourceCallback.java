package com.mycompany.myapp.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReportDataSourceCallback implements AfterSaveCallback<ReportDataSource>, AfterConvertCallback<ReportDataSource> {

    @Override
    public Publisher<ReportDataSource> onAfterConvert(ReportDataSource entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ReportDataSource> onAfterSave(ReportDataSource entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
