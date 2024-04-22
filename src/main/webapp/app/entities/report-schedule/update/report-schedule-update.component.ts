import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IReportSchedule } from '../report-schedule.model';
import { ReportScheduleService } from '../service/report-schedule.service';
import { ReportScheduleFormService, ReportScheduleFormGroup } from './report-schedule-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-schedule-update',
  templateUrl: './report-schedule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportScheduleUpdateComponent implements OnInit {
  isSaving = false;
  reportSchedule: IReportSchedule | null = null;

  reportsSharedCollection: IReport[] = [];

  protected reportScheduleService = inject(ReportScheduleService);
  protected reportScheduleFormService = inject(ReportScheduleFormService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportScheduleFormGroup = this.reportScheduleFormService.createReportScheduleFormGroup();

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportSchedule }) => {
      this.reportSchedule = reportSchedule;
      if (reportSchedule) {
        this.updateForm(reportSchedule);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportSchedule = this.reportScheduleFormService.getReportSchedule(this.editForm);
    if (reportSchedule.rid !== null) {
      this.subscribeToSaveResponse(this.reportScheduleService.update(reportSchedule));
    } else {
      this.subscribeToSaveResponse(this.reportScheduleService.create(reportSchedule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportSchedule>>): void {
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

  protected updateForm(reportSchedule: IReportSchedule): void {
    this.reportSchedule = reportSchedule;
    this.reportScheduleFormService.resetForm(this.editForm, reportSchedule);

    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      reportSchedule.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.reportSchedule?.report)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
