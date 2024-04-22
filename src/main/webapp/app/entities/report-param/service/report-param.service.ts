import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportParam, NewReportParam } from '../report-param.model';

export type PartialUpdateReportParam = Partial<IReportParam> & Pick<IReportParam, 'rid'>;

export type EntityResponseType = HttpResponse<IReportParam>;
export type EntityArrayResponseType = HttpResponse<IReportParam[]>;

@Injectable({ providedIn: 'root' })
export class ReportParamService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-params');

  create(reportParam: NewReportParam): Observable<EntityResponseType> {
    return this.http.post<IReportParam>(this.resourceUrl, reportParam, { observe: 'response' });
  }

  update(reportParam: IReportParam): Observable<EntityResponseType> {
    return this.http.put<IReportParam>(`${this.resourceUrl}/${this.getReportParamIdentifier(reportParam)}`, reportParam, {
      observe: 'response',
    });
  }

  partialUpdate(reportParam: PartialUpdateReportParam): Observable<EntityResponseType> {
    return this.http.patch<IReportParam>(`${this.resourceUrl}/${this.getReportParamIdentifier(reportParam)}`, reportParam, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReportParam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportParam[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportParamIdentifier(reportParam: Pick<IReportParam, 'rid'>): string {
    return reportParam.rid;
  }

  compareReportParam(o1: Pick<IReportParam, 'rid'> | null, o2: Pick<IReportParam, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportParamIdentifier(o1) === this.getReportParamIdentifier(o2) : o1 === o2;
  }

  addReportParamToCollectionIfMissing<Type extends Pick<IReportParam, 'rid'>>(
    reportParamCollection: Type[],
    ...reportParamsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportParams: Type[] = reportParamsToCheck.filter(isPresent);
    if (reportParams.length > 0) {
      const reportParamCollectionIdentifiers = reportParamCollection.map(reportParamItem => this.getReportParamIdentifier(reportParamItem));
      const reportParamsToAdd = reportParams.filter(reportParamItem => {
        const reportParamIdentifier = this.getReportParamIdentifier(reportParamItem);
        if (reportParamCollectionIdentifiers.includes(reportParamIdentifier)) {
          return false;
        }
        reportParamCollectionIdentifiers.push(reportParamIdentifier);
        return true;
      });
      return [...reportParamsToAdd, ...reportParamCollection];
    }
    return reportParamCollection;
  }
}
