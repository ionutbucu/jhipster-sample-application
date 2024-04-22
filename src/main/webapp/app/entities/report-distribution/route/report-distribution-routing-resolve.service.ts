import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportDistribution } from '../report-distribution.model';
import { ReportDistributionService } from '../service/report-distribution.service';

const reportDistributionResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportDistribution> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportDistributionService)
      .find(id)
      .pipe(
        mergeMap((reportDistribution: HttpResponse<IReportDistribution>) => {
          if (reportDistribution.body) {
            return of(reportDistribution.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportDistributionResolve;
