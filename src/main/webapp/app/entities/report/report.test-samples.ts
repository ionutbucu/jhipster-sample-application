import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  rid: '04b554d0-124f-4e42-aa6a-e1b8845d028b',
  name: 'lightly verbally before',
  query: 'corner micturate forebear',
  fileName: 'blah actual avocado',
};

export const sampleWithPartialData: IReport = {
  rid: 'de31e1fd-60a3-41f8-a078-76300c52ae3d',
  name: 'conjoin tragic till',
  description: 'weight',
  query: 'unnecessarily uh-huh underneath',
  queryType: 'NATIVE_QUERY',
  fileName: 'valiantly',
  licenseHolder: 'squirm boohoo',
  owner: 'noisily cutlet delayed',
};

export const sampleWithFullData: IReport = {
  rid: 'fe0daf68-7402-4113-b6af-0d220336f929',
  cid: 'impure whoever fully',
  name: 'joint mansion',
  description: 'nor pace pessimistic',
  query: 'tillXXXXXX',
  queryType: 'NATIVE_QUERY',
  fileName: 'willfully inside',
  reportType: 'CSV',
  licenseHolder: 'notwithstanding',
  owner: 'incidentally once',
};

export const sampleWithNewData: NewReport = {
  name: 'educated smile unwrap',
  query: 'ick jumpyX',
  fileName: 'amongst',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
