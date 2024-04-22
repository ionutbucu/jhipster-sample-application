import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportDistribution } from '../report-distribution.model';
import { ReportDistributionService } from '../service/report-distribution.service';

@Component({
  standalone: true,
  templateUrl: './report-distribution-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportDistributionDeleteDialogComponent {
  reportDistribution?: IReportDistribution;

  protected reportDistributionService = inject(ReportDistributionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportDistributionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
