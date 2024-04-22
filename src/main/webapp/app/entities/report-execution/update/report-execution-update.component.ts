import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';
import { ReportExecutionFormService, ReportExecutionFormGroup } from './report-execution-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-execution-update',
  templateUrl: './report-execution-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportExecutionUpdateComponent implements OnInit {
  isSaving = false;
  reportExecution: IReportExecution | null = null;

  reportsSharedCollection: IReport[] = [];

  protected reportExecutionService = inject(ReportExecutionService);
  protected reportExecutionFormService = inject(ReportExecutionFormService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportExecutionFormGroup = this.reportExecutionFormService.createReportExecutionFormGroup();

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportExecution }) => {
      this.reportExecution = reportExecution;
      if (reportExecution) {
        this.updateForm(reportExecution);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportExecution = this.reportExecutionFormService.getReportExecution(this.editForm);
    if (reportExecution.rid !== null) {
      this.subscribeToSaveResponse(this.reportExecutionService.update(reportExecution));
    } else {
      this.subscribeToSaveResponse(this.reportExecutionService.create(reportExecution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportExecution>>): void {
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

  protected updateForm(reportExecution: IReportExecution): void {
    this.reportExecution = reportExecution;
    this.reportExecutionFormService.resetForm(this.editForm, reportExecution);

    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      reportExecution.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.reportExecution?.report)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
