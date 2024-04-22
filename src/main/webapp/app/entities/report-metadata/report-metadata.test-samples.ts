import { IReportMetadata, NewReportMetadata } from './report-metadata.model';

export const sampleWithRequiredData: IReportMetadata = {
  rid: '024b9d58-ac38-405a-a9b1-db717f78f18f',
};

export const sampleWithPartialData: IReportMetadata = {
  rid: 'c36b7f9c-fd4e-4f35-8caa-5d5facc5b274',
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReportMetadata = {
  rid: 'c110a3d3-1657-4dc6-a315-6efee2055aa6',
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportMetadata = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
