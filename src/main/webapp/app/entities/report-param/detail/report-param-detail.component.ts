import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReportParam } from '../report-param.model';

@Component({
  standalone: true,
  selector: 'jhi-report-param-detail',
  templateUrl: './report-param-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReportParamDetailComponent {
  reportParam = input<IReportParam | null>(null);

  previousState(): void {
    window.history.back();
  }
}
