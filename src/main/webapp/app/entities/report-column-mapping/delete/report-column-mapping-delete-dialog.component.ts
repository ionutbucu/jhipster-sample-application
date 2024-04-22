import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportColumnMapping } from '../report-column-mapping.model';
import { ReportColumnMappingService } from '../service/report-column-mapping.service';

@Component({
  standalone: true,
  templateUrl: './report-column-mapping-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportColumnMappingDeleteDialogComponent {
  reportColumnMapping?: IReportColumnMapping;

  protected reportColumnMappingService = inject(ReportColumnMappingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportColumnMappingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
