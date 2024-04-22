import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportDataSource } from '../report-data-source.model';
import { ReportDataSourceService } from '../service/report-data-source.service';

@Component({
  standalone: true,
  templateUrl: './report-data-source-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportDataSourceDeleteDialogComponent {
  reportDataSource?: IReportDataSource;

  protected reportDataSourceService = inject(ReportDataSourceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportDataSourceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
