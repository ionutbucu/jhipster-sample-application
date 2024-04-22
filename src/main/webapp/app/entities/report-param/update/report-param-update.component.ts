import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IReportParam } from '../report-param.model';
import { ReportParamService } from '../service/report-param.service';
import { ReportParamFormService, ReportParamFormGroup } from './report-param-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-param-update',
  templateUrl: './report-param-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportParamUpdateComponent implements OnInit {
  isSaving = false;
  reportParam: IReportParam | null = null;

  reportsSharedCollection: IReport[] = [];

  protected reportParamService = inject(ReportParamService);
  protected reportParamFormService = inject(ReportParamFormService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportParamFormGroup = this.reportParamFormService.createReportParamFormGroup();

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportParam }) => {
      this.reportParam = reportParam;
      if (reportParam) {
        this.updateForm(reportParam);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportParam = this.reportParamFormService.getReportParam(this.editForm);
    if (reportParam.rid !== null) {
      this.subscribeToSaveResponse(this.reportParamService.update(reportParam));
    } else {
      this.subscribeToSaveResponse(this.reportParamService.create(reportParam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportParam>>): void {
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

  protected updateForm(reportParam: IReportParam): void {
    this.reportParam = reportParam;
    this.reportParamFormService.resetForm(this.editForm, reportParam);

    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      reportParam.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.reportParam?.report)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
