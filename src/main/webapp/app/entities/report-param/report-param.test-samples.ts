import { IReportParam, NewReportParam } from './report-param.model';

export const sampleWithRequiredData: IReportParam = {
  rid: '57f18b3b-9dd4-4eec-8694-e58f44d75d55',
};

export const sampleWithPartialData: IReportParam = {
  rid: 'bab158f3-4ca6-4282-80ef-6946bc0b33a8',
  type: 'versus though',
  conversionRule: 'shiny broadly',
};

export const sampleWithFullData: IReportParam = {
  rid: 'fb257ec1-4391-4196-a8d8-2e08296f8589',
  name: 'closely',
  type: 'zone er',
  value: 'phooey west justly',
  conversionRule: 'yet across',
};

export const sampleWithNewData: NewReportParam = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
