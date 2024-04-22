import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReportDataSource } from '../report-data-source.model';
import { ReportDataSourceService } from '../service/report-data-source.service';
import { ReportDataSourceFormService, ReportDataSourceFormGroup } from './report-data-source-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-data-source-update',
  templateUrl: './report-data-source-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportDataSourceUpdateComponent implements OnInit {
  isSaving = false;
  reportDataSource: IReportDataSource | null = null;

  protected reportDataSourceService = inject(ReportDataSourceService);
  protected reportDataSourceFormService = inject(ReportDataSourceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportDataSourceFormGroup = this.reportDataSourceFormService.createReportDataSourceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportDataSource }) => {
      this.reportDataSource = reportDataSource;
      if (reportDataSource) {
        this.updateForm(reportDataSource);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportDataSource = this.reportDataSourceFormService.getReportDataSource(this.editForm);
    if (reportDataSource.rid !== null) {
      this.subscribeToSaveResponse(this.reportDataSourceService.update(reportDataSource));
    } else {
      this.subscribeToSaveResponse(this.reportDataSourceService.create(reportDataSource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportDataSource>>): void {
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

  protected updateForm(reportDataSource: IReportDataSource): void {
    this.reportDataSource = reportDataSource;
    this.reportDataSourceFormService.resetForm(this.editForm, reportDataSource);
  }
}
