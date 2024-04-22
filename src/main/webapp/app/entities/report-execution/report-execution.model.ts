import dayjs from 'dayjs/esm';
import { IReport } from 'app/entities/report/report.model';

export interface IReportExecution {
  rid: string;
  date?: dayjs.Dayjs | null;
  error?: string | null;
  url?: string | null;
  user?: string | null;
  additionalInfo?: string | null;
  report?: Pick<IReport, 'rid'> | null;
}

export type NewReportExecution = Omit<IReportExecution, 'rid'> & { rid: null };
