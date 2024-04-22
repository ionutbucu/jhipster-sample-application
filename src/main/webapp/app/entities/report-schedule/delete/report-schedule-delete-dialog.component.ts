import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportSchedule } from '../report-schedule.model';
import { ReportScheduleService } from '../service/report-schedule.service';

@Component({
  standalone: true,
  templateUrl: './report-schedule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportScheduleDeleteDialogComponent {
  reportSchedule?: IReportSchedule;

  protected reportScheduleService = inject(ReportScheduleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportScheduleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
