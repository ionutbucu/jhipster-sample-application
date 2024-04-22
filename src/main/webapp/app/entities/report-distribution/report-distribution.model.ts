import { IReport } from 'app/entities/report/report.model';

export interface IReportDistribution {
  rid: string;
  email?: string | null;
  description?: string | null;
  report?: Pick<IReport, 'rid'> | null;
}

export type NewReportDistribution = Omit<IReportDistribution, 'rid'> & { rid: null };
