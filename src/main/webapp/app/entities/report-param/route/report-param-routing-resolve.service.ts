import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportParam } from '../report-param.model';
import { ReportParamService } from '../service/report-param.service';

const reportParamResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportParam> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportParamService)
      .find(id)
      .pipe(
        mergeMap((reportParam: HttpResponse<IReportParam>) => {
          if (reportParam.body) {
            return of(reportParam.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportParamResolve;
