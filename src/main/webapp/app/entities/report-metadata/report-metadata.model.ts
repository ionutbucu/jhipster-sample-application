export interface IReportMetadata {
  rid: string;
  metadata?: string | null;
}

export type NewReportMetadata = Omit<IReportMetadata, 'rid'> & { rid: null };
