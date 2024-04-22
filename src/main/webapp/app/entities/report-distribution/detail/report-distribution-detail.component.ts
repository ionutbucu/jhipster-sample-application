import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReportDistribution } from '../report-distribution.model';

@Component({
  standalone: true,
  selector: 'jhi-report-distribution-detail',
  templateUrl: './report-distribution-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReportDistributionDetailComponent {
  reportDistribution = input<IReportDistribution | null>(null);

  previousState(): void {
    window.history.back();
  }
}
