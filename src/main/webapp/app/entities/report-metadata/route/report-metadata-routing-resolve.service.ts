import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportMetadata } from '../report-metadata.model';
import { ReportMetadataService } from '../service/report-metadata.service';

const reportMetadataResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportMetadata> => {
  const id = route.params['rid'];
  if (id) {
    return inject(ReportMetadataService)
      .find(id)
      .pipe(
        mergeMap((reportMetadata: HttpResponse<IReportMetadata>) => {
          if (reportMetadata.body) {
            return of(reportMetadata.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reportMetadataResolve;
