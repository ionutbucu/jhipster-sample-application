import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportMetadata } from '../report-metadata.model';
import { ReportMetadataService } from '../service/report-metadata.service';

@Component({
  standalone: true,
  templateUrl: './report-metadata-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportMetadataDeleteDialogComponent {
  reportMetadata?: IReportMetadata;

  protected reportMetadataService = inject(ReportMetadataService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportMetadataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
