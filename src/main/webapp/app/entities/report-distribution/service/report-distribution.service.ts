import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportDistribution, NewReportDistribution } from '../report-distribution.model';

export type PartialUpdateReportDistribution = Partial<IReportDistribution> & Pick<IReportDistribution, 'rid'>;

export type EntityResponseType = HttpResponse<IReportDistribution>;
export type EntityArrayResponseType = HttpResponse<IReportDistribution[]>;

@Injectable({ providedIn: 'root' })
export class ReportDistributionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-distributions');

  create(reportDistribution: NewReportDistribution): Observable<EntityResponseType> {
    return this.http.post<IReportDistribution>(this.resourceUrl, reportDistribution, { observe: 'response' });
  }

  update(reportDistribution: IReportDistribution): Observable<EntityResponseType> {
    return this.http.put<IReportDistribution>(
      `${this.resourceUrl}/${this.getReportDistributionIdentifier(reportDistribution)}`,
      reportDistribution,
      { observe: 'response' },
    );
  }

  partialUpdate(reportDistribution: PartialUpdateReportDistribution): Observable<EntityResponseType> {
    return this.http.patch<IReportDistribution>(
      `${this.resourceUrl}/${this.getReportDistributionIdentifier(reportDistribution)}`,
      reportDistribution,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReportDistribution>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportDistribution[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportDistributionIdentifier(reportDistribution: Pick<IReportDistribution, 'rid'>): string {
    return reportDistribution.rid;
  }

  compareReportDistribution(o1: Pick<IReportDistribution, 'rid'> | null, o2: Pick<IReportDistribution, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportDistributionIdentifier(o1) === this.getReportDistributionIdentifier(o2) : o1 === o2;
  }

  addReportDistributionToCollectionIfMissing<Type extends Pick<IReportDistribution, 'rid'>>(
    reportDistributionCollection: Type[],
    ...reportDistributionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportDistributions: Type[] = reportDistributionsToCheck.filter(isPresent);
    if (reportDistributions.length > 0) {
      const reportDistributionCollectionIdentifiers = reportDistributionCollection.map(reportDistributionItem =>
        this.getReportDistributionIdentifier(reportDistributionItem),
      );
      const reportDistributionsToAdd = reportDistributions.filter(reportDistributionItem => {
        const reportDistributionIdentifier = this.getReportDistributionIdentifier(reportDistributionItem);
        if (reportDistributionCollectionIdentifiers.includes(reportDistributionIdentifier)) {
          return false;
        }
        reportDistributionCollectionIdentifiers.push(reportDistributionIdentifier);
        return true;
      });
      return [...reportDistributionsToAdd, ...reportDistributionCollection];
    }
    return reportDistributionCollection;
  }
}
