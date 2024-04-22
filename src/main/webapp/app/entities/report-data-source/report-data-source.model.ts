export interface IReportDataSource {
  rid: string;
  type?: string | null;
  url?: string | null;
  user?: string | null;
  password?: string | null;
}

export type NewReportDataSource = Omit<IReportDataSource, 'rid'> & { rid: null };
