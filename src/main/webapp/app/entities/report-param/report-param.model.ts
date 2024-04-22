import { IReport } from 'app/entities/report/report.model';

export interface IReportParam {
  rid: string;
  name?: string | null;
  type?: string | null;
  value?: string | null;
  conversionRule?: string | null;
  report?: Pick<IReport, 'rid'> | null;
}

export type NewReportParam = Omit<IReportParam, 'rid'> & { rid: null };
