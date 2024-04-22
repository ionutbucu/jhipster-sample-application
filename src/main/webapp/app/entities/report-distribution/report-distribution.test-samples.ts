import { IReportDistribution, NewReportDistribution } from './report-distribution.model';

export const sampleWithRequiredData: IReportDistribution = {
  rid: '80ab62f9-bd1c-445c-a9c9-afbecfc30064',
  email: '1G@A\'"#9I',
};

export const sampleWithPartialData: IReportDistribution = {
  rid: 'c8c432da-20ce-44b9-ad6d-68c32e827eb3',
  email: '20k7@VLTnk1',
  description: 'alcove but',
};

export const sampleWithFullData: IReportDistribution = {
  rid: 'f80a8804-b6d0-4545-be8c-0e781042d987',
  email: 'V-+-@{',
  description: 'dogwood',
};

export const sampleWithNewData: NewReportDistribution = {
  email: '`{^@^4<m|b',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
