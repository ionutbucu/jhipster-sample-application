import { IReportDataSource } from 'app/entities/report-data-source/report-data-source.model';
import { IReportMetadata } from 'app/entities/report-metadata/report-metadata.model';
import { QueryType } from 'app/entities/enumerations/query-type.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';

export interface IReport {
  rid: string;
  cid?: string | null;
  name?: string | null;
  description?: string | null;
  query?: string | null;
  queryType?: keyof typeof QueryType | null;
  fileName?: string | null;
  reportType?: keyof typeof ReportType | null;
  licenseHolder?: string | null;
  owner?: string | null;
  datasource?: Pick<IReportDataSource, 'rid'> | null;
  metadata?: Pick<IReportMetadata, 'rid'> | null;
}

export type NewReport = Omit<IReport, 'rid'> & { rid: null };
