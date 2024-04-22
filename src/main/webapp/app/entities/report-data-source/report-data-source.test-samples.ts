import { IReportDataSource, NewReportDataSource } from './report-data-source.model';

export const sampleWithRequiredData: IReportDataSource = {
  rid: 'f4d2aed9-248e-4104-9688-a32fe37fcc05',
};

export const sampleWithPartialData: IReportDataSource = {
  rid: 'c0208a59-3699-40f4-9df1-f5a33e9268d5',
  url: 'https://strict-substance.com',
  password: 'than',
};

export const sampleWithFullData: IReportDataSource = {
  rid: 'b5f60223-72b8-4078-a498-42b9678741ed',
  type: 'content',
  url: 'https://honored-possession.biz',
  user: 'mmm now hm',
  password: 'limp gosh',
};

export const sampleWithNewData: NewReportDataSource = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
