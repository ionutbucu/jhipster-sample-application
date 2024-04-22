import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';

@Component({
  standalone: true,
  templateUrl: './report-execution-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportExecutionDeleteDialogComponent {
  reportExecution?: IReportExecution;

  protected reportExecutionService = inject(ReportExecutionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportExecutionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
