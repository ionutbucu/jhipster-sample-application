import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportExecution, NewReportExecution } from '../report-execution.model';

export type PartialUpdateReportExecution = Partial<IReportExecution> & Pick<IReportExecution, 'rid'>;

type RestOf<T extends IReportExecution | NewReportExecution> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestReportExecution = RestOf<IReportExecution>;

export type NewRestReportExecution = RestOf<NewReportExecution>;

export type PartialUpdateRestReportExecution = RestOf<PartialUpdateReportExecution>;

export type EntityResponseType = HttpResponse<IReportExecution>;
export type EntityArrayResponseType = HttpResponse<IReportExecution[]>;

@Injectable({ providedIn: 'root' })
export class ReportExecutionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-executions');

  create(reportExecution: NewReportExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportExecution);
    return this.http
      .post<RestReportExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportExecution: IReportExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportExecution);
    return this.http
      .put<RestReportExecution>(`${this.resourceUrl}/${this.getReportExecutionIdentifier(reportExecution)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportExecution: PartialUpdateReportExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportExecution);
    return this.http
      .patch<RestReportExecution>(`${this.resourceUrl}/${this.getReportExecutionIdentifier(reportExecution)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestReportExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportExecution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportExecutionIdentifier(reportExecution: Pick<IReportExecution, 'rid'>): string {
    return reportExecution.rid;
  }

  compareReportExecution(o1: Pick<IReportExecution, 'rid'> | null, o2: Pick<IReportExecution, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportExecutionIdentifier(o1) === this.getReportExecutionIdentifier(o2) : o1 === o2;
  }

  addReportExecutionToCollectionIfMissing<Type extends Pick<IReportExecution, 'rid'>>(
    reportExecutionCollection: Type[],
    ...reportExecutionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportExecutions: Type[] = reportExecutionsToCheck.filter(isPresent);
    if (reportExecutions.length > 0) {
      const reportExecutionCollectionIdentifiers = reportExecutionCollection.map(reportExecutionItem =>
        this.getReportExecutionIdentifier(reportExecutionItem),
      );
      const reportExecutionsToAdd = reportExecutions.filter(reportExecutionItem => {
        const reportExecutionIdentifier = this.getReportExecutionIdentifier(reportExecutionItem);
        if (reportExecutionCollectionIdentifiers.includes(reportExecutionIdentifier)) {
          return false;
        }
        reportExecutionCollectionIdentifiers.push(reportExecutionIdentifier);
        return true;
      });
      return [...reportExecutionsToAdd, ...reportExecutionCollection];
    }
    return reportExecutionCollection;
  }

  protected convertDateFromClient<T extends IReportExecution | NewReportExecution | PartialUpdateReportExecution>(
    reportExecution: T,
  ): RestOf<T> {
    return {
      ...reportExecution,
      date: reportExecution.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportExecution: RestReportExecution): IReportExecution {
    return {
      ...restReportExecution,
      date: restReportExecution.date ? dayjs(restReportExecution.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportExecution>): HttpResponse<IReportExecution> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportExecution[]>): HttpResponse<IReportExecution[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
