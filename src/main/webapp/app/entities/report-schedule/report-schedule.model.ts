import { IReport } from 'app/entities/report/report.model';

export interface IReportSchedule {
  rid: string;
  cron?: string | null;
  report?: Pick<IReport, 'rid'> | null;
}

export type NewReportSchedule = Omit<IReportSchedule, 'rid'> & { rid: null };
