import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IReportDistribution } from '../report-distribution.model';
import { ReportDistributionService } from '../service/report-distribution.service';
import { ReportDistributionFormService, ReportDistributionFormGroup } from './report-distribution-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-distribution-update',
  templateUrl: './report-distribution-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportDistributionUpdateComponent implements OnInit {
  isSaving = false;
  reportDistribution: IReportDistribution | null = null;

  reportsSharedCollection: IReport[] = [];

  protected reportDistributionService = inject(ReportDistributionService);
  protected reportDistributionFormService = inject(ReportDistributionFormService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportDistributionFormGroup = this.reportDistributionFormService.createReportDistributionFormGroup();

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportDistribution }) => {
      this.reportDistribution = reportDistribution;
      if (reportDistribution) {
        this.updateForm(reportDistribution);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportDistribution = this.reportDistributionFormService.getReportDistribution(this.editForm);
    if (reportDistribution.rid !== null) {
      this.subscribeToSaveResponse(this.reportDistributionService.update(reportDistribution));
    } else {
      this.subscribeToSaveResponse(this.reportDistributionService.create(reportDistribution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportDistribution>>): void {
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

  protected updateForm(reportDistribution: IReportDistribution): void {
    this.reportDistribution = reportDistribution;
    this.reportDistributionFormService.resetForm(this.editForm, reportDistribution);

    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      reportDistribution.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(
        map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.reportDistribution?.report)),
      )
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
