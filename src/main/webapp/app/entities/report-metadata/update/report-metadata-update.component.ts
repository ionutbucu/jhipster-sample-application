import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ReportMetadataService } from '../service/report-metadata.service';
import { IReportMetadata } from '../report-metadata.model';
import { ReportMetadataFormService, ReportMetadataFormGroup } from './report-metadata-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-metadata-update',
  templateUrl: './report-metadata-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportMetadataUpdateComponent implements OnInit {
  isSaving = false;
  reportMetadata: IReportMetadata | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportMetadataService = inject(ReportMetadataService);
  protected reportMetadataFormService = inject(ReportMetadataFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportMetadataFormGroup = this.reportMetadataFormService.createReportMetadataFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportMetadata }) => {
      this.reportMetadata = reportMetadata;
      if (reportMetadata) {
        this.updateForm(reportMetadata);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key }),
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportMetadata = this.reportMetadataFormService.getReportMetadata(this.editForm);
    if (reportMetadata.rid !== null) {
      this.subscribeToSaveResponse(this.reportMetadataService.update(reportMetadata));
    } else {
      this.subscribeToSaveResponse(this.reportMetadataService.create(reportMetadata));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportMetadata>>): void {
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

  protected updateForm(reportMetadata: IReportMetadata): void {
    this.reportMetadata = reportMetadata;
    this.reportMetadataFormService.resetForm(this.editForm, reportMetadata);
  }
}
