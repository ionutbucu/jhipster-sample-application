import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportDataSource } from '../report-data-source.model';
import { ReportDataSourceService } from '../service/report-data-source.service';

const reportDataSourceResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportDataSource> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportDataSourceService)
      .find(id)
      .pipe(
        mergeMap((reportDataSource: HttpResponse<IReportDataSource>) => {
          if (reportDataSource.body) {
            return of(reportDataSource.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportDataSourceResolve;
