import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';

const reportExecutionResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportExecution> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportExecutionService)
      .find(id)
      .pipe(
        mergeMap((reportExecution: HttpResponse<IReportExecution>) => {
          if (reportExecution.body) {
            return of(reportExecution.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportExecutionResolve;
