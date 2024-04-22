import { IReportColumnMapping, NewReportColumnMapping } from './report-column-mapping.model';

export const sampleWithRequiredData: IReportColumnMapping = {
  rid: '60396472-c0db-42d0-924d-e0cf65abc9b8',
};

export const sampleWithPartialData: IReportColumnMapping = {
  rid: 'e13c44d7-e285-462d-800b-c6ed3e94259e',
  sourceColumnName: 'clam whenever',
  lang: 'qua shyly honestly',
};

export const sampleWithFullData: IReportColumnMapping = {
  rid: '65c381e1-1cc2-46fb-806d-3a3f0c422886',
  sourceColumnName: 'playful once black-and-white',
  sourceColumnIndex: 31958,
  columnTitle: 'amongst',
  lang: 'than so',
};

export const sampleWithNewData: NewReportColumnMapping = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
