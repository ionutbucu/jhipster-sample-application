import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReportDataSource } from 'app/entities/report-data-source/report-data-source.model';
import { ReportDataSourceService } from 'app/entities/report-data-source/service/report-data-source.service';
import { IReportMetadata } from 'app/entities/report-metadata/report-metadata.model';
import { ReportMetadataService } from 'app/entities/report-metadata/service/report-metadata.service';
import { QueryType } from 'app/entities/enumerations/query-type.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ReportService } from '../service/report.service';
import { IReport } from '../report.model';
import { ReportFormService, ReportFormGroup } from './report-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;
  report: IReport | null = null;
  queryTypeValues = Object.keys(QueryType);
  reportTypeValues = Object.keys(ReportType);

  datasourcesCollection: IReportDataSource[] = [];
  metadataCollection: IReportMetadata[] = [];

  protected reportService = inject(ReportService);
  protected reportFormService = inject(ReportFormService);
  protected reportDataSourceService = inject(ReportDataSourceService);
  protected reportMetadataService = inject(ReportMetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportFormGroup = this.reportFormService.createReportFormGroup();

  compareReportDataSource = (o1: IReportDataSource | null, o2: IReportDataSource | null): boolean =>
    this.reportDataSourceService.compareReportDataSource(o1, o2);

  compareReportMetadata = (o1: IReportMetadata | null, o2: IReportMetadata | null): boolean =>
    this.reportMetadataService.compareReportMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ report }) => {
      this.report = report;
      if (report) {
        this.updateForm(report);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const report = this.reportFormService.getReport(this.editForm);
    if (report.rid !== null) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(report: IReport): void {
    this.report = report;
    this.reportFormService.resetForm(this.editForm, report);

    this.datasourcesCollection = this.reportDataSourceService.addReportDataSourceToCollectionIfMissing<IReportDataSource>(
      this.datasourcesCollection,
      report.datasource,
    );
    this.metadataCollection = this.reportMetadataService.addReportMetadataToCollectionIfMissing<IReportMetadata>(
      this.metadataCollection,
      report.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportDataSourceService
      .query({ filter: 'report-is-null' })
      .pipe(map((res: HttpResponse<IReportDataSource[]>) => res.body ?? []))
      .pipe(
        map((reportDataSources: IReportDataSource[]) =>
          this.reportDataSourceService.addReportDataSourceToCollectionIfMissing<IReportDataSource>(
            reportDataSources,
            this.report?.datasource,
          ),
        ),
      )
      .subscribe((reportDataSources: IReportDataSource[]) => (this.datasourcesCollection = reportDataSources));

    this.reportMetadataService
      .query({ filter: 'report-is-null' })
      .pipe(map((res: HttpResponse<IReportMetadata[]>) => res.body ?? []))
      .pipe(
        map((reportMetadata: IReportMetadata[]) =>
          this.reportMetadataService.addReportMetadataToCollectionIfMissing<IReportMetadata>(reportMetadata, this.report?.metadata),
        ),
      )
      .subscribe((reportMetadata: IReportMetadata[]) => (this.metadataCollection = reportMetadata));
  }
}
