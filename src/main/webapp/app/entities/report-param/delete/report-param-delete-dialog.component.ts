import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportParam } from '../report-param.model';
import { ReportParamService } from '../service/report-param.service';

@Component({
  standalone: true,
  templateUrl: './report-param-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportParamDeleteDialogComponent {
  reportParam?: IReportParam;

  protected reportParamService = inject(ReportParamService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.reportParamService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
