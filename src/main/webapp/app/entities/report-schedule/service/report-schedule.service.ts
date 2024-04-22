import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportSchedule, NewReportSchedule } from '../report-schedule.model';

export type PartialUpdateReportSchedule = Partial<IReportSchedule> & Pick<IReportSchedule, 'rid'>;

export type EntityResponseType = HttpResponse<IReportSchedule>;
export type EntityArrayResponseType = HttpResponse<IReportSchedule[]>;

@Injectable({ providedIn: 'root' })
export class ReportScheduleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-schedules');

  create(reportSchedule: NewReportSchedule): Observable<EntityResponseType> {
    return this.http.post<IReportSchedule>(this.resourceUrl, reportSchedule, { observe: 'response' });
  }

  update(reportSchedule: IReportSchedule): Observable<EntityResponseType> {
    return this.http.put<IReportSchedule>(`${this.resourceUrl}/${this.getReportScheduleIdentifier(reportSchedule)}`, reportSchedule, {
      observe: 'response',
    });
  }

  partialUpdate(reportSchedule: PartialUpdateReportSchedule): Observable<EntityResponseType> {
    return this.http.patch<IReportSchedule>(`${this.resourceUrl}/${this.getReportScheduleIdentifier(reportSchedule)}`, reportSchedule, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReportSchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportSchedule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportScheduleIdentifier(reportSchedule: Pick<IReportSchedule, 'rid'>): string {
    return reportSchedule.rid;
  }

  compareReportSchedule(o1: Pick<IReportSchedule, 'rid'> | null, o2: Pick<IReportSchedule, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportScheduleIdentifier(o1) === this.getReportScheduleIdentifier(o2) : o1 === o2;
  }

  addReportScheduleToCollectionIfMissing<Type extends Pick<IReportSchedule, 'rid'>>(
    reportScheduleCollection: Type[],
    ...reportSchedulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportSchedules: Type[] = reportSchedulesToCheck.filter(isPresent);
    if (reportSchedules.length > 0) {
      const reportScheduleCollectionIdentifiers = reportScheduleCollection.map(reportScheduleItem =>
        this.getReportScheduleIdentifier(reportScheduleItem),
      );
      const reportSchedulesToAdd = reportSchedules.filter(reportScheduleItem => {
        const reportScheduleIdentifier = this.getReportScheduleIdentifier(reportScheduleItem);
        if (reportScheduleCollectionIdentifiers.includes(reportScheduleIdentifier)) {
          return false;
        }
        reportScheduleCollectionIdentifiers.push(reportScheduleIdentifier);
        return true;
      });
      return [...reportSchedulesToAdd, ...reportScheduleCollection];
    }
    return reportScheduleCollection;
  }
}
