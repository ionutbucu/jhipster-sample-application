import { IReport } from 'app/entities/report/report.model';

export interface IReportColumnMapping {
  rid: string;
  sourceColumnName?: string | null;
  sourceColumnIndex?: number | null;
  columnTitle?: string | null;
  lang?: string | null;
  report?: Pick<IReport, 'rid'> | null;
}

export type NewReportColumnMapping = Omit<IReportColumnMapping, 'rid'> & { rid: null };
