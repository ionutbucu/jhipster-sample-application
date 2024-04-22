import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportColumnMapping } from '../report-column-mapping.model';
import { ReportColumnMappingService } from '../service/report-column-mapping.service';

const reportColumnMappingResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportColumnMapping> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportColumnMappingService)
      .find(id)
      .pipe(
        mergeMap((reportColumnMapping: HttpResponse<IReportColumnMapping>) => {
          if (reportColumnMapping.body) {
            return of(reportColumnMapping.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportColumnMappingResolve;
