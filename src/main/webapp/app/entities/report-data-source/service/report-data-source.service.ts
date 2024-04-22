import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportDataSource, NewReportDataSource } from '../report-data-source.model';

export type PartialUpdateReportDataSource = Partial<IReportDataSource> & Pick<IReportDataSource, 'rid'>;

export type EntityResponseType = HttpResponse<IReportDataSource>;
export type EntityArrayResponseType = HttpResponse<IReportDataSource[]>;

@Injectable({ providedIn: 'root' })
export class ReportDataSourceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-data-sources');

  create(reportDataSource: NewReportDataSource): Observable<EntityResponseType> {
    return this.http.post<IReportDataSource>(this.resourceUrl, reportDataSource, { observe: 'response' });
  }

  update(reportDataSource: IReportDataSource): Observable<EntityResponseType> {
    return this.http.put<IReportDataSource>(
      `${this.resourceUrl}/${this.getReportDataSourceIdentifier(reportDataSource)}`,
      reportDataSource,
      { observe: 'response' },
    );
  }

  partialUpdate(reportDataSource: PartialUpdateReportDataSource): Observable<EntityResponseType> {
    return this.http.patch<IReportDataSource>(
      `${this.resourceUrl}/${this.getReportDataSourceIdentifier(reportDataSource)}`,
      reportDataSource,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReportDataSource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportDataSource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportDataSourceIdentifier(reportDataSource: Pick<IReportDataSource, 'rid'>): string {
    return reportDataSource.rid;
  }

  compareReportDataSource(o1: Pick<IReportDataSource, 'rid'> | null, o2: Pick<IReportDataSource, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportDataSourceIdentifier(o1) === this.getReportDataSourceIdentifier(o2) : o1 === o2;
  }

  addReportDataSourceToCollectionIfMissing<Type extends Pick<IReportDataSource, 'rid'>>(
    reportDataSourceCollection: Type[],
    ...reportDataSourcesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportDataSources: Type[] = reportDataSourcesToCheck.filter(isPresent);
    if (reportDataSources.length > 0) {
      const reportDataSourceCollectionIdentifiers = reportDataSourceCollection.map(reportDataSourceItem =>
        this.getReportDataSourceIdentifier(reportDataSourceItem),
      );
      const reportDataSourcesToAdd = reportDataSources.filter(reportDataSourceItem => {
        const reportDataSourceIdentifier = this.getReportDataSourceIdentifier(reportDataSourceItem);
        if (reportDataSourceCollectionIdentifiers.includes(reportDataSourceIdentifier)) {
          return false;
        }
        reportDataSourceCollectionIdentifiers.push(reportDataSourceIdentifier);
        return true;
      });
      return [...reportDataSourcesToAdd, ...reportDataSourceCollection];
    }
    return reportDataSourceCollection;
  }
}
