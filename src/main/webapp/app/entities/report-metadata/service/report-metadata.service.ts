import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportMetadata, NewReportMetadata } from '../report-metadata.model';

export type PartialUpdateReportMetadata = Partial<IReportMetadata> & Pick<IReportMetadata, 'rid'>;

export type EntityResponseType = HttpResponse<IReportMetadata>;
export type EntityArrayResponseType = HttpResponse<IReportMetadata[]>;

@Injectable({ providedIn: 'root' })
export class ReportMetadataService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-metadata');

  create(reportMetadata: NewReportMetadata): Observable<EntityResponseType> {
    return this.http.post<IReportMetadata>(this.resourceUrl, reportMetadata, { observe: 'response' });
  }

  update(reportMetadata: IReportMetadata): Observable<EntityResponseType> {
    return this.http.put<IReportMetadata>(`${this.resourceUrl}/${this.getReportMetadataIdentifier(reportMetadata)}`, reportMetadata, {
      observe: 'response',
    });
  }

  partialUpdate(reportMetadata: PartialUpdateReportMetadata): Observable<EntityResponseType> {
    return this.http.patch<IReportMetadata>(`${this.resourceUrl}/${this.getReportMetadataIdentifier(reportMetadata)}`, reportMetadata, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReportMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportMetadata[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportMetadataIdentifier(reportMetadata: Pick<IReportMetadata, 'rid'>): string {
    return reportMetadata.rid;
  }

  compareReportMetadata(o1: Pick<IReportMetadata, 'rid'> | null, o2: Pick<IReportMetadata, 'rid'> | null): boolean {
    return o1 && o2 ? this.getReportMetadataIdentifier(o1) === this.getReportMetadataIdentifier(o2) : o1 === o2;
  }

  addReportMetadataToCollectionIfMissing<Type extends Pick<IReportMetadata, 'rid'>>(
    reportMetadataCollection: Type[],
    ...reportMetadataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportMetadata: Type[] = reportMetadataToCheck.filter(isPresent);
    if (reportMetadata.length > 0) {
      const reportMetadataCollectionIdentifiers = reportMetadataCollection.map(reportMetadataItem =>
        this.getReportMetadataIdentifier(reportMetadataItem),
      );
      const reportMetadataToAdd = reportMetadata.filter(reportMetadataItem => {
        const reportMetadataIdentifier = this.getReportMetadataIdentifier(reportMetadataItem);
        if (reportMetadataCollectionIdentifiers.includes(reportMetadataIdentifier)) {
          return false;
        }
        reportMetadataCollectionIdentifiers.push(reportMetadataIdentifier);
        return true;
      });
      return [...reportMetadataToAdd, ...reportMetadataCollection];
    }
    return reportMetadataCollection;
  }
}
