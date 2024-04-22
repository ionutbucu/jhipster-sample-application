import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IReportColumnMapping } from '../report-column-mapping.model';
import { ReportColumnMappingService } from '../service/report-column-mapping.service';
import { ReportColumnMappingFormService, ReportColumnMappingFormGroup } from './report-column-mapping-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-column-mapping-update',
  templateUrl: './report-column-mapping-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportColumnMappingUpdateComponent implements OnInit {
  isSaving = false;
  reportColumnMapping: IReportColumnMapping | null = null;

  reportsSharedCollection: IReport[] = [];

  protected reportColumnMappingService = inject(ReportColumnMappingService);
  protected reportColumnMappingFormService = inject(ReportColumnMappingFormService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportColumnMappingFormGroup = this.reportColumnMappingFormService.createReportColumnMappingFormGroup();

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportColumnMapping }) => {
      this.reportColumnMapping = reportColumnMapping;
      if (reportColumnMapping) {
        this.updateForm(reportColumnMapping);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportColumnMapping = this.reportColumnMappingFormService.getReportColumnMapping(this.editForm);
    if (reportColumnMapping.rid !== null) {
      this.subscribeToSaveResponse(this.reportColumnMappingService.update(reportColumnMapping));
    } else {
      this.subscribeToSaveResponse(this.reportColumnMappingService.create(reportColumnMapping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportColumnMapping>>): void {
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

  protected updateForm(reportColumnMapping: IReportColumnMapping): void {
    this.reportColumnMapping = reportColumnMapping;
    this.reportColumnMappingFormService.resetForm(this.editForm, reportColumnMapping);

    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      reportColumnMapping.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(
        map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.reportColumnMapping?.report)),
      )
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
