import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReportColumnMapping } from '../report-column-mapping.model';

@Component({
  standalone: true,
  selector: 'jhi-report-column-mapping-detail',
  templateUrl: './report-column-mapping-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReportColumnMappingDetailComponent {
  reportColumnMapping = input<IReportColumnMapping | null>(null);

  previousState(): void {
    window.history.back();
  }
}
