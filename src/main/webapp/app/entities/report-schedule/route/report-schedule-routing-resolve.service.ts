import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportSchedule } from '../report-schedule.model';
import { ReportScheduleService } from '../service/report-schedule.service';

const reportScheduleResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportSchedule> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportScheduleService)
      .find(id)
      .pipe(
        mergeMap((reportSchedule: HttpResponse<IReportSchedule>) => {
          if (reportSchedule.body) {
            return of(reportSchedule.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportScheduleResolve;
